package com.ChatSphere.Backend.Controller;

import com.ChatSphere.Backend.Dto.MessageRequestDto;
import com.ChatSphere.Backend.Model.Message;
import com.ChatSphere.Backend.Services.MessageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("auth/message")
@RequiredArgsConstructor
@Slf4j
public class ChatMessageController {
    private final MessageService messageService;

    @PostMapping("/create")
    private ResponseEntity<Object> saveMessage(@RequestBody MessageRequestDto messageDto) {
        log.info("Saving new message for sender  {} {}", messageDto.getSender(), messageDto.getMessage().length());
        Object response = messageService.saveMessage(messageDto);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/messages")
    public ResponseEntity<List<Message>> loadMessages(@RequestParam String email) {
        log.info("Loading messages for {}", email);
        List<Message> messages = messageService.loadMessages(email);
        if (messages != null) {
            return new ResponseEntity<>(messages, HttpStatus.OK);
        }
        return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
