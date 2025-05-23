package com.ChatSphere.Backend.Dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record FollowFollowingRequest(
        @NotBlank(message = "Email is required") @Email(message = "Follower email needs to be valid if provided") String followerEmail,
        @NotBlank(message = "Email is required") @Email(message = "Following email needs to be valid if provided") String followingEmail) {
}
