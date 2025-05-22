package com.Wasfa.front_end.Controllers;
import com.Wasfa.front_end.Dto.RoleAssignmentRequest;
import com.Wasfa.front_end.Entity.EmployeRanch;
import com.Wasfa.front_end.Entity.Role;
import com.Wasfa.front_end.Securite.CryptageAES;
import com.Wasfa.front_end.repository.EmployeRepositoryRanch;
import com.Wasfa.front_end.repository.RoleRepository;
import com.Wasfa.front_end.services.EmployeService;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.Wasfa.front_end.Configuration.EncryptionConfig.hashCin;

@RestController
@RequestMapping("/api/employes")
@CrossOrigin(origins = "http://localhost:3000")
@PreAuthorize("hasRole('ADMIN')")


public class EmployeController {

    @Value("${upload.directory}")
    private String derectory;



    @Autowired
    private CryptageAES crypto ;

    @Autowired
    public RoleRepository roleRepository;

    @Autowired
    public EmployeRepositoryRanch employeRepository;

    private final EmployeService service;

    public EmployeController(EmployeService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<?> addEmploye(
            @RequestPart("cin") String cin,
            @RequestPart("prenom") String prenom,
            @RequestPart("nom") String nom,
            @RequestPart("fonction") String fonction,
            @RequestPart("email") String email,
            @RequestPart("dateEmbauche") String date,
            @RequestPart("salaire") String salaire,
            @RequestPart(value = "contratPdf", required = true) MultipartFile contratPdf
    ) {
        try {
            // Chiffrement du CIN pour la vérification
            String cinChiffre = crypto.encrypt(cin);
            String cinHash = hashCin(cin); // SHA-256
            // Vérifier si un employé avec ce CIN existe déjà
            if (employeRepository.findByCinHash(cinHash).isPresent()) {
                Map<String, String> error = new HashMap<>();
                error.put("error", "Un employé avec ce CIN existe déjà.");
                return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
            }
            if (service.existsByEmail(email)) {
                Map<String, String> error = new HashMap<>();
                error.put("error", "Cet email est déjà utilisé.");
                return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
            }


            // Création de l'employé
            EmployeRanch employe = new EmployeRanch();
            employe.setCin(cinChiffre);
            employe.setCinHash(cinHash);
            employe.setPrenom(prenom);
            employe.setNom(nom);
            employe.setFonction(fonction);
            employe.setEmail(email);
            employe.setDateEmbauche(LocalDate.parse(date));
            employe.setSalaire(Double.parseDouble(salaire));

            EmployeRanch savedEmploye = service.saveEmploye(employe, contratPdf);

            // Déchiffrement du CIN pour la réponse
            savedEmploye.setCin(cin);

            Map<String, Object> response = new HashMap<>();
            response.put("message", "Employé ajouté avec succès !");
            response.put("employe", savedEmploye);

            return ResponseEntity.ok(response);
        } catch (IOException e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Échec de l'ajout de l'employé : " + e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Erreur inconnue : " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }



    @GetMapping
    public List<EmployeRanch> getAll() {     /// dernier modf
        return service.getAllEmployes();
    }


//    @GetMapping("/role/{role}")
//    public List<EmployeRanch> getByRole(@PathVariable String role) {
//        return service.getByRole(role);
//    }



    @GetMapping("/pdf/{filename:.+}")
    public ResponseEntity<UrlResource> getContratPdf(@PathVariable String filename) throws IOException {
         final String uploadDir = System.getProperty("user.dir") +"/"+ derectory;
        //System.out.println(uploadDir);
        File file = new File(uploadDir, filename);

        if (!file.exists()) {
            return ResponseEntity.notFound().build();
        }

        UrlResource resource = new UrlResource(file.toURI());

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + filename + "\"")
                .contentType(MediaType.APPLICATION_PDF)
                .body(resource);
    }


    @PutMapping("/{id}")
    public ResponseEntity<EmployeRanch> updateEmploye(@PathVariable String id, @RequestBody EmployeRanch employe) {
        return service.updateEmploye(id, employe)
                .map(updated -> {
                    // Déchiffrer le CIN avant de retourner l'objet
                    updated.setCin(crypto.decrypt(updated.getCin()));
                    return ResponseEntity.ok(updated);
                })
                .orElse(ResponseEntity.notFound().build());
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        return service.deleteEmploye(id)
                ? ResponseEntity.ok().build()
                : ResponseEntity.notFound().build();
    }


    @PostMapping("/assign-role")
    @PreAuthorize("hasRole('ADMIN')")

    public ResponseEntity<?> assignRole(@RequestBody RoleAssignmentRequest dto) {
        return service.assignRoleToEmploye(dto.getCin(), dto.getMotDePasse(), dto.getRole());
    }
    public record EmployeDTO(String cin, String nom,String email, String prenom, String role) {}

    @GetMapping("/avec-role")
    @PreAuthorize("hasRole('ADMIN')")

    public ResponseEntity<List<EmployeDTO>> getEmployesAvecRole() {
        List<Role> roles = roleRepository.findAll();

        List<EmployeDTO> employes = roles.stream()
                .map(role -> {
                    EmployeRanch emp = role.getEmployeRanch();
                    String cinDechiffre = crypto.decrypt(emp.getCin()); // 🔐 Déchiffrement

                    return new EmployeDTO(
                            cinDechiffre,
                            emp.getNom(),
                            emp.getEmail(),
                            emp.getPrenom(),
                            role.getRole()
                    );
                })
                .toList();

        return ResponseEntity.ok(employes);
    }



}

