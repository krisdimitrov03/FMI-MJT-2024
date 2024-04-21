package bg.sofia.uni.fmi.mjt.space.algorithm;

import bg.sofia.uni.fmi.mjt.space.exception.CipherException;

import javax.crypto.Cipher;
import javax.crypto.CipherOutputStream;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;
import java.util.List;

public class Rijndael implements SymmetricBlockCipher {

    private static final int BUFFER_SIZE = 1024;

    private static final int KEY_SIZE = 128;

    private final SecretKey secretKey;

    public Rijndael(SecretKey secretKey) {
        this.secretKey = secretKey;
    }

    @Override
    public void encrypt(InputStream inputStream, OutputStream outputStream) throws CipherException {
        try {
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);

            try (var cypherOutputStream = new CipherOutputStream(outputStream, cipher)) {
                List<String> data = List.of("my-", "secret-", "pass–", "and–", "other–", "stuff");

                for (String str : data) {
                    byte[] dataBytes = str.getBytes(StandardCharsets.UTF_8);
                    cypherOutputStream.write(dataBytes);
                }
            }
        } catch (Exception e) {
            throw new CipherException("encrypt operation cannot be completed successfully");
        }
    }

    @Override
    public void decrypt(InputStream inputStream, OutputStream outputStream) throws CipherException {
        try {
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.DECRYPT_MODE, secretKey);

            try (var decryptedOutputStream = new CipherOutputStream(outputStream, cipher)) {

                byte[] buffer = new byte[BUFFER_SIZE];
                int bytesRead;

                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    decryptedOutputStream.write(buffer, 0, bytesRead);
                }
            }
        } catch (Exception e) {
            throw new CipherException("decrypt operation cannot be completed successfully");
        }
    }

    public static SecretKey generateSecretKey() throws NoSuchAlgorithmException {
        KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
        keyGenerator.init(KEY_SIZE);
        return keyGenerator.generateKey();
    }

}
