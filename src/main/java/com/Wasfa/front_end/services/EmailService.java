package com.Wasfa.front_end.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private  JavaMailSender mailSender;

    public  void envoyerOtp(String toEmail, String otp) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(toEmail);
        message.setSubject("Votre code OTP");
        message.setText("Voici votre code OTP : " + otp);
        message.setFrom("lamrabtioumaima2005@gmail.com");
        try {
            mailSender.send(message);
            System.out.println("Email envoyé !");
        } catch (Exception e) {
            System.err.println("Erreur d'envoi d'email : " + e.getMessage());
        }
    }
}
