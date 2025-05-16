package com.ChatSphere.Backend.Dto;

import com.ChatSphere.Backend.Model.User;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class UserSearchResponse {
    private User user;
    private boolean requesterFollowsUser;
    private boolean requesterIsFollowed;
    private List<User> mutualFriendsList;
    private List<UserSearchDTO> topThreeMutualFriendsSearchDTOList;

    private UserSearchResponse(Builder builder) {
        this.user = builder.user;
        this.mutualFriendsList = builder.mutualFriendsList;
        this.topThreeMutualFriendsSearchDTOList = builder.topThreeMutualFriendsSearchDTOList;
        this.requesterFollowsUser = builder.requesterFollowsUser;
        this.requesterIsFollowed = builder.requesterIsFollowed;
    }

    public static class Builder {
        private User user;
        private boolean requesterFollowsUser;
        private boolean requesterIsFollowed;
        private List<User> mutualFriendsList;
        private List<UserSearchDTO> topThreeMutualFriendsSearchDTOList;

        public Builder user(User user) {
            this.user = user;
            return this;
        }

        public Builder requesterFollowsUser(boolean requesterFollowsUser) {
            this.requesterFollowsUser = requesterFollowsUser;
            return this;
        }

        public Builder mutualFriendsList(List<User> mutualFriendsList) {
            this.mutualFriendsList = mutualFriendsList;
            return this;
        }

        public Builder topThreeMutualFriendsSearchDTOList(List<UserSearchDTO> topThreeMutualFriendsSearchDTOList) {
            this.topThreeMutualFriendsSearchDTOList = topThreeMutualFriendsSearchDTOList;
            return this;
        }

        public Builder requesterIsFollowed(boolean requesterIsFollowed) {
            this.requesterIsFollowed = requesterIsFollowed;
            return this;
        }

        public UserSearchResponse build() {
            return new UserSearchResponse(this);
        }
    }
}
