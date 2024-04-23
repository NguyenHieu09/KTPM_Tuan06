package vn.edu.iuh.fit.utils;

import org.apache.tomcat.util.codec.binary.Base64;

public class Base64EncodingText implements EncodingText {
    public String encrypt(String jsonText) throws Exception {
        return Base64.encodeBase64String(jsonText.getBytes());
    }

    public String decrypt(String encodeJson) throws Exception {
        return new String(Base64.decodeBase64(encodeJson));
    }
}
