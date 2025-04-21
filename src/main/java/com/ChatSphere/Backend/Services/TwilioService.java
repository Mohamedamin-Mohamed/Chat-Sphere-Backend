package com.ChatSphere.Backend.Services;

import com.ChatSphere.Backend.Config.TwilioConfig;
import com.ChatSphere.Backend.Dto.TwilioVerifyDto;
import com.twilio.rest.verify.v2.service.Verification;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TwilioVerify {
    private TwilioConfig twilioConfig;
    @Value("${twilio.VERIFICATION_SID}")
    String serviceId;

    public boolean sendCode(TwilioVerifyDto twilioVerifyDto) {
        twilioConfig.connect();
        Verification verification = Verification.creator(serviceId, twilioVerifyDto.getPhoneNumber(), "sms").create();
        return verification.getValid();
    }
}
