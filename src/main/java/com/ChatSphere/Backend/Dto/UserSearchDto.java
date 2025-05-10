package com.ChatSphere.Backend.Dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserSearchDto {
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
}
