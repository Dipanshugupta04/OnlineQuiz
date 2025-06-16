package com.OnlineQuiz.OnlineQuiz.Exception; // <-- use your actual package here

public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String message) {
        super(message);
    }
}
