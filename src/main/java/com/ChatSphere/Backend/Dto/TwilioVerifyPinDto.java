package com.ChatSphere.Backend.Dto;

public record TwilioVerifyPinDto(String email, String phoneNumber, String pin) {
}
