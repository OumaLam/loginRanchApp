package com.Wasfa.front_end.services;

import com.Wasfa.front_end.Entity.Role;
import com.Wasfa.front_end.repository.EmployeRepositoryRanch;
import com.Wasfa.front_end.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Random;
@Service

public class serviceMotDePasse {
    @Autowired
    private EmployeRepositoryRanch employeRepository;
    @Autowired
    private EmailService emailService;
    @Autowired

    private RoleRepository roleRepository;

    public void envoiOtp(String email) {
        Role employe = roleRepository.findRoleByEmail(email)
                .orElseThrow(() -> new RuntimeException("Employé introuvable"));

        String otp = String.format("%06d", new Random().nextInt(999999));
        LocalDateTime expiration = LocalDateTime.now().plusMinutes(5);

        employe.setOtp(otp);
        employe.setOtpExpiration(expiration);
        employe.setOtpResendCount(0);
        employe.setLastOtpSentTime(LocalDateTime.now());
        roleRepository.save(employe);
        emailService.envoyerOtp(email, otp);
    }

}
