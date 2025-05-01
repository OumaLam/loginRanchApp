package com.Wasfa.front_end.Securite;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtTokenProvider {

    // La clé secrète utilisée pour signer les JWT
    private final String SECRET_KEY = "G8vsFvjSx72AdssZgURfLBRscX0nZZET7E5cLVG7qNE";

    // La durée de validité du token en millisecondes (par exemple, 1 heure)
    private final long VALIDITY = 1000 * 60 * 60;  // 1 heure

    // Méthode pour générer un JWT à partir de l'email de l'utilisateur
    public String generateToken(String email) {
        return Jwts.builder()
                .setSubject(email)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + VALIDITY))
                .signWith(SignatureAlgorithm.HS512, SECRET_KEY)
                .compact();
    }

    // Méthode pour valider un JWT
    public boolean validateToken(String token) {
        try {
            Jwts.parser()
                    .setSigningKey(SECRET_KEY)
                    .parseClaimsJws(token);
            return true;  // Le token est valide
        } catch (JwtException | IllegalArgumentException e) {
            return false;  // Le token est invalide
        }
    }

    // Méthode pour extraire l'email du token JWT
    public String getEmailFromToken(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(SECRET_KEY)
                .parseClaimsJws(token)
                .getBody();
        return claims.getSubject();
    }
}
