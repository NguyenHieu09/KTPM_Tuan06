package vn.edu.iuh.fit.utils;

import javax.crypto.SecretKey;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

public interface EncodingText {
    final String CRYPT_ALGORITHM = "AES";
    final String DEFAULT_PASSWORD = "S3cr3t";
    String encrypt(String plainText)throws Exception;
    public String decrypt(String encryptedText)throws Exception ;
}
