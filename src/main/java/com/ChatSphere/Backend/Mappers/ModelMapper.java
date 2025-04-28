package com.ChatSphere.Backend.Mappers;

import com.ChatSphere.Backend.Dto.*;
import com.ChatSphere.Backend.Model.Message;
import com.ChatSphere.Backend.Model.User;
import com.ChatSphere.Backend.Services.PasswordService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

@Service
@RequiredArgsConstructor
public class ModelMapper {
    private final PasswordService passwordService;

    public User map(SignUpRequestDto signUpRequest) {
        String hashedPassword = passwordService.hashPassword(signUpRequest.getPassword());

        User user = new User();
        user.setEmail(signUpRequest.getEmail());
        user.setPassword(hashedPassword);
        user.setName(signUpRequest.getName());
        return user;
    }

    public User map(OAuthSignUpRequestDto oAuthSignUpRequest) {
        User user = new User();
        user.setEmail(oAuthSignUpRequest.getEmail());
        user.setName(oAuthSignUpRequest.getName());
        user.setOauthProvider(oAuthSignUpRequest.getOauthProvider());
        user.setOauthId(oAuthSignUpRequest.getOauthId());
        user.setEmailVerified(oAuthSignUpRequest.isEmailVerified());
        user.setPicture(oAuthSignUpRequest.getPicture());
        user.setAuthorizationCode(oAuthSignUpRequest.getAuthorizationCode());
        return user;
    }

    public UserDto map(User user) {
        UserDto userDto = new UserDto();
        userDto.setEmail(user.getEmail());
        userDto.setName(user.getName());
        userDto.setOauthProvider(user.getOauthProvider());
        userDto.setPicture(user.getPicture());
        userDto.setBio(user.getBio());
        userDto.setPhoneNumber(user.getPhoneNumber());

        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("MMMM yyy").withZone(ZoneId.systemDefault());
        String formattedDate = dateTimeFormatter.format(user.getCreatedAt());
        userDto.setCreatedAt(formattedDate);
        return userDto;
    }

    public Message map(MessageRequestDto messageRequestDto) {
        Message message = new Message();
        message.setEmail(messageRequestDto.getEmail());
        message.setSender(messageRequestDto.getSender());
        message.setMessage(messageRequestDto.getMessage());
        message.setTimestamp(messageRequestDto.getTimestamp());
        return message;
    }

    public MessageResponseDto map(Message message) {
        MessageResponseDto messageResponseDto = new MessageResponseDto();
        messageResponseDto.setMessageId(String.valueOf(message.getMessageID()));
        messageResponseDto.setEmail(message.getEmail());
        messageResponseDto.setSender(message.getSender());
        messageResponseDto.setMessage(message.getMessage());
        messageResponseDto.setTimestamp(message.getTimestamp());
        return messageResponseDto;
    }
}
