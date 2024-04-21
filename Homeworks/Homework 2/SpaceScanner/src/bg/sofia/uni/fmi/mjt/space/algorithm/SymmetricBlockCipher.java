package bg.sofia.uni.fmi.mjt.space.algorithm;

import java.io.InputStream;
import java.io.OutputStream;

import bg.sofia.uni.fmi.mjt.space.exception.CipherException;

public interface SymmetricBlockCipher {
    /**
     * Encrypts the data from inputStream and puts it into outputStream
     *
     * @param inputStream  the input stream where the data is read from
     * @param outputStream the output stream where the encrypted result is written into
     * @throws CipherException if the encrypt/decrypt operation cannot be completed successfully
     */
    void encrypt(InputStream inputStream, OutputStream outputStream) throws CipherException;

    /**
     * Decrypts the data from inputStream and puts it into outputStream
     *
     * @param inputStream  the input stream where the data is read from
     * @param outputStream the output stream where the decrypted result is written into
     * @throws CipherException if the encrypt/decrypt operation cannot be completed successfully
     */
    void decrypt(InputStream inputStream, OutputStream outputStream) throws CipherException;
}

