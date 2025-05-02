package com.ChatSphere.Backend.Dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
public class SendCodeResultDto {
    private boolean success;
    private String message;
}
