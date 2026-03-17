package com.hector.orders.util;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class GeneratedPass {
    public static void main(String[] arg){
        var encoder = new BCryptPasswordEncoder();
        String password = "1234";
        String encryptedPassword = encoder.encode(password);
        System.out.println("Encrypted password: " + encryptedPassword);
    }
}
