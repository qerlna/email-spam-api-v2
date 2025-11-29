package com.example.emailspamapi.dto;

public class StatisticsDto {
    private Long totalMessages;
    private Long spamCount;
    private Long hamCount;
    private Double spamPercentage;

    // Конструкторы, геттеры и сеттеры
    public StatisticsDto() {}

    public StatisticsDto(Long totalMessages, Long spamCount, Long hamCount) {
        this.totalMessages = totalMessages;
        this.spamCount = spamCount;
        this.hamCount = hamCount;
        this.spamPercentage = totalMessages > 0 ? (double) spamCount / totalMessages * 100 : 0.0;
    }

    // Геттеры и сеттеры
    public Long getTotalMessages() { return totalMessages; }
    public void setTotalMessages(Long totalMessages) { this.totalMessages = totalMessages; }
    public Long getSpamCount() { return spamCount; }
    public void setSpamCount(Long spamCount) { this.spamCount = spamCount; }
    public Long getHamCount() { return hamCount; }
    public void setHamCount(Long hamCount) { this.hamCount = hamCount; }
    public Double getSpamPercentage() { return spamPercentage; }
    public void setSpamPercentage(Double spamPercentage) { this.spamPercentage = spamPercentage; }
}