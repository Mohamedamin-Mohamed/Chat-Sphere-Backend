package com.ChatSphere.Backend.Dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@AllArgsConstructor
public class ApiResponseDto {
    private boolean success;
    private String message;
    private Object data;

}
