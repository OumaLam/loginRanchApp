package com.Wasfa.front_end.Controllers;

import com.Wasfa.front_end.Dto.UserResponseDTO;
import com.Wasfa.front_end.Entity.Role;
import com.Wasfa.front_end.Securite.JwtService;
import com.Wasfa.front_end.repository.EmployeRepositoryRanch;
import com.Wasfa.front_end.repository.RoleRepository;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")

@RestController

@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private JwtService jwtService;

    @Autowired
    private EmployeRepositoryRanch employeRepository;
    private RoleRepository roleRepository;
    @GetMapping("/me")
    public ResponseEntity<?> getUserInfo(HttpServletRequest request) {
        // Lire le cookie auth_token
        String token = null;
        if (request.getCookies() != null) {
            for (Cookie cookie : request.getCookies()) {
                if ("auth_token".equals(cookie.getName())) {
                    token = cookie.getValue();
                }
            }
        }

        if (token == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token manquant (cookie)");
        }

        String email = jwtService.extractUsername(token);

        Role employe = roleRepository.findRoleByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Utilisateur non trouvé"));

        UserResponseDTO userDto = new UserResponseDTO(
                employe.getUsername(),
                employe.getEmployeRanch().getNom(),
                employe.getEmployeRanch().getPrenom(),
                employe.getRole()
        );

        return ResponseEntity.ok(userDto);
    }

}
