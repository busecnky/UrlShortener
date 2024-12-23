package com.urlshortener.utils;

import org.springframework.stereotype.Component;
import java.security.SecureRandom;

@Component
public class IdGenerator {

    private final SecureRandom RANDOM = new SecureRandom();

    public String generateUniqueId(int length) {
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
            sb.append(CHARACTERS.charAt(RANDOM.nextInt(CHARACTERS.length())));
        }
        return sb.toString();
    }
}