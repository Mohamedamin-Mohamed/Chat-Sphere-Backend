package com.ChatSphere.Backend.Mappers;

import com.ChatSphere.Backend.Dto.OAuthSignUpRequestDto;
import com.ChatSphere.Backend.Dto.SignUpRequestDto;
import com.ChatSphere.Backend.Dto.UserDto;
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
}
