package com.example.emailspamapi.dto;

public class MessageResponse {
    private String classification;
    private double confidence;
    private String details;
    private String message;

    // Конструкторы
    public MessageResponse() {}

    public MessageResponse(String classification, double confidence, String details) {
        this.classification = classification;
        this.confidence = confidence;
        this.details = details;
        this.message = "Message analyzed successfully";
    }

    // Геттеры и сеттеры
    public String getClassification() { return classification; }
    public void setClassification(String classification) { this.classification = classification; }

    public double getConfidence() { return confidence; }
    public void setConfidence(double confidence) { this.confidence = confidence; }

    public String getDetails() { return details; }
    public void setDetails(String details) { this.details = details; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
}