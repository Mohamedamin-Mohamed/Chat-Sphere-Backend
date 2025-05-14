package com.ChatSphere.Backend.Mappers;

import com.ChatSphere.Backend.Dto.SearchRequest;
import com.ChatSphere.Backend.Dto.UserSearchDto;
import com.ChatSphere.Backend.Exceptions.EmailNotFoundException;
import com.ChatSphere.Backend.Model.Follow;
import com.ChatSphere.Backend.Model.User;
import com.ChatSphere.Backend.Services.FollowService;
import com.ChatSphere.Backend.Services.UserRelationshipService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserSearchDTOMapper {
    private final UserRelationshipService userRelationshipService;
    private final FollowService followService;
    private final ModelMapper modelMapper;

    public UserSearchDto map(User user, SearchRequest searchRequest) {
        boolean requesterFollowsUser = followsUser(searchRequest.requesterEmail(), user);
        boolean requesterIsFollowed = constructFollowingUser(searchRequest, user);
        return modelMapper.map(user, requesterFollowsUser, requesterIsFollowed);
    }

    public boolean constructFollowingUser(SearchRequest searchRequest, User user) {
        User followingUser = userRelationshipService.findByEmail(searchRequest.requesterEmail()).orElseThrow(() -> new EmailNotFoundException("Following email not found"));
        return followsUser(user.getEmail(), followingUser);
    }

    public boolean followsUser(String requesterEmail, User targetUser) {
        User requester = userRelationshipService.findByEmail(requesterEmail).orElseThrow(() -> new EmailNotFoundException("Requester email not found"));
        List<Follow> followList = followService.findByFollower(requester);

        return followList.stream().anyMatch(f -> f.getFollowing().getId() == targetUser.getId());
    }
}
