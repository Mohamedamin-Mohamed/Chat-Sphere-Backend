package com.ChatSphere.Backend.Controller;

import com.ChatSphere.Backend.Dto.EmbeddingDto;
import com.ChatSphere.Backend.Dto.EmbeddingRequestDto;
import com.ChatSphere.Backend.Services.EmbeddingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("api/embeddings")
@Slf4j
@RequiredArgsConstructor
public class EmbeddingController {

    private final EmbeddingService embeddingService;

    @PostMapping
    public ResponseEntity<?> createEmbedding(@RequestBody EmbeddingDto embeddingDto) throws IOException {
        log.info("Creating embedding for question {}", embeddingDto.getQuestion());
        embeddingService.storeEmbeddings(embeddingDto);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/search")
    public ResponseEntity<Object> searchEmbeddings(@RequestBody EmbeddingRequestDto embeddingRequestDto) {
        log.info("Searching embedding for input {}", embeddingRequestDto.question());
        List<EmbeddingDto> embeddingDtoList = embeddingService.searchEmbedding(embeddingRequestDto);
        log.info("Size of returned list is {}", embeddingDtoList.size());
        return new ResponseEntity<>(embeddingDtoList, HttpStatus.OK);
    }

}
