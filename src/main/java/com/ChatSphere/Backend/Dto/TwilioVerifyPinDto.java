package com.ChatSphere.Backend.Dto;

import lombok.Data;

@Data
public class TwilioVerifyPinDto {
    private String email;
    private String phoneNumber;
    private String pin;
}
