package com.ChatSphere.Backend.Services;

import com.ChatSphere.Backend.Config.TwilioConfig;
import com.ChatSphere.Backend.Dto.SendCodeResultDto;
import com.ChatSphere.Backend.Dto.TwilioSendPinDto;
import com.ChatSphere.Backend.Dto.TwilioVerifyPinDto;
import com.ChatSphere.Backend.Dto.UpdateProfileDto;
import com.twilio.rest.lookups.v1.PhoneNumber;
import com.twilio.rest.verify.v2.service.Verification;
import com.twilio.rest.verify.v2.service.VerificationCheck;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class TwilioService {

    private static final String STATUS_PENDING = "pending";
    private static final String STATUS_APPROVED = "approved";

    private final TwilioConfig twilioConfig;
    private final UserService userService;

    @Value("${twilio.VERIFICATION_SID}")
    private String serviceId;

    public SendCodeResultDto sendCode(TwilioSendPinDto sendPinDto) {
        try {
            twilioConfig.connect();

            String phoneNum = sendPinDto.getPhoneNumber();
            PhoneNumber number = PhoneNumber
                    .fetcher(new com.twilio.type.PhoneNumber(phoneNum))
                    .fetch();

            if (number != null) {
                Verification verification = Verification
                        .creator(serviceId, phoneNum, "sms")
                        .create();

                boolean isPending = STATUS_PENDING.equals(verification.getStatus());
                return new SendCodeResultDto(isPending, "Code sent successfully.");
            } else {
                return new SendCodeResultDto(false, "Invalid phone number.");
            }
        } catch (Exception exp) {
            log.error("Failed to send verification code to {}: {}", sendPinDto.getPhoneNumber(), exp.getMessage(), exp);
            return new SendCodeResultDto(false, exp.getMessage());
        }
    }

    public boolean verifyCode(TwilioVerifyPinDto verifyPinDto) {
        try {
            twilioConfig.connect();

            VerificationCheck verificationCheck = VerificationCheck.creator(serviceId, verifyPinDto.getPin())
                    .setTo(verifyPinDto.getPhoneNumber())
                    .create();

            boolean isApproved = STATUS_APPROVED.equals(verificationCheck.getStatus());
            if (isApproved) {
                UpdateProfileDto updateProfileDto = new UpdateProfileDto();
                updateProfileDto.setEmail(verifyPinDto.getEmail());
                updateProfileDto.setPhoneNumber(verifyPinDto.getPhoneNumber());
                userService.updateProfile(updateProfileDto);
                return true;
            }

            log.info("Verification status for {} is {}", verifyPinDto.getPhoneNumber(), verificationCheck.getStatus());
            return false;

        } catch (Exception exp) {
            log.error("Verification failed for {}: {}", verifyPinDto.getPhoneNumber(), exp.getMessage(), exp);
            return false;
        }
    }
}
