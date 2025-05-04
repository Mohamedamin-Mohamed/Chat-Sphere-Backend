package com.ChatSphere.Backend.Exceptions;

public class ChatCompletionsNotCreated extends RuntimeException {
    public ChatCompletionsNotCreated(String message) {
        super(message);
    }
}
