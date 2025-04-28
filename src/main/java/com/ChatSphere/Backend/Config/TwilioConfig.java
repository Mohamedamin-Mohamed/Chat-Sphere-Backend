package com.ChatSphere.Backend.Config;

import com.twilio.Twilio;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TwilioConfig {
    @Value("${twilio.accountSID}")
    String ACCOUNT_SID;

    @Value("${twilio.authToken}")
    String AUTH_TOKEN;

    public void connect() {
        Twilio.init(ACCOUNT_SID, AUTH_TOKEN);
    }
}
