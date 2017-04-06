package com.OODPAssn1;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by Tweakisher on 6/4/2017.
 */
public class MD5Hasher
{
    public static String hash (String plaintText)
    {
       try
       {
           MessageDigest md = MessageDigest.getInstance("MD5");
           //Add password byte to digest
           md.update(plaintText.getBytes());
           //Get the hash's bytes
           byte[] bytes = md.digest();

           StringBuilder sb = new StringBuilder();
           for(int i = 0; i < bytes.length; ++ i)
           {
               sb.append(Integer.toString((bytes[i] & 0xff) + 0x100,16).substring(1));
           }
           return sb.toString(); //teh generated password
       }
       catch (NoSuchAlgorithmException e)
       {
           e.printStackTrace();
       }

       return null;

    }

}
