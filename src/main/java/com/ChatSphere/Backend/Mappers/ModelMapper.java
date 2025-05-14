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

        return new UserDto.
                Builder().
                email(user.getEmail()).
                createdAt(formatedDate(user.getCreatedAt())).
                name(user.getName()).
                oauthProvider(user.getOauthProvider()).
                picture(user.getPicture()).
                bio(user.getBio()).
                phoneNumber(user.getPhoneNumber()).
                build();
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

        return new MessageResponseDto.
                Builder().messageId(String.valueOf(message.getMessageID())).
                email(message.getEmail()).
                sender(message.getSender()).
                message(message.getMessage()).
                timestamp(message.getTimestamp())
                .build();
    }

    public Follow map(User follower, User following) {
        Follow follow = new Follow();
        follow.setFollower(follower);
        follow.setFollowing(following);
        return follow;
    }

    public UserSearchDto map(User user, boolean isFollowedByRequester, boolean isFollowingRequester) {

        return new UserSearchDto.
                Builder(user.getName(), user.getEmail()).
                bio(user.getBio()).
                picture(user.getPicture()).
                joinedDate(formatedDate(user.getCreatedAt())).
                isOnline(false).
                followerSize(user.getFollowers().size()).
                followingSize(user.getFollowings().size()).
                isFollowedByRequester(isFollowedByRequester).
                isFollowingRequester(isFollowingRequester).
                build();
    }

    public UserStatsDto map(List<Follow> followList, List<Follow> followingList) {
        UserStatsDto userStatsDto = new UserStatsDto();
        userStatsDto.setFollowers(followingList.size());
        userStatsDto.setFollowings(followList.size());
        return userStatsDto;
    }
}
