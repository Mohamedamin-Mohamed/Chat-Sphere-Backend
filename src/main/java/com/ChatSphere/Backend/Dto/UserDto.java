package com.ChatSphere.Backend.Dto;

import lombok.Data;

@Data
public class UserDto {
    private String email;
    private String createdAt;
    private String name;
    private String oauthProvider;
    private String picture;
    private String bio;
    private String phoneNumber;
}
