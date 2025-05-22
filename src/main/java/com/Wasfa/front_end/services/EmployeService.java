package com.Wasfa.front_end.services;

import com.Wasfa.front_end.Entity.EmployeRanch;
import com.Wasfa.front_end.Entity.Role;
import com.Wasfa.front_end.Securite.CryptageAES;
import com.Wasfa.front_end.repository.EmployeRepositoryRanch;
import com.Wasfa.front_end.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static com.Wasfa.front_end.Configuration.EncryptionConfig.hashCin;

@Service
public class EmployeService {
    @Autowired
    private  EmployeRepositoryRanch repository;

    @Autowired
    private CryptageAES crypto ;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;


    @Value("${upload.directory}")
    private String uploadDir;



    public EmployeService(EmployeRepositoryRanch repository) {
        this.repository = repository;
    }
    public EmployeRanch saveEmploye(EmployeRanch employe, MultipartFile contratPdf) throws IOException {
        if (contratPdf != null && !contratPdf.isEmpty()) {
            // Vérifier la taille max 1MB
            long maxSize = 1 * 1024 * 1024;
            if (contratPdf.getSize() > maxSize) {
                throw new IOException("Fichier trop volumineux (max 1MB)");
            }

            // Créer le dossier s'il n'existe pas
            File uploadDirectory = new File(uploadDir);
            if (!uploadDirectory.exists() && !uploadDirectory.mkdirs()) {
                throw new IOException("Impossible de créer le dossier d'upload : " + uploadDir);
            }

            // Nom unique pour éviter les conflits
            String fileName = UUID.randomUUID() + "_" + contratPdf.getOriginalFilename();
            File file = new File(uploadDirectory, fileName);

            // Copier le fichier
            Files.copy(contratPdf.getInputStream(), file.toPath(), StandardCopyOption.REPLACE_EXISTING);

            // Enregistrer le nom du fichier
            employe.setContratPdf(fileName);
        }

        // Sauvegarde employé
        return repository.save(employe);
    }


    public List<EmployeRanch> getAllEmployes() {

        List<EmployeRanch> allEmployees = repository.findAll();

        for (EmployeRanch employe : allEmployees) {
            String cinChiffre = employe.getCin();
            String cinDechiffre = crypto.decrypt(cinChiffre);
            employe.setCin(cinDechiffre); // remplacer le champ par la version déchiffrée
        }
       System.out.println(allEmployees);
        return allEmployees;

    }


    public Optional<EmployeRanch> updateEmploye(String cin, EmployeRanch updatedEmploye) {
        String hashedCin = hashCin(cin); // Utiliser la version hachée pour chercher

        return repository.findByCinHash(hashedCin).map(emp -> {
            emp.setNom(updatedEmploye.getNom());
            emp.setPrenom(updatedEmploye.getPrenom());
            emp.setFonction(updatedEmploye.getFonction());
            emp.setEmail(updatedEmploye.getEmail());
            emp.setSalaire(updatedEmploye.getSalaire());
            emp.setDateEmbauche(updatedEmploye.getDateEmbauche());

            return repository.save(emp);
        });
    }

    public boolean deleteEmploye(String id) {
        String hashedCin = hashCin(id); // méthode hashage

        Optional<EmployeRanch> empOpt = repository.findByCinHash(hashedCin);
        if (empOpt.isPresent()) {
            repository.delete(empOpt.get()); // supprime par entité
            return true;
        }
        return false;
    }



    public ResponseEntity<?> assignRoleToEmploye(String cin, String motDePasse, String roleName) {
        String cinHashed = hashCin(cin); // 🔒 Hash pour recherche
        Optional<EmployeRanch> optEmploye = repository.findByCinHash(cinHashed);
        if (optEmploye.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Employé introuvable avec le CIN : " + cin);
        }

        EmployeRanch employe = optEmploye.get();

        Optional<Role> existingRole = roleRepository.findByEmployeRanch_Cin(cinHashed);
        if (existingRole.isPresent()) {
            Role r = existingRole.get();
            r.setMotDePasse(passwordEncoder.encode(motDePasse));
            r.setRole(roleName);
            roleRepository.save(r);
            return ResponseEntity.ok("Rôle mis à jour avec succès !");
        } else {
            Role role = new Role();
            role.setMotDePasse(passwordEncoder.encode(motDePasse));
            role.setRole(roleName);
            role.setEmployeRanch(employe);
            role.setEnabled(true);

            try {
                roleRepository.save(role);
                return ResponseEntity.ok("Rôle assigné avec succès !");
            } catch (Exception e) {
                e.printStackTrace();
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erreur lors de la sauvegarde du rôle");
            }
        }
    }


    public boolean existsByEmail(String email) {
        return repository.findByEmail(email).isPresent();
    }



}
