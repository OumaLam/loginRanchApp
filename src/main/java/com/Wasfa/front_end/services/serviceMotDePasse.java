package com.Wasfa.front_end.services;

import com.Wasfa.front_end.Entity.Employe;
import com.Wasfa.front_end.repository.EmployeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Random;
@Service

public class serviceMotDePasse {
    @Autowired
    private EmployeRepository employeRepository;
    @Autowired
    private EmailService emailService;

    public void envoiOtp(String email) {
        Employe employe = employeRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Employé introuvable"));

        String otp = String.format("%06d", new Random().nextInt(999999));
        LocalDateTime expiration = LocalDateTime.now().plusMinutes(5);

        employe.setOtp(otp);
        employe.setOtpExpiration(expiration);
        employe.setOtpResendCount(0);
        employe.setLastOtpSentTime(LocalDateTime.now());
        employeRepository.save(employe);
        emailService.envoyerOtp(email, otp);
    }

}
