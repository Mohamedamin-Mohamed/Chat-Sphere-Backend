package com.ChatSphere.Backend.Dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@RequiredArgsConstructor
@Getter
@Setter
public class EmbeddingDto {
    private String question;
    private String answer;
    private String timestamp;
}
