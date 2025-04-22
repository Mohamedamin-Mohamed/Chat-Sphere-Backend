package com.ChatSphere.Backend.Dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
@AllArgsConstructor
public class SendCodeResultDto {
    private boolean success;
    private String message;
}
