package com.ChatSphere.Backend.Services;

import com.ChatSphere.Backend.Dto.EmbeddingDto;
import com.ChatSphere.Backend.Dto.VectorStoreDto;
import com.ChatSphere.Backend.Exceptions.DocumentNotIndexed;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.opensearch.client.opensearch.OpenSearchClient;
import org.opensearch.client.opensearch._types.Result;
import org.opensearch.client.opensearch.core.IndexRequest;
import org.opensearch.client.opensearch.core.IndexResponse;
import org.opensearch.client.opensearch.core.SearchRequest;
import org.opensearch.client.opensearch.core.SearchResponse;
import org.opensearch.client.opensearch.core.search.Hit;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class OpenSearchService {
    @Value("${OpenSearch.indexName}")
    String indexName;

    @Value("${OpenSearch.searchField}")
    String searchField;

    private final OpenSearchClient client;

    public void indexEmbeddings(EmbeddingDto embeddingDto, float[] embedding) {
        try {
            VectorStoreDto vectorStoreDto = getVectorStoreEquivalent(embeddingDto, embedding);
            IndexRequest<VectorStoreDto> indexRequest = new IndexRequest.Builder<VectorStoreDto>().
                    index(indexName).
                    document(vectorStoreDto).build();

            IndexResponse indexResponse = client.index(indexRequest);
            if (indexResponse.result() != Result.Created && indexResponse.result() != Result.Updated) {
                throw new DocumentNotIndexed("An error occurred while indexing document");
            }
        } catch (Exception exp) {
            log.error("Error occurred while indexing documents: ", exp);
            throw new DocumentNotIndexed("An error occurred while indexing document");
        }
    }

    public VectorStoreDto getVectorStoreEquivalent(EmbeddingDto embeddingDto, float[] embeddingsVector) {
        VectorStoreDto vectorStoreDto = new VectorStoreDto();
        vectorStoreDto.setEmbeddingDto(embeddingDto);
        vectorStoreDto.setEmbeddings(embeddingsVector);
        return vectorStoreDto;
    }

    public List<EmbeddingDto> searchEmbedding(float[] embeddings) {
        log.info("Searching from vector store!!!");

        try {
            SearchRequest searchRequest = new SearchRequest.Builder()
                    .index(indexName)
                    .size(10)
                    .query(q -> q.knn(knn -> knn
                            .field(searchField)
                            .vector(embeddings)
                            .k(10)
                    ))
                    .build();

            SearchResponse<VectorStoreDto> searchResponse = client.search(searchRequest, VectorStoreDto.class);

            List<EmbeddingDto> embeddingDtoList = new ArrayList<>();

            for (Hit<VectorStoreDto> vectorStoreDtoHit : searchResponse.hits().hits()) {
                VectorStoreDto vectorStoreDto = vectorStoreDtoHit.source();
                if (vectorStoreDto != null) {
                    EmbeddingDto embeddingDto = vectorStoreDto.getEmbeddingDto();
                    embeddingDtoList.add(embeddingDto);
                }
            }
            return embeddingDtoList;
        } catch (Exception exp) {
            log.error("Search request failed: ", exp);
            return null;
        }

    }

    public void searchResponse() throws IOException {
        SearchRequest searchRequest = new SearchRequest.Builder().index(indexName).query(q -> q.matchAll(m -> m)).build();

        SearchResponse<VectorStoreDto> searchResponse = client.search(searchRequest, VectorStoreDto.class);

        for (Hit<VectorStoreDto> hit : searchResponse.hits().hits()) {
            VectorStoreDto document = hit.source();
            if (document != null) {
                EmbeddingDto embeddingDto = document.getEmbeddingDto();
                System.out.println("Document ID: " + hit.id());
                System.out.println("Question: " + embeddingDto.getQuestion());
                System.out.println("Answer: " + embeddingDto.getAnswer());
                System.out.println("Embedding: " + Arrays.toString(document.getEmbeddings()));
                System.out.println("Timestamp: " + embeddingDto.getTimestamp());
            }
        }

    }
}
