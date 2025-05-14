package com.Wasfa.front_end.Controllers;
import com.Wasfa.front_end.Dto.PasswordDto;
import com.Wasfa.front_end.services.*;
import jakarta.servlet.http.HttpServletResponse;
import com.Wasfa.front_end.Dto.LoginRequest;
import com.Wasfa.front_end.Dto.LoginResponse;
import com.Wasfa.front_end.Dto.OtpRequest;
import com.Wasfa.front_end.Entity.Employe;
import com.Wasfa.front_end.Securite.JwtService;
import com.Wasfa.front_end.repository.EmployeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;
import java.util.Random;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private EmployeRepository employeRepository;

    @Autowired
    private EmailService emailService;

    @Autowired
    private JwtService jwtService;
   @Autowired
    private serviceOtp serviceOtp;
   @Autowired
    private serviceMotDePasse serviceMotDePasse;

    @Autowired
    private serviceUtilisateur serviceUtilisateur;

    @Autowired
    private PasswordEncoder passwordEncoder; // Assure-toi que ce bean est bien défini dans ta config
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request, HttpServletResponse response) {
        Optional<Employe> employeOpt = employeRepository.findByEmail(request.getEmail());

        if (employeOpt.isEmpty()) {
            return ResponseEntity.status(401).body("Identifiants invalides");
        }

        Employe employe = employeOpt.get();

        // Vérification du mot de passe
        if (!passwordEncoder.matches(request.getMotDePasse(), employe.getMotDePasse())) {
            return ResponseEntity.status(401).body("Mot de passe incorrect");
        }

        // Vérification de la fonction si précisée
        if (request.getFonction() != null && !request.getFonction().equals(employe.getFonction())) {
            return ResponseEntity.status(403).body("Fonction invalide");
        } else {
            addJwtToCookie.addJwtToCookie(response, "fonction", employe.getFonction());
        }
        System.out.println("Fonction envoyée: " + request.getFonction());
        System.out.println("Fonction en base : " + employe.getFonction());

        // Si appareil reconnu => Connexion directe
        if (request.getDeviceId() != null && request.getDeviceId().equals(employe.getDeviceId())) {
            String token = jwtService.generateToken(employe);
            addJwtToCookie.addJwtToCookie(response, "auth_token", token);
            return ResponseEntity.ok(new LoginResponse("Connexion sans OTP", employe.getFonction(), token));
        }

        // 💡 Nouvelle logique : OTP avec limitation
        LocalDateTime now = LocalDateTime.now();

        if (employe.getLastOtpSentTime() == null || employe.getLastOtpSentTime().isBefore(now.minusHours(1))) {
            employe.setOtpResendCount(0);
        }

        if (employe.getOtpResendCount() >= 5) {
            return ResponseEntity.status(429).body("Trop de tentatives OTP. Réessaye dans une heure.");
        }

        String otp = String.format("%06d", new Random().nextInt(999999));
        employe.setOtp(otp);
        employe.setOtpExpiration(now.plusMinutes(2));
        employe.setLastOtpSentTime(now);
        employe.setOtpResendCount(employe.getOtpResendCount() + 1);
        employe.setDeviceId(request.getDeviceId());

        String token = jwtService.generateToken(employe);
        employe.setToken(token);

        employeRepository.save(employe);
        emailService.envoyerOtp(employe.getEmail(), otp);

        return ResponseEntity.ok(new LoginResponse("OTP envoyé à l'adresse email.", employe.getFonction()));
    }

    @PostMapping("/verifier-otp")
    public ResponseEntity<?> verifierOtp(@RequestBody OtpRequest request, HttpServletResponse response) {
        Optional<Employe> employeOpt = employeRepository.findByEmail(request.getEmail());

        if (employeOpt.isEmpty()) {
            return ResponseEntity.status(404).body("Utilisateur introuvable");
        }

        Employe employe = employeOpt.get();

        boolean otpValide = employe.getOtp() != null &&
                employe.getOtp().equals(request.getOtp()) &&
                employe.getOtpExpiration() != null &&
                employe.getOtpExpiration().isAfter(LocalDateTime.now());

        if (!otpValide) {
            return ResponseEntity.status(403).body("OTP invalide ou expiré");
        }

        // Réinitialisation OTP après succès
        employe.setOtp(null);
        employe.setOtpExpiration(null);
        employe.setOtpResendCount(0);
        employe.setLastOtpSentTime(null);

        String token = jwtService.generateToken(employe);
        addJwtToCookie.addJwtToCookie(response, "auth_token", token);
        employe.setToken(token);
        employeRepository.save(employe);

        return ResponseEntity.ok(new LoginResponse("🔐 Authentification réussie !", employe.getFonction(), token));
    }

    @PostMapping("/resend-otp")
    public ResponseEntity<?> resendOtp(@RequestBody OtpRequest request) {
        Optional<Employe> employeOpt = employeRepository.findByEmail(request.getEmail());

        if (employeOpt.isEmpty()) {
            return ResponseEntity.status(404).body("Utilisateur introuvable");
        }

        Employe employe = employeOpt.get();
        LocalDateTime now = LocalDateTime.now();

        // Vérifier si la limite de 5 renvois est atteinte
        if (employe.getLastOtpSentTime() == null || employe.getLastOtpSentTime().isBefore(now.minusHours(1))) {
            employe.setOtpResendCount(0);
        }

        if (employe.getOtpResendCount() >= 5) {
            return ResponseEntity.status(429).body("Trop de renvois d'OTP. Réessaye dans une heure.");
        }

        // Vérifie si l'OTP est expiré
        if (employe.getOtpExpiration() != null && employe.getOtpExpiration().isAfter(now)) {
            return ResponseEntity.status(400).body("L’OTP est encore valide.");
        }

        // Génère un nouvel OTP
        String otp = String.format("%06d", new Random().nextInt(999999));
        employe.setOtp(otp);
        employe.setOtpExpiration(now.plusMinutes(1));
        employe.setLastOtpSentTime(now);
        employe.setOtpResendCount(employe.getOtpResendCount() + 1);
        employeRepository.save(employe);

        emailService.envoyerOtp(employe.getEmail(), otp);

        return ResponseEntity.ok("Nouveau OTP envoyé.");

    }
        @PostMapping("/mot-de-passe-oublie")
        public ResponseEntity<?> envoyerOtp(@RequestBody Map<String, String> payload) {
            String email = payload.get("email");
            serviceMotDePasse.envoiOtp(email);
            return ResponseEntity.ok("Un code OTP a été envoyé à votre adresse email.");
        }

    @PostMapping("/nouveau-mot-de-passe")
    public ResponseEntity<?> changerMotDePasse(@RequestBody PasswordDto passwordDto) {
        //System.out.println("mot de passe " +passwordDto);
        String email = passwordDto.getEmail();
        String nouveauMdp = passwordDto.getNewPassword();
//        System.out.println("mot de passe " +nouveauMdp);
//        System.out.println("mot de passe " +email);
        serviceUtilisateur.reinitialiserMotDePasse(email, nouveauMdp);


        return ResponseEntity.ok("Mot de passe mis à jour,Connexion sans OTP");
    }


    @PostMapping("/verifier-otp-reinitialisation")
    public ResponseEntity<?> verifierOtp(@RequestBody Map<String, String> payload) {
        String email = payload.get("email");
        String otp = payload.get("otp");

        if (serviceOtp.verifierOtp(email, otp)) {
            return ResponseEntity.ok("OTP validé");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("OTP invalide ou expiré");
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletResponse response) {
        addJwtToCookie.deleteJwtInCookie(response,"fonction");
        addJwtToCookie.deleteJwtInCookie(response, "auth_token");

        return ResponseEntity.ok("Déconnexion réussie");
    }




}


