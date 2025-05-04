package com.ChatSphere.Backend.Dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateProfileDto {
    @NotBlank(message = "Email is required")
    @Email(message = "Valid email is required")
    private String email;

    @Email(message = "New email must be valid if provided")
    private String newEmail;

    private String name;
    private String bio;
    private String phoneNumber;
}