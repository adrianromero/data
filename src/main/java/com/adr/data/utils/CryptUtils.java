//    HelloCore is a client application platform
//    Copyright (C) 2015 Adrián Romero Corchado.
//    All Rights reserved.

package com.adr.data.utils;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

/**
 *
 * @author adrian
 */
public class CryptUtils {
    
    public static byte[] generateSalt() {
        SecureRandom random = new SecureRandom();
        byte salt[] = new byte[32];
        random.nextBytes(salt);     
        return salt;
    }
    
    public static String hashsaltPassword(String input, byte[] salt) {
        try {
            // generate hash
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            digest.reset();
            digest.update(salt);
            byte[] hashedBytes = digest.digest(input.getBytes("UTF-8"));

            return "password:" + Base64.getEncoder().encodeToString(salt) + ":" + Base64.getEncoder().encodeToString(hashedBytes);
        } catch (NoSuchAlgorithmException | UnsupportedEncodingException ex) {
            throw new RuntimeException(ex);
        }
    }
    
    public static boolean validatePassword(String password, String hashsalt) {
        
        if ((password == null || password.isEmpty()) && (hashsalt == null || hashsalt.isEmpty())) {
            return true; // empty password. 
        } else {
            String [] splitted = hashsalt.split(":");
            
            if (splitted.length != 3 || !"password".equals(splitted[0])) {
                return false;
            }
            
            byte[] salt = Base64.getDecoder().decode(splitted[1]);
                
            return hashsaltPassword(password, salt).equals(hashsalt);
        }
    }
}
