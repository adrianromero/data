//     Data Access is a Java library to store data
//     Copyright (C) 2016-2017 Adrián Romero Corchado.
//
//     This file is part of Data Access
//
//     Licensed under the Apache License, Version 2.0 (the "License");
//     you may not use this file except in compliance with the License.
//     You may obtain a copy of the License at
//     
//         http://www.apache.org/licenses/LICENSE-2.0
//     
//     Unless required by applicable law or agreed to in writing, software
//     distributed under the License is distributed on an "AS IS" BASIS,
//     WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
//     See the License for the specific language governing permissions and
//     limitations under the License.

package com.adr.data.security;

import java.nio.charset.StandardCharsets;
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
            byte[] hashedBytes = digest.digest(input.getBytes(StandardCharsets.UTF_8));
            return "password:" + Base64.getEncoder().encodeToString(salt) + ":" + Base64.getEncoder().encodeToString(hashedBytes);
        } catch (NoSuchAlgorithmException ex) {
            throw new RuntimeException(ex);
        }
    }
    
    public static boolean validatePassword(String password, String hashsalt) {
        
        if ((password == null || password.isEmpty())) {
            return hashsalt == null || hashsalt.isEmpty(); // empty password. 
        } else if (hashsalt == null || hashsalt.isEmpty()) {
            return false;
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
