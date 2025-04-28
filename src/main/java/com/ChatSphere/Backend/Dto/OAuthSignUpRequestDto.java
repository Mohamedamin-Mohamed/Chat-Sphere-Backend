package com.ChatSphere.Backend.Dto;

import lombok.Data;

@Data
public class OAuthSignUpRequestDto {
    private String email;
    private String name;
    private String oauthProvider;
    private String oauthId;
    private boolean emailVerified;
    private String picture;
    private String authorizationCode;
    private String identityToken;
}
