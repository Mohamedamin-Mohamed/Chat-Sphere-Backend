package com.ChatSphere.Backend.Services;

import com.ChatSphere.Backend.Dto.MessageRequestDto;
import com.ChatSphere.Backend.Dto.MessageResponseErrorDto;
import com.ChatSphere.Backend.Mappers.ModelMapper;
import com.ChatSphere.Backend.Model.Message;
import com.ChatSphere.Backend.Repositories.MessageRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class MessageService {
    private final MessageRepository messageRepository;
    private final ModelMapper modelMapper;

    @Transactional
    public Object saveMessage(MessageRequestDto messageDto) {
        try {
            Message message = modelMapper.map(messageDto);
            messageRepository.save(message);
            return modelMapper.map(message);
        } catch (Exception exp) {
            log.error("Something went wrong", exp);
            return new MessageResponseErrorDto(false, String.format("Couldn't save %s message with sender of email %s", messageDto.getSender(), messageDto.getEmail()));
        }
    }

    public List<Message> loadMessages(String email) {
        return messageRepository.findMessageByEmail(email);
    }
}
