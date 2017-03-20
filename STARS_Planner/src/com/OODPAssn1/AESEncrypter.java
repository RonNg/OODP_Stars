package com.OODPAssn1;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;
import java.util.Base64;

/**
 * Created by Tweakisher on 16/3/2017.
 */
final public class AESEncrypter
{
    private AESEncrypter(){}

    public static String encrypt (String plainText, String encryptionKey, String IV ) throws Exception
    {
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        SecretKeySpec key = new SecretKeySpec(encryptionKey.getBytes("UTF-8"), "AES");

        cipher.init(Cipher.ENCRYPT_MODE, key, new IvParameterSpec(IV.getBytes("UTF-8")));

        return Base64.getEncoder().encodeToString(cipher.doFinal(plainText.getBytes("UTF-8")));
    }

    public static String decrypt(String cipherText, String encryptionKey, String IV) throws Exception
    {

        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        SecretKeySpec key = new SecretKeySpec(encryptionKey.getBytes("UTF-8"), "AES");

        cipher.init(Cipher.DECRYPT_MODE, key,new IvParameterSpec(IV.getBytes("UTF-8")));

        return new String(cipher.doFinal(Base64.getDecoder().decode(cipherText)),"UTF-8");
    }
}
