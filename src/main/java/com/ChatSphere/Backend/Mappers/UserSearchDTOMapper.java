package com.ChatSphere.Backend.Mappers;

import com.ChatSphere.Backend.Dto.UserDto;
import com.ChatSphere.Backend.Dto.UserSearchDTO;
import com.ChatSphere.Backend.Dto.UserSearchResponse;
import com.ChatSphere.Backend.Exceptions.EmailNotFoundException;
import com.ChatSphere.Backend.Model.Follow;
import com.ChatSphere.Backend.Model.User;
import com.ChatSphere.Backend.Services.FollowService;
import com.ChatSphere.Backend.Services.UserRelationshipService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.BiFunction;
import java.util.function.Predicate;

@Service
@RequiredArgsConstructor
public class UserSearchDTOMapper implements BiFunction<User, String, UserSearchDTO> {
    private final UserRelationshipService userRelationshipService;
    private final FollowService followService;
    private final ModelMapper modelMapper;

    @Override
    public UserSearchDTO apply(User targetUser, String requesterEmail) {
        boolean requesterFollowsUser = followsUser(requesterEmail, targetUser.getId());
        boolean requesterIsFollowed = isUserFollowingRequester(requesterEmail, targetUser);
        List<User> mutualFriendsList = getMutualFriends(targetUser, requesterEmail);
        List<UserDto> topThreeMutualFriends = mutualFriendsList.stream().map(modelMapper::map).limit(3).toList();

        List<UserSearchDTO> topThreeMutualFriendsSearchDTOList = topThreeMutualFriends.stream().map(userDto -> convertUserToUserSearchDTO(userDto, requesterEmail)).toList();

        UserSearchResponse userSearchResponse = new UserSearchResponse.
                Builder().
                user(targetUser).
                mutualFriendsList(mutualFriendsList).
                topThreeMutualFriendsSearchDTOList(topThreeMutualFriendsSearchDTOList).
                requesterFollowsUser(requesterFollowsUser).
                requesterIsFollowed(requesterIsFollowed).
                build();
        return modelMapper.map(userSearchResponse);
    }

    private boolean isUserFollowingRequester(String targetUser, User user) {
        User followingUser = userRelationshipService.findByEmail(targetUser).orElseThrow(() -> new EmailNotFoundException("Following email not found"));
        return followsUser(user.getEmail(), followingUser.getId());
    }

    private boolean followsUser(String requesterEmail, Long targetUserId) {
        User requester = userRelationshipService.findByEmail(requesterEmail).orElseThrow(() -> new EmailNotFoundException("Requester email not found"));
        List<Follow> followList = followService.findByFollower(requester);
        Predicate<Follow> isUserFollowed = follow -> follow.getFollowing().getId() == targetUserId;

        return followList.stream().anyMatch(isUserFollowed);
    }

    private List<User> getMutualFriends(User targetUser, String requesterEmail) {
        User requesterUser = userRelationshipService.findByEmail(requesterEmail).orElseThrow(() -> new EmailNotFoundException("Requester user email not found"));
        List<User> usersFollowedByRequester = followService.findByFollower(requesterUser).stream().map(Follow::getFollowing).toList();
        List<User> userFollowedByTargetUser = followService.findByFollower(targetUser).stream().map(Follow::getFollowing).toList();
        Set<User> intersection = new HashSet<>(usersFollowedByRequester);
        intersection.retainAll(userFollowedByTargetUser);

        return new ArrayList<>(intersection);
    }

    private UserSearchDTO convertUserToUserSearchDTO(UserDto userDto, String requesterEmail) {
        User targetUser = userRelationshipService.findByEmail(userDto.getEmail())
                .orElseThrow(() -> new EmailNotFoundException("Target user email not found"));

        boolean requesterFollowsUser = followsUser(requesterEmail, targetUser.getId());
        boolean requesterIsFollowed = isUserFollowingRequester(requesterEmail, targetUser);
        List<User> mutualFriendsList = getMutualFriends(targetUser, requesterEmail);

        List<UserDto> topThreeMutualFriends = mutualFriendsList.stream()
                .map(modelMapper::map)
                .limit(3)
                .toList();

        List<UserSearchDTO> topThreeMutualFriendsSearchDTOList = topThreeMutualFriends.stream()
                .map(dto -> new UserSearchDTO.Builder(dto.getName(), dto.getEmail())
                        .bio(dto.getBio())
                        .picture(dto.getPicture())
                        .joinedDate(dto.getCreatedAt())
                        .isOnline(false)
                        .followerSize(targetUser.getFollowers().size())
                        .followingSize(targetUser.getFollowings().size())
                        .isFollowedByRequester(requesterFollowsUser)
                        .isFollowingRequester(requesterIsFollowed)
                        .mutualFriendsSize(0)
                        .topThreeMutualFriends(List.of())
                        .build())
                .toList();

        return new UserSearchDTO.Builder(targetUser.getName(), targetUser.getEmail())
                .bio(targetUser.getBio())
                .picture(targetUser.getPicture())
                .joinedDate(formatedDate(targetUser.getCreatedAt()))
                .isOnline(false)
                .followerSize(targetUser.getFollowers().size())
                .followingSize(targetUser.getFollowings().size())
                .isFollowedByRequester(requesterFollowsUser)
                .isFollowingRequester(requesterIsFollowed)
                .mutualFriendsSize(mutualFriendsList.size())
                .topThreeMutualFriends(topThreeMutualFriendsSearchDTOList)
                .build();
    }


    private String formatedDate(Instant instant) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("MMMM yyy").withZone(ZoneId.systemDefault());
        return dateTimeFormatter.format(instant);
    }
}
