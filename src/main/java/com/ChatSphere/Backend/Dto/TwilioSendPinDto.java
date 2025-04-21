package com.ChatSphere.Backend.Dto;

import lombok.Data;

@Data
public class TwilioSendPinDto {
    private String email;
    private String phoneNumber;
}
