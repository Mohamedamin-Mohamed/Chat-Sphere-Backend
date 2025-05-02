package com.ChatSphere.Backend.Dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateEmbeddingDto {
    private String question;
    private String answer;
    private String timestamp;
}
