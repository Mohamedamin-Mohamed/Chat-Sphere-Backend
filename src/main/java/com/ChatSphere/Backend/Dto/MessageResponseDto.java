package com.ChatSphere.Backend.Dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class MessageResponseDto {
    private String messageId;
    private String email;
    private String sender;
    private String message;
    private String timestamp;

    private MessageResponseDto(Builder builder) {
        this.messageId = builder.messageId;
        this.email = builder.email;
        this.sender = builder.sender;
        this.message = builder.message;
        this.timestamp = builder.timestamp;
    }


    public static class Builder {
        private String messageId;
        private String email;
        private String sender;
        private String message;
        private String timestamp;


        public Builder messageId(String messageId) {
            this.messageId = messageId;
            return this;
        }

        public Builder email(String email) {
            this.email = email;
            return this;
        }

        public Builder sender(String sender) {
            this.sender = sender;
            return this;
        }

        public Builder message(String message) {
            this.message = message;
            return this;
        }

        public Builder timestamp(String timestamp) {
            this.timestamp = timestamp;
            return this;
        }

        public MessageResponseDto build() {
            return new MessageResponseDto(this);
        }
    }
}
