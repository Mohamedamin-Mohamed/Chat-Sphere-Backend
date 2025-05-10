package com.ChatSphere.Backend.Services;

import com.ChatSphere.Backend.Dto.SearchRequest;
import com.ChatSphere.Backend.Exceptions.EmailNotFoundException;
import com.ChatSphere.Backend.Model.Follow;
import com.ChatSphere.Backend.Model.User;
import com.ChatSphere.Backend.Repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserFollowHelperService {
    private final UserRepository userRepository;

    public Optional<User> findByEmail(String email) {
        return Optional.ofNullable(userRepository.findByEmail(email));
    }

    private boolean followRelationShip(String followerEmail, User following) {
        return followsUser(followerEmail, following);
    }

    public boolean constructFollowingUser(SearchRequest searchRequest, User user) {
        //here we are generating the following user and make
        User following = findByEmail(searchRequest.requesterEmail()).orElseThrow(() -> new EmailNotFoundException("Following email not found"));
        return followRelationShip(user.getEmail(), following);
    }

    public boolean followsUser(String requesterEmail, User targetUser) {
        User requester = findByEmail(requesterEmail).orElseThrow(() -> new EmailNotFoundException("Requester email not found"));

        List<Follow> followList = followService.findByFollower(requester);

        return followList.stream().anyMatch(f -> f.getFollowing().getId() == targetUser.getId());
    }
}
