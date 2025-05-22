package com.Wasfa.front_end.services;

import com.Wasfa.front_end.Entity.Role;
import com.Wasfa.front_end.repository.EmployeRepositoryRanch;
import com.Wasfa.front_end.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
@Service
public class serviceOtp {
    @Autowired
    private EmployeRepositoryRanch employeRepository;
    private RoleRepository roleRepository;

    public boolean verifierOtp(String email, String otp) {
        Role employe = roleRepository.findRoleByEmail(email)
                .orElseThrow(() -> new RuntimeException("Employé introuvable"));

        if (employe.getOtp() == null || !employe.getOtp().equals(otp) ||
                employe.getOtpExpiration().isBefore(LocalDateTime.now())) {
            return false;
        }

        // Invalider l’OTP
        employe.setOtp(null);
        employe.setOtpExpiration(null);
        employe.setOtpResendCount(0);
        employe.setLastOtpSentTime(null);
        roleRepository.save(employe);

        return true;
    }

}
