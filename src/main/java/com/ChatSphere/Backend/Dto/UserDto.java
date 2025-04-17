package com.ChatSphere.Backend.Dto;

import lombok.Data;

@Data
public class UserDto {
    private String email;
    private String name;
    private String oauthProvider;
    private String picture;
}
