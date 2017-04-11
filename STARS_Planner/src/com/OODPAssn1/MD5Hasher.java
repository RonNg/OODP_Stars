package com.OODPAssn1;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * The <code>MD5Hasher</code> class provides methods for one way hashing of plaintext <code>String</code> variables.
 */
public class MD5Hasher {
    /**
     * Does a one way hash of plaintext passwords
     *
     * @param plaintText String to hash
     * @return hashed plainText string. <code>null</code> if failed to hash.
     */
    public static String hash(String plaintText) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            //Add password byte to digest
            md.update(plaintText.getBytes());
            //Get the hash's bytes
            byte[] bytes = md.digest();

            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < bytes.length; ++i) {
                sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
            }
            return sb.toString(); //teh generated password
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }

}
