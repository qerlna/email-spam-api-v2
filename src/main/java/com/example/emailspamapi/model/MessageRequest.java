package com.example.emailspamapi.model;

import jakarta.validation.constraints.NotBlank;

public class MessageRequest {

    @NotBlank(message = "Message cannot be blank")
    private String message;

    // Default constructor
    public MessageRequest() {}

    public MessageRequest(String message) {
        this.message = message;
    }

    // Getters and Setters
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
}