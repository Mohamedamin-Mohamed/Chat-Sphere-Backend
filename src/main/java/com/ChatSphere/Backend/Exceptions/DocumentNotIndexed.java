package com.ChatSphere.Backend.Exceptions;

public class DocumentNotIndexed extends RuntimeException {
    public DocumentNotIndexed(String message) {
        super(message);
    }
}
