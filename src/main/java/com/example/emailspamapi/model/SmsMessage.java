package com.example.emailspamapi.model;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import java.time.LocalDateTime;

@Entity
@Table(name = "sms_messages")
public class SmsMessage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "classification", nullable = false)
    private String classification; // "spam" или "ham"

    @Column(name = "message", length = 1000)
    private String message;

    @Column(name = "details", length = 2000)
    private String details; // Добавляем поле для деталей

    @CreationTimestamp
    private LocalDateTime createdAt;

    // Constructors
    public SmsMessage() {}

    public SmsMessage(String classification, String message) {
        this.classification = classification;
        this.message = message;
    }

    // Конструктор с тремя параметрами для SpamController
    public SmsMessage(String message, String classification, String details) {
        this.message = message;
        this.classification = classification;
        this.details = details;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getClassification() { return classification; }
    public void setClassification(String classification) {
        this.classification = classification;
    }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public String getDetails() { return details; }
    public void setDetails(String details) { this.details = details; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    @Override
    public String toString() {
        return "SmsMessage{" +
                "id=" + id +
                ", classification='" + classification + '\'' +
                ", message='" + message + '\'' +
                ", details='" + details + '\'' +
                ", createdAt=" + createdAt +
                '}';
    }
}