package com.ChatSphere.Backend.Dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserDto {
    private String email;
    private String createdAt;
    private String name;
    private String oauthProvider;
    private String picture;
    private String bio;
    private String phoneNumber;

    private UserDto(Builder builder) {
        this.email = builder.email;
        this.createdAt = builder.createdAt;
        this.name = builder.name;
        this.oauthProvider = builder.oauthProvider;
        this.picture = builder.picture;
        this.bio = builder.bio;
        this.phoneNumber = builder.phoneNumber;
    }

    public static class Builder {
        private String email;
        private String createdAt;
        private String name;
        private String oauthProvider;
        private String picture;
        private String bio;
        private String phoneNumber;

        public Builder email(String email) {
            this.email = email;
            return this;
        }

        public Builder createdAt(String createdAt) {
            this.createdAt = createdAt;
            return this;
        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder oauthProvider(String oauthProvider) {
            this.oauthProvider = oauthProvider;
            return this;
        }

        public Builder picture(String picture) {
            this.picture = picture;
            return this;
        }

        public Builder bio(String bio) {
            this.bio = bio;
            return this;
        }

        public Builder phoneNumber(String phoneNumber) {
            this.phoneNumber = phoneNumber;
            return this;
        }

        public UserDto build() {
            return new UserDto(this);
        }
    }
}
