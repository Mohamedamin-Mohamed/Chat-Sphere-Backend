package com.ChatSphere.Backend.Services;

import com.ChatSphere.Backend.Dto.MessageDto;
import com.ChatSphere.Backend.Exceptions.ChatCompletionsNotCreated;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
public class ConversationService {
    @Value("${openAI.chatCompletionsURL}")
    String url;

    @Value("${openAI.chatCompletionsModel}")
    String model;

    @Value("${openAI.chatCompletionsMaxTokens}")
    int maxTokens;

    @Value("${openAI.apiKey}")
    String apiKey;

    public Object chatCompletions(MessageDto[] messageDtos) {
        log.info("Sending request to LLM to  generate chat response");
        try {
            HttpResponse<String> httpResponse = httpResponse(messageDtos);
            if (httpResponse.statusCode() != 200) {
                throw new ChatCompletionsNotCreated("Error occurred when completing the chat");
            }
            return httpResponse.body();
        } catch (Exception exp) {
            log.error("Something went wrong", exp);
            throw new ChatCompletionsNotCreated("Error occurred when completing the chat");
        }
    }

    private HttpResponse<String> httpResponse(MessageDto[] messageDtos) throws IOException, InterruptedException {
        HttpClient httpClient = HttpClient.newHttpClient();

        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("model", model);
        requestBody.put("messages", messageDtos);
        requestBody.put("max_tokens", maxTokens);

        ObjectMapper objectMapper = new ObjectMapper();
        String jsonBody = objectMapper.writeValueAsString(requestBody);

        HttpRequest httpRequest = HttpRequest.newBuilder().
                uri(URI.create(url)).
                header("Content-type", "application/json").
                header("Authorization", "Bearer " + apiKey).
                POST(HttpRequest.BodyPublishers.ofString(jsonBody)).build();

        return httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
    }
}
