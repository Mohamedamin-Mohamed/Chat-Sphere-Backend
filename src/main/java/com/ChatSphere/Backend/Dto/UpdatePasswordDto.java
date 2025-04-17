package com.ChatSphere.Backend.Dto;

import lombok.Data;

@Data
public class UpdatePasswordDto {
    private String email;
    private String currentPassword;
    private String newPassword;
}
