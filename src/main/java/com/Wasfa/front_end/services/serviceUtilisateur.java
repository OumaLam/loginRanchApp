package com.Wasfa.front_end.services;

import com.Wasfa.front_end.Entity.Employe;
import com.Wasfa.front_end.repository.EmployeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class serviceUtilisateur {

    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
     private EmployeRepository employeRepository;


    public void reinitialiserMotDePasse(String email, String nouveauMotDePasse) {

        if (nouveauMotDePasse == null || nouveauMotDePasse.trim().isEmpty()) {
            throw new IllegalArgumentException("Le mot de passe ne peut pas être vide ou null.");
        }

        String hashedPassword = passwordEncoder.encode(nouveauMotDePasse);
        Employe employe = employeRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Utilisateur introuvable avec cet email"));

        employe.setMotDePasse(hashedPassword);
        employeRepository.save(employe);
    }


}
