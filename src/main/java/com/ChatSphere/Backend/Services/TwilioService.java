package com.ChatSphere.Backend.Services;

import com.ChatSphere.Backend.Config.TwilioConfig;
import com.ChatSphere.Backend.Dto.TwilioSendPinDto;
import com.ChatSphere.Backend.Dto.TwilioVerifyPinDto;
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
    private final TwilioConfig twilioConfig;
    @Value("${twilio.VERIFICATION_SID}")
    String serviceId;

    public boolean sendCode(TwilioSendPinDto sendPinDto) {
        try {
            twilioConfig.connect();
            Verification verification = Verification.creator(serviceId, sendPinDto.getPhoneNumber(), "sms").create();
            return verification.getStatus().equals("pending");
        } catch (Exception exp) {
            log.info("Something went wrong", exp);
        }
        return false;
    }

    public boolean verifyCode(TwilioVerifyPinDto verifyPinDto) {
        try {
            twilioConfig.connect();
            VerificationCheck verificationCheck = VerificationCheck.
                    creator(serviceId, verifyPinDto.getCode()).
                    setTo(verifyPinDto.getPhoneNumber()).create();
            return verificationCheck.getStatus().equals("approved");
        } catch (Exception exp) {
            log.error("Something went wrong", exp);
        }
        return false;
    }

}
