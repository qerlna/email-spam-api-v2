package com.example.emailspamapi.model;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class MessageResponse {
    private String classification;
    private double confidence;
    private String message;
    private String error;

    // Constructors
    public MessageResponse() {}

    public MessageResponse(String classification, double confidence, String message) {
        this.classification = classification;
        this.confidence = confidence;
        this.message = message;
    }

    public MessageResponse(String error) {
        this.error = error;
    }

    // Getters and Setters
    public String getClassification() { return classification; }
    public void setClassification(String classification) { this.classification = classification; }

    public double getConfidence() { return confidence; }
    public void setConfidence(double confidence) { this.confidence = confidence; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public String getError() { return error; }
    public void setError(String error) { this.error = error; }
}