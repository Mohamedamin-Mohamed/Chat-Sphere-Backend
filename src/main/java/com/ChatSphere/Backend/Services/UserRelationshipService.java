package com.ChatSphere.Backend.Services;

import com.ChatSphere.Backend.Model.User;
import com.ChatSphere.Backend.Repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserRelationshipService {
    private final UserRepository userRepository;

    public Optional<User> findByEmail(String email) {
        return Optional.ofNullable(userRepository.findByEmail(email));
    }

}
