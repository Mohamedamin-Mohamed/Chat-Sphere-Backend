package com.ChatSphere.Backend.Dto;

import lombok.Data;

@Data
public class PasswordResetDto {
    private String email;
    private String password;
}
