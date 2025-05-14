package com.Wasfa.front_end.Configuration;

import jakarta.annotation.PostConstruct;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Data
public class EncryptionConfig {

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

}
