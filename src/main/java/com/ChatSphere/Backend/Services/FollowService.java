package com.ChatSphere.Backend.Services;

import com.ChatSphere.Backend.Dto.FollowFollowingRequest;
import com.ChatSphere.Backend.Exceptions.EmailNotFoundException;
import com.ChatSphere.Backend.Mappers.ModelMapper;
import com.ChatSphere.Backend.Model.Follow;
import com.ChatSphere.Backend.Model.User;
import com.ChatSphere.Backend.Repositories.FollowRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class FollowService {
    private final ModelMapper modelMapper;
    private final FollowRepository followRepository;
    private final UserRelationshipService userRelationshipService;

    @Transactional
    public void followUser(FollowFollowingRequest followFollowingRequest) {
        String followerEmail = followFollowingRequest.followerEmail();
        String followingEmail = followFollowingRequest.followingEmail();

        User follower = userRelationshipService.findByEmail(followerEmail).orElseThrow(() -> new EmailNotFoundException("Follower email not found"));
        User following = userRelationshipService.findByEmail(followingEmail).orElseThrow(() -> new EmailNotFoundException("Following email not found"));

        Follow follow = modelMapper.map(follower, following);
        followRepository.save(follow);
    }

    @Transactional
    public void unfollowUser(FollowFollowingRequest followFollowingRequest) {
        String followerEmail = followFollowingRequest.followerEmail();
        User follower = userRelationshipService.findByEmail(followerEmail).orElseThrow(() -> new EmailNotFoundException("Follower Email not found"));
        deleteFollow(follower);
    }

    @Transactional
    public void deleteFollow(User follower) {
        List<Follow> follows = followRepository.findFollowsByFollower(follower);
        followRepository.deleteAll(follows);
    }

    public List<Follow> findByFollower(User requester) {
        return followRepository.findByFollower(requester);
    }

}
