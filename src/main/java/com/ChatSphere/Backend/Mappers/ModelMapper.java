package com.ChatSphere.Backend.Mappers;

import com.ChatSphere.Backend.Dto.*;
import com.ChatSphere.Backend.Model.Follow;
import com.ChatSphere.Backend.Model.Message;
import com.ChatSphere.Backend.Model.User;
import com.ChatSphere.Backend.Services.PasswordService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;

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

        String formattedDate = formatedDate(user.getCreatedAt());
        userDto.setCreatedAt(formattedDate);
        return userDto;
    }

    public String formatedDate(Instant instant) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("MMMM yyy").withZone(ZoneId.systemDefault());
        return dateTimeFormatter.format(instant);
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

    public Follow map(User follower, User following) {
        Follow follow = new Follow();
        follow.setFollower(follower);
        follow.setFollowing(following);
        return follow;
    }

    public UserSearchDto map(User user, boolean isFollowedByRequester, boolean isFollowingRequester) {
        UserSearchDto userSearchDto = new UserSearchDto();
        userSearchDto.setEmail(user.getEmail());
        userSearchDto.setName(user.getName());
        userSearchDto.setBio(user.getBio());
        userSearchDto.setPicture(user.getPicture());
        userSearchDto.setJoinedDate(formatedDate(user.getCreatedAt()));
        userSearchDto.setOnline(false);
        userSearchDto.setFollowerSize(user.getFollowers().size());
        userSearchDto.setFollowingSize(user.getFollowings().size());
        userSearchDto.setFollowedByRequester(isFollowedByRequester);
        userSearchDto.setFollowingRequester(isFollowingRequester);
        return userSearchDto;
    }

    public UserStatsDto map(List<Follow> followList, List<Follow> followingList) {
        UserStatsDto userStatsDto = new UserStatsDto();
        userStatsDto.setFollowers(followingList.size());
        userStatsDto.setFollowings(followList.size());
        return userStatsDto;
    }
}
