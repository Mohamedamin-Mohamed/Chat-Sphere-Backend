package com.ChatSphere.Backend.Controllers;

import com.ChatSphere.Backend.Dto.ApiResponseDto;
import com.ChatSphere.Backend.Dto.TwilioSendPinDto;
import com.ChatSphere.Backend.Dto.TwilioVerifyPinDto;
import com.ChatSphere.Backend.Services.TwilioService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/twilio/code")
@RequiredArgsConstructor
@Slf4j
public class TwilioVerification {
    private final TwilioService twilioService;

    @PostMapping("/send")
    public ResponseEntity<ApiResponseDto> codeSending(@RequestBody TwilioSendPinDto sendPinDto) {
        log.info("Sending code for phone number: {}", sendPinDto.getPhoneNumber());

        boolean codeSent = twilioService.sendCode(sendPinDto);

        if (codeSent) {
            ApiResponseDto successResponse = new ApiResponseDto(true, "Code sent successfully", null);
            return new ResponseEntity<>(successResponse, HttpStatus.CREATED);
        }

        ApiResponseDto errorResponse = new ApiResponseDto(false, "Something went wrong", null);
        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @PostMapping("/verify")
    public ResponseEntity<ApiResponseDto> codeVerification(@RequestBody TwilioVerifyPinDto verifyPinDto) {
        log.info("Verifying code for phone number: {}", verifyPinDto.getPhoneNumber());
        boolean codeVerified = twilioService.verifyCode(verifyPinDto);

        if (codeVerified) {
            ApiResponseDto successResponse = new ApiResponseDto(true, "Code is valid", null);
            return new ResponseEntity<>(successResponse, HttpStatus.OK);


        }
        ApiResponseDto errorResponse = new ApiResponseDto(false, "Code not valid", null);
        return new ResponseEntity<>(errorResponse, HttpStatus.UNAUTHORIZED);
    }
}
