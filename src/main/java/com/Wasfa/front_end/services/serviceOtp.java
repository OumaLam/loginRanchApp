package com.Wasfa.front_end.services;

import com.Wasfa.front_end.Entity.Employe;
import com.Wasfa.front_end.repository.EmployeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
@Service
public class serviceOtp {
    @Autowired
    private EmployeRepository employeRepository;


    public boolean verifierOtp(String email, String otp) {
        Employe employe = employeRepository.findByEmail(email)
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
        employeRepository.save(employe);

        return true;
    }

}
