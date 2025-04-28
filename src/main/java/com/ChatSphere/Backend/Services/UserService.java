package com.ChatSphere.Backend.Services;

import com.ChatSphere.Backend.Dto.*;
import com.ChatSphere.Backend.Exceptions.EmailAlreadyExistsException;
import com.ChatSphere.Backend.Exceptions.EmailNotFoundException;
import com.ChatSphere.Backend.Exceptions.IncorrectPasswordException;
import com.ChatSphere.Backend.Exceptions.OAuthSignInRequiredException;
import com.ChatSphere.Backend.Mappers.ModelMapper;
import com.ChatSphere.Backend.Model.User;
import com.ChatSphere.Backend.Repositories.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class UserService {
    private final UserRepository userRepository;
    private final PasswordService passwordService;
    private final ModelMapper modelMapper;

    public UserDto signUpWithEmail(SignUpRequestDto signUpRequest) {
        findByEmail(signUpRequest.getEmail()).ifPresent(user -> {
            throw new EmailAlreadyExistsException("Account already exists");
        });

        User user = modelMapper.map(signUpRequest);
        userRepository.save(user);

        return modelMapper.map(user);
    }

    public UserDto signUpWithOauth(OAuthSignUpRequestDto oAuthSignUpRequest) {
        Optional<User> existingUser = findByEmail(oAuthSignUpRequest.getEmail());
        if (existingUser.isPresent()) {
            User user = existingUser.get();
            if (user.getOauthProvider() == null || !user.getOauthProvider().equals(oAuthSignUpRequest.getOauthProvider())) {
                throw new EmailAlreadyExistsException("Email already registered");
            }
            return modelMapper.map(user);
        }

        User user = modelMapper.map(oAuthSignUpRequest);
        userRepository.save(user);

        return modelMapper.map(user);
    }

    public UserDto signInWithEmail(SignInDto signInDto) {
        User user = findByEmail(signInDto.getEmail())
                .orElseThrow(() -> new EmailNotFoundException("Email not found"));

        //if user has already an oauth account don't check for password
        if ((user.getOauthProvider() != null && !user.getOauthProvider().isEmpty()) && !user.getOauthId().isEmpty()) {
            throw new OAuthSignInRequiredException(String.format("Account exists sign in with %s provider", user.getOauthProvider()));
        }

        if (!passwordService.verifyPassword(signInDto.getPassword(), user.getPassword())) {
            throw new IncorrectPasswordException("Incorrect password");
        }
        return modelMapper.map(user);
    }

    public Optional<User> findByEmail(String email) {
        return Optional.ofNullable(userRepository.findByEmail(email));
    }

    public User findByOauthId(String oauthId) {
        return userRepository.findByOauthId(oauthId);
    }

    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    public boolean resetPassword(Object genericObject) {
        User user = null;

        try {
            // Try to convert to appropriate DTO first
            ObjectMapper mapper = new ObjectMapper();
            Map dataMap = mapper.convertValue(genericObject, Map.class);

            Object convertedObject;
            if (dataMap.containsKey("email") && dataMap.containsKey("password")) {
                convertedObject = mapper.convertValue(genericObject, PasswordResetDto.class);
            } else if (dataMap.containsKey("email") && dataMap.containsKey("currentPassword") &&
                    dataMap.containsKey("newPassword")) {
                convertedObject = mapper.convertValue(genericObject, UpdatePasswordDto.class);
            } else {
                return false;
            }

            if (convertedObject instanceof PasswordResetDto passwordResetDto) {
                Optional<User> optionalUser = findByEmail(passwordResetDto.getEmail());
                if (optionalUser.isEmpty()) return false;

                user = optionalUser.get();
                String rawPassword = passwordResetDto.getPassword();
                String hashedPassword = passwordService.hashPassword(rawPassword);

                user.setPassword(hashedPassword);
                userRepository.save(user);
                return true;
            } else if (convertedObject instanceof UpdatePasswordDto updatePasswordDto) {
                Optional<User> optionalUser = findByEmail(updatePasswordDto.getEmail());
                if (optionalUser.isEmpty()) return false;

                user = optionalUser.get();
                boolean isPasswordCorrect = passwordService.verifyPassword(
                        updatePasswordDto.getCurrentPassword(), user.getPassword());
                if (!isPasswordCorrect) {
                    return false;
                }

                String hashedNewPassword = passwordService.hashPassword(updatePasswordDto.getNewPassword());
                user.setPassword(hashedNewPassword);
                userRepository.save(user);
                return true;
            }
        } catch (Exception e) {
            log.error("Error processing password reset/update", e);
        }
        return false;
    }

    public void deleteUserById(String email) {
        Optional<User> user = findByEmail(email);
        user.ifPresent(userRepository::delete);
    }

    public UserDto updateProfile(UpdateProfileDto updateProfileDto) {
        User user = findByEmail(updateProfileDto.getEmail())
                .orElseThrow(() -> new EmailNotFoundException("Email not found"));

        // Check if email is being changed
        String currentEmail = user.getEmail();
        String newEmail = updateProfileDto.getNewEmail();

        if (newEmail != null && !newEmail.isEmpty() && !currentEmail.equals(newEmail)) {
            // Check if the new email is already taken by another user
            findByEmail(newEmail).ifPresent(existingUser -> {
                if (existingUser.getId() != user.getId()) {
                    throw new EmailAlreadyExistsException("Email already in use.");
                }
            });

            user.setEmail(newEmail);
        }

        if (updateProfileDto.getName() != null) {
            user.setName(updateProfileDto.getName());
        }

        if (updateProfileDto.getBio() != null) {
            user.setBio(updateProfileDto.getBio());
        }

        if (updateProfileDto.getProfileImage() != null) {
            //in here implement s3 file upload logic and store the public url
            user.setPicture(updateProfileDto.getProfileImage());
        }

        if (updateProfileDto.getPhoneNumber() != null) {
            user.setPhoneNumber(updateProfileDto.getPhoneNumber());
        }

        userRepository.save(user);

        return modelMapper.map(user);
    }
}
