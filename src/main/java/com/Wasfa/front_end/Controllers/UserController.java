package com.Wasfa.front_end.Controllers;

import com.Wasfa.front_end.Dto.UserResponseDTO;
import com.Wasfa.front_end.Entity.Employe;
import com.Wasfa.front_end.Securite.JwtService;
import com.Wasfa.front_end.repository.EmployeRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private JwtService jwtService;

    @Autowired
    private EmployeRepository employeRepository;

    @GetMapping("/me")
    public ResponseEntity<?> getUserInfo(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token manquant");
        }

        String token = authHeader.substring(7);
        String email = jwtService.extractUsername(token);

        Employe employe = employeRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Utilisateur non trouvé"));

        UserResponseDTO userDto = new UserResponseDTO(
                employe.getEmail(),
                employe.getNom(),
                employe.getPrenom(),
                employe.getFonction() // ou getLibelle(), selon ton modèle
        );

        return ResponseEntity.ok(userDto);
    }
}
