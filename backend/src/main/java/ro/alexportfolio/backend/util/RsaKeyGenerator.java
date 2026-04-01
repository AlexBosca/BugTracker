package ro.alexportfolio.backend.util;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.Key;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.util.Base64;

import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;

@Component
public class RsaKeyGenerator {
    private static final Path PUBLIC_KEY_PATH = Paths.get("target/certs/public.pem");
    private static final Path PRIVATE_KEY_PATH = Paths.get("target/certs/private.pem");

    @PostConstruct
    public void generateKeysIfMissing() throws Exception {
        
        if(Files.exists(PRIVATE_KEY_PATH) && Files.exists(PUBLIC_KEY_PATH)) {
            return;
        }

        Files.createDirectories(PUBLIC_KEY_PATH.getParent());

        KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");
        generator.initialize(2048);

        KeyPair keyPair = generator.generateKeyPair();

        String publicKeyContent = encodePublicKey(keyPair);
        String privateKeyContent = encodePrivateKey(keyPair);

        Files.writeString(PUBLIC_KEY_PATH, publicKeyContent);
        Files.writeString(PRIVATE_KEY_PATH, privateKeyContent);
    }

    private String encodePublicKey(KeyPair keyPair) {
        return "-----BEGIN PUBLIC KEY-----\n" +
            Base64.getEncoder().encodeToString(keyPair.getPublic().getEncoded()) +
            "\n-----END PUBLIC KEY-----";
    }

    private String encodePrivateKey(KeyPair keyPair) {
        return "-----BEGIN PRIVATE KEY-----\n" +
            Base64.getEncoder().encodeToString(keyPair.getPrivate().getEncoded()) +
            "\n-----END PRIVATE KEY-----";
    }
}
