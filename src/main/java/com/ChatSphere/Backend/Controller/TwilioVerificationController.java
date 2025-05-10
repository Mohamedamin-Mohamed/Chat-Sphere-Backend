package com.ChatSphere.Backend.Controller;

import com.ChatSphere.Backend.Dto.ApiResponseDto;
import com.ChatSphere.Backend.Dto.SendCodeResultDto;
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
public class TwilioVerificationController {
    private final TwilioService twilioService;

    @PostMapping("/send")
    public ResponseEntity<ApiResponseDto> codeSending(@RequestBody TwilioSendPinDto sendPinDto) {
        log.info("Sending code for phone number: {}", sendPinDto.phoneNumber());

        SendCodeResultDto result = twilioService.sendCode(sendPinDto);

        if (result.isSuccess()) {
            return ResponseEntity.status(HttpStatus.CREATED).body(new ApiResponseDto(true, result.getMessage(), null));
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponseDto(false, result.getMessage(), null));
    }

    @PostMapping("/verify")
    public ResponseEntity<ApiResponseDto> codeVerification(@RequestBody TwilioVerifyPinDto verifyPinDto) {
        log.info("Verifying pin");

        boolean codeVerified = twilioService.verifyCode(verifyPinDto);

        if (codeVerified) {
            log.info("Verified");
            ApiResponseDto successResponse = new ApiResponseDto(true, "Pin verified", null);
            return new ResponseEntity<>(successResponse, HttpStatus.OK);
        }

        log.warn("Verification failed");
        ApiResponseDto errorResponse = new ApiResponseDto(false, "Invalid pin", null);
        return new ResponseEntity<>(errorResponse, HttpStatus.UNAUTHORIZED);
    }

}
