package com.Wasfa.front_end.Securite;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CryptageAES {

    private static final String ALGO = "AES/CBC/PKCS5Padding";
    private static final String AES = "AES";
    private static final int IV_LENGTH = 16;
    private static final Logger logger = Logger.getLogger(CryptageAES.class.getName());
    private final SecretKeySpec secretKey;

    public CryptageAES(String secret) {
        if (secret.length() != 32) {
            throw new IllegalArgumentException("La clé doit être de 32 caractères pour AES-256");
        }
        this.secretKey = new SecretKeySpec(secret.getBytes(), AES);
    }

    public String encrypt(String data) {
        try {
            // Génère un IV aléatoire
            byte[] iv = new byte[IV_LENGTH];
            SecureRandom secureRandom = new SecureRandom();
            secureRandom.nextBytes(iv);
            IvParameterSpec ivSpec = new IvParameterSpec(iv);

            // Chiffrement
            Cipher cipher = Cipher.getInstance(ALGO);
            cipher.init(Cipher.ENCRYPT_MODE, secretKey, ivSpec);
            byte[] encrypted = cipher.doFinal(data.getBytes());

            // Concatène IV + données chiffrées
            byte[] ivAndEncrypted = new byte[IV_LENGTH + encrypted.length];
            System.arraycopy(iv, 0, ivAndEncrypted, 0, IV_LENGTH);
            System.arraycopy(encrypted, 0, ivAndEncrypted, IV_LENGTH, encrypted.length);

            // Encode en Base64
            return Base64.getEncoder().encodeToString(ivAndEncrypted);

        } catch (Exception ex) {
            logger.log(Level.SEVERE, "Erreur lors du chiffrement", ex);
            throw new RuntimeException("Erreur lors du chiffrement", ex);
        }
    }

    public String decrypt(String encryptedData) {
        try {
            byte[] ivAndEncrypted = Base64.getDecoder().decode(encryptedData);

            // Sépare IV et données chiffrées
            byte[] iv = new byte[IV_LENGTH];
            byte[] encrypted = new byte[ivAndEncrypted.length - IV_LENGTH];
            System.arraycopy(ivAndEncrypted, 0, iv, 0, IV_LENGTH);
            System.arraycopy(ivAndEncrypted, IV_LENGTH, encrypted, 0, encrypted.length);

            IvParameterSpec ivSpec = new IvParameterSpec(iv);

            Cipher cipher = Cipher.getInstance(ALGO);
            cipher.init(Cipher.DECRYPT_MODE, secretKey, ivSpec);
            byte[] original = cipher.doFinal(encrypted);
            return new String(original);

        } catch (Exception ex) {
            logger.log(Level.SEVERE, "Erreur lors du déchiffrement", ex);
            throw new RuntimeException("Erreur lors du déchiffrement", ex);
        }
    }
}
