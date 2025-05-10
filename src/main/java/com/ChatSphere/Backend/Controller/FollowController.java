package com.ChatSphere.Backend.Controller;

import com.ChatSphere.Backend.Dto.FollowFollowingRequest;
import com.ChatSphere.Backend.Services.FollowService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/follows")
@Slf4j
@RequiredArgsConstructor
public class Follow {
    private final FollowService followService;

    @PostMapping("/follow")
    public ResponseEntity.BodyBuilder followUser(@RequestBody FollowFollowingRequest followFollowingRequest) {
        log.info("Received follow request from {} to follow {}.", followFollowingRequest.followerEmail(), followFollowingRequest.followingEmail());
        followService.followUser(followFollowingRequest);
        return ResponseEntity.ok();
    }

    @PostMapping("/unfollow")
    public ResponseEntity.BodyBuilder unfollowUser(@RequestBody FollowFollowingRequest followFollowingRequest) {
        log.info("Received unfollow request from {} to unfollow {}.", followFollowingRequest.followerEmail(), followFollowingRequest.followingEmail());
        followService.unfollowUser(followFollowingRequest);
        return ResponseEntity.ok();
    }
}
