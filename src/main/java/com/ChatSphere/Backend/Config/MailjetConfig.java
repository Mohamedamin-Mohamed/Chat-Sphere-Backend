package com.ChatSphere.Backend.Config;

import com.mailjet.client.ClientOptions;
import com.mailjet.client.MailjetClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MailjetConfig {
    @Value("${mailjet.apiKey}")
    String apiKey;

    @Value("${mailjet.apiSecretKey}")
    String apiSecretKey;

    public MailjetClient connect() {
        ClientOptions clientOptions = ClientOptions.builder().apiKey(apiKey).apiSecretKey(apiSecretKey).build();
        return new MailjetClient(clientOptions);
    }
}
