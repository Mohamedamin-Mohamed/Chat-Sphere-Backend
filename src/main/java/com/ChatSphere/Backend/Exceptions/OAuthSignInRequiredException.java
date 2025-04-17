package com.ChatSphere.Backend.Exceptions;

public class OAuthSignInRequiredException extends RuntimeException {
    public OAuthSignInRequiredException(String message) {
        super(message);
    }
}
