package com.ChatSphere.Backend.Dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
public class OAuthSignUpRequest {
    private String email;
    private String name;
    private String oauthProvider;
    private String oauthId;
}
