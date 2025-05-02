package com.ChatSphere.Backend.Dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {
    private String email;
    private String createdAt;
    private String name;
    private String oauthProvider;
    private String picture;
    private String bio;
    private String phoneNumber;
}
