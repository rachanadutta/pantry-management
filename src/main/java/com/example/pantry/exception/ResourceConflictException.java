package com.example.pantry.exception;

public class ResourceConflictException extends RuntimeException {

    public ResourceConflictException(String message) {
        super(message);
    }
}