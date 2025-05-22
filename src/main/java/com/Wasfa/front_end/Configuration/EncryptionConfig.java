package com.Wasfa.front_end.Configuration;

import jakarta.annotation.PostConstruct;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

@Component
@Data
public class EncryptionConfig {
    /**
     * Génère un hash SHA-256 du CIN en Base64
     *
     * @param cin le numéro CIN à hacher
     * @return le hash du CIN en Base64
     */
    private final String secretKey;

    public EncryptionConfig(@Value("${encryption.secret.key}") String secretKey) {
        // Valider la clé avant de l'assigner
        if (secretKey.length() != 32) {
            throw new IllegalArgumentException("La clé secrète doit être de 32 caractères pour AES-256");
        }
        this.secretKey = secretKey;
    }

    @PostConstruct
    public void init() {
        System.out.println("La clé secrète est : " + secretKey);
    }


       public static String hashCin(String cin) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hashBytes = digest.digest(cin.getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(hashBytes);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Erreur lors du hachage SHA-256", e);
        }
    }
}
