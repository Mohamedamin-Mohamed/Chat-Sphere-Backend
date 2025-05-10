package com.ChatSphere.Backend.Services;

import com.ChatSphere.Backend.Model.Follower;
import com.ChatSphere.Backend.Model.User;
import com.ChatSphere.Backend.Repositories.FollowersRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class FollowerService {
    private final FollowersRepository followersRepository;


    public boolean operateFollow(User follower, User following) {
        return false;
    }
}
