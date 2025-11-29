package com.example.emailspamapi.service;

import com.example.emailspamapi.dto.StatisticsDto;
import com.example.emailspamapi.model.MessageResponse;
import com.example.emailspamapi.model.SmsMessage;
import com.example.emailspamapi.repository.SmsMessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
public class SpamDetectionService {

    @Autowired
    private SmsMessageRepository messageRepository;

    private final List<String> spamKeywords = Arrays.asList(
            "free", "win", "winner", "cash", "prize", "ticket", "claim",
            "urgent", "money", "congratulations", "selected", "award",
            "lottery", "bonus", "discount", "offer", "limited", "time",
            "click", "call now", "apply now", "winner", "won", "congrats"
    );

    public MessageResponse classifyMessage(String message) {
        if (message == null || message.trim().isEmpty()) {
            return new MessageResponse("Message cannot be empty");
        }

        String lowerMessage = message.toLowerCase();
        int spamScore = 0;

        // Check for spam keywords
        for (String keyword : spamKeywords) {
            if (lowerMessage.contains(keyword)) {
                spamScore++;
            }
        }

        // Check for phone numbers (long sequences of digits)
        if (message.matches(".*\\d{8,}.*")) {
            spamScore += 2;
        }

        // Check for URLs
        if (lowerMessage.contains("http") || lowerMessage.contains("www") ||
                lowerMessage.contains(".com") || lowerMessage.contains(".ru")) {
            spamScore += 2;
        }

        // Check for excessive capitalization
        long upperCaseCount = message.chars().filter(Character::isUpperCase).count();
        if (upperCaseCount > message.length() * 0.3) {
            spamScore += 1;
        }

        // Check for special characters
        long specialCharCount = message.chars().filter(ch -> !Character.isLetterOrDigit(ch) && !Character.isWhitespace(ch)).count();
        if (specialCharCount > message.length() * 0.1) {
            spamScore += 1;
        }

        boolean isSpam = spamScore >= 2;
        double confidence = Math.min(0.95, (spamScore * 0.15));

        // Auto-save classified message
        SmsMessage sms = new SmsMessage(isSpam ? "spam" : "ham", message);
        messageRepository.save(sms);

        return new MessageResponse(
                isSpam ? "spam" : "ham",
                confidence,
                message
        );
    }

    // Новые методы для SpamController
    public boolean isSpam(String message) {
        MessageResponse response = classifyMessage(message);
        return "spam".equals(response.getClassification());
    }

    public String getSpamProbability(String message) {
        MessageResponse response = classifyMessage(message);
        return String.format("%.2f%%", response.getConfidence() * 100);
    }

    // CRUD Operations
    public SmsMessage createMessage(SmsMessage message) {
        return messageRepository.save(message);
    }

    public List<SmsMessage> getAllMessages() {
        return messageRepository.findAll(Sort.by(Sort.Direction.DESC, "createdAt"));
    }

    public Optional<SmsMessage> getMessageById(Long id) {
        return messageRepository.findById(id);
    }

    public List<SmsMessage> getMessagesByType(String type) {
        return messageRepository.findByClassification(type.toLowerCase());
    }

    public SmsMessage updateMessage(Long id, SmsMessage messageDetails) {
        Optional<SmsMessage> optionalMessage = messageRepository.findById(id);
        if (optionalMessage.isPresent()) {
            SmsMessage message = optionalMessage.get();
            message.setClassification(messageDetails.getClassification());
            message.setMessage(messageDetails.getMessage());
            return messageRepository.save(message);
        }
        throw new RuntimeException("Message not found with id: " + id);
    }

    public boolean deleteMessage(Long id) {
        if (messageRepository.existsById(id)) {
            messageRepository.deleteById(id);
            return true;
        }
        return false;
    }

    // Search operations
    public List<SmsMessage> searchMessages(String keyword) {
        return messageRepository.findByMessageContainingIgnoreCase(keyword);
    }

    // Statistics
    public StatisticsDto getStatistics() {
        long total = messageRepository.count();
        long spamCount = messageRepository.countByClassification("spam");
        long hamCount = messageRepository.countByClassification("ham");

        return new StatisticsDto(total, spamCount, hamCount);
    }
}