package com.ChatSphere.Backend.Controller;

import com.ChatSphere.Backend.Dto.MessageDto;
import com.ChatSphere.Backend.Services.ConversationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequestMapping("api/chat")
@RequiredArgsConstructor
public class Conversation {
    private final ConversationService conversationService;

    @PostMapping("/completions")
    public ResponseEntity<Object> askChatGpt(@RequestBody MessageDto[] messageDtos) {
        log.info("Performing chat completion");
        Object object = conversationService.chatCompletions(messageDtos);
        return ResponseEntity.ok(object);
    }
}
