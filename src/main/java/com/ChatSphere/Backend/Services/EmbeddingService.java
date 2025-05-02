package com.ChatSphere.Backend.Services;

import com.ChatSphere.Backend.Dto.EmbeddingDto;
import com.ChatSphere.Backend.Dto.EmbeddingRequestDto;
import com.ChatSphere.Backend.Exceptions.EmbeddingNotCreated;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
@RequiredArgsConstructor
public class EmbeddingService {
    @Value("${openAI.apiKey}")
    String apiKey;

    @Value("${openAI.embeddingURL}")
    String embeddingUrl;

    @Value("${openAI.embeddingModel}")
    String embeddingModel;

    private final OpenSearchService openSearchService;

    public HttpResponse<String> createEmbedding(String input) {
        try {
            HttpClient httpClient = HttpClient.newHttpClient();

            Map<String, Object> requestBody = new HashMap<>();
            requestBody.put("model", embeddingModel);
            requestBody.put("input", input);

            ObjectMapper objectMapper = new ObjectMapper();
            String jsonBody = objectMapper.writeValueAsString(requestBody);

            HttpRequest headers = HttpRequest.newBuilder().
                    uri(URI.create(embeddingUrl)).
                    headers("Content-Type", "application/json").
                    header("Authorization", "Bearer " + apiKey).
                    POST(HttpRequest.BodyPublishers.ofString(jsonBody)).build();
            HttpResponse<String> httpResponse = httpClient.send(headers, HttpResponse.BodyHandlers.ofString());

            if (httpResponse.statusCode() != 200) {
                throw new EmbeddingNotCreated("Error occurred when creating embedding");
            }
            return httpResponse;
        } catch (Exception exp) {
            log.error("Something went wrong", exp);
            throw new EmbeddingNotCreated("Error occurred when creating embedding");
        }
    }

    public List<EmbeddingDto> searchEmbedding(EmbeddingRequestDto embeddingRequestDto) {
        HttpResponse<String> httpResponse = createEmbedding(embeddingRequestDto.question());
        float[] embedding = retrieveEmbedding(httpResponse);
        return openSearchService.searchEmbedding(embedding);
    }

    public void storeEmbeddings(EmbeddingDto embeddingDto) throws IOException {
        HttpResponse<String> httpResponse = createEmbedding(embeddingDto.getQuestion());
        float[] embeddings = retrieveEmbedding(httpResponse);

        openSearchService.indexEmbeddings(embeddingDto, embeddings);
    }

    public float[] retrieveEmbedding(HttpResponse<String> httpResponse) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            JsonNode rootNode = objectMapper.readTree(httpResponse.body());
            JsonNode node = rootNode.path("data").get(0).path("embedding");

            float[] embeddings = new float[node.size()];
            int i = 0;

            for (JsonNode jsonNode : node) {
                embeddings[i++] = jsonNode.floatValue();
            }
            return embeddings;
        } catch (Exception exp) {
            log.error("Couldn't retrieve embedding from response body: ", exp);
            return null;
        }
    }
}
