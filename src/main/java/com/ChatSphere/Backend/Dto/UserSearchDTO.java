package com.ChatSphere.Backend.Dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class UserSearchDTO {
    private String email;
    private String name;
    private String bio;
    private String picture;
    private String joinedDate;
    private boolean isOnline;
    private int followerSize;
    private int followingSize;
    private boolean isFollowedByRequester;
    private boolean isFollowingRequester;
    private int mutualFriendsSize;
    private List<UserSearchDTO> topThreeMutualFriends;

    private UserSearchDTO(Builder builder) {
        this.email = builder.email;
        this.name = builder.name;
        this.bio = builder.bio;
        this.picture = builder.picture;
        this.joinedDate = builder.joinedDate;
        this.isOnline = builder.isOnline;
        this.followerSize = builder.followerSize;
        this.followingSize = builder.followingSize;
        this.isFollowedByRequester = builder.isFollowedByRequester;
        this.isFollowingRequester = builder.isFollowingRequester;
        this.mutualFriendsSize = builder.mutualFriendsSize;
        this.topThreeMutualFriends = builder.topThreeMutualFriends;
    }

    public static class Builder {
        private final String email;
        private final String name;
        private String bio;
        private String picture;
        private String joinedDate;
        private boolean isOnline;
        private int followerSize;
        private int followingSize;
        private boolean isFollowedByRequester;
        private boolean isFollowingRequester;
        private int mutualFriendsSize;
        private List<UserSearchDTO> topThreeMutualFriends;

        public Builder(String name, String email) {
            this.name = name;
            this.email = email;
        }

        public Builder bio(String bio) {
            this.bio = bio;
            return this;
        }

        public Builder picture(String picture) {
            this.picture = picture;
            return this;
        }

        public Builder joinedDate(String joinedDate) {
            this.joinedDate = joinedDate;
            return this;
        }

        public Builder isOnline(boolean isOnline) {
            this.isOnline = isOnline;
            return this;
        }

        public Builder followerSize(int followerSize) {
            this.followerSize = followerSize;
            return this;
        }

        public Builder followingSize(int followingSize) {
            this.followingSize = followingSize;
            return this;
        }

        public Builder isFollowedByRequester(boolean isFollowedByRequester) {
            this.isFollowedByRequester = isFollowedByRequester;
            return this;
        }

        public Builder isFollowingRequester(boolean isFollowingRequester) {
            this.isFollowingRequester = isFollowingRequester;
            return this;
        }

        public Builder mutualFriendsSize(int mutualFriendsSize) {
            this.mutualFriendsSize = mutualFriendsSize;
            return this;
        }

        public Builder topThreeMutualFriends(List<UserSearchDTO> topThreeMutualFriends) {
            this.topThreeMutualFriends = topThreeMutualFriends;
            return this;
        }

        public UserSearchDTO build() {
            return new UserSearchDTO(this);
        }
    }

}
