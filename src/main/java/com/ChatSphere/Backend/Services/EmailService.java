package com.ChatSphere.Backend.Services;

import com.ChatSphere.Backend.Config.MailjetConfig;
import com.ChatSphere.Backend.Dto.MailjetDto;
import com.mailjet.client.MailjetClient;
import com.mailjet.client.MailjetRequest;
import com.mailjet.client.MailjetResponse;
import com.mailjet.client.resource.Emailv31;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailService {
    private final MailjetConfig mailjetConfig;

    @Value("${mailjet.senderEmail}")
    private String senderEmail;

    @Value("${mailjet.senderName}")
    private String senderName;

    @Value("${fileNames.verificationCode}")
    private String  fileName;

    private final CodeGeneratorService codeGeneratorService;

    public boolean sendVerificationEmail(String recipientEmail, String code) {
        log.info("Sending verification code {} to {}", code, recipientEmail);
        if (code.isEmpty()) {
            throw new IllegalArgumentException("Verification code need to be specified");
        }
        String template = getEmailTemplate(fileName);
        String[] templateLines = template.split("\\r?\\n");

        String subject = templateLines[0].replace("Subject: ", "");
        String bodyContent = String.join("\n", Arrays.copyOfRange(templateLines, 1, templateLines.length));

        bodyContent = bodyContent.replace("{verification_code}", code);

        MailjetDto mailjetDto = new MailjetDto();
        mailjetDto.setRecipientEmail(recipientEmail);
        mailjetDto.setSubject(subject);
        mailjetDto.setBodyContent(bodyContent);

        return setMailjetConfig(mailjetDto);
    }

    private boolean setMailjetConfig(MailjetDto mailjetDto) {
        try {
            // convert to HTML with proper formatting
            String htmlContent = mailjetDto.getBodyContent().replace("\n", "<br>");

            MailjetClient client = mailjetConfig.connect();
            MailjetRequest request = new MailjetRequest(Emailv31.resource)
                    .property(Emailv31.MESSAGES, new JSONArray()
                            .put(new JSONObject()
                                    .put(Emailv31.Message.FROM, new JSONObject()
                                            .put("Email", senderEmail)
                                            .put("Name", senderName))
                                    .put(Emailv31.Message.TO, new JSONArray()
                                            .put(new JSONObject()
                                                    .put("Email", mailjetDto.getRecipientEmail())))
                                    .put(Emailv31.Message.SUBJECT, mailjetDto.getSubject())
                                    .put(Emailv31.Message.TEXTPART, mailjetDto.getBodyContent())
                                    .put(Emailv31.Message.HTMLPART, htmlContent)));

            MailjetResponse response = client.post(request);

            if (response.getStatus() == 200) {
                log.info("Email sent successfully: {}", response.getData());
                return true;
            } else {
                log.error("Error sending email: {}", response.getData());
                return false;
            }
        } catch (Exception e) {
            log.error("Error reading content: {}", e.getMessage());
            throw new RuntimeException("Error sending email", e);
        }
    }

    public String getEmailTemplate(String fileName) {
        // access the template file from the resources directory
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream(String.format("EmailTemplates/%s", fileName));

        if (inputStream == null) {
            throw new IllegalArgumentException("Template file not found");
        }

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
            StringBuilder templateBuilder = new StringBuilder();
            String line;

            while ((line = reader.readLine()) != null) {
                templateBuilder.append(line).append("\n");
            }

            return templateBuilder.toString();
        } catch (IOException e) {
            throw new RuntimeException("Error reading email template", e);
        }
    }
}
