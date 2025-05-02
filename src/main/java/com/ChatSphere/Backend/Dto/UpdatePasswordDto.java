package com.ChatSphere.Backend.Dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class UpdatePasswordDto {
    private String email;
    private String currentPassword;
    private String newPassword;
}
