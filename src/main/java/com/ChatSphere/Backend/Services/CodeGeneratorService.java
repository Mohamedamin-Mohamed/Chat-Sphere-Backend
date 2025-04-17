package com.ChatSphere.Backend.Services;

import org.springframework.stereotype.Service;

import java.security.SecureRandom;

@Service
public class CodeGenerator {
    public String generateCode() {
        SecureRandom secureRandom = new SecureRandom();
        int code = 10000 + secureRandom.nextInt(900000);
        return String.valueOf(code);
    }
}
