package com.example.emailspamapi.controller;

import com.example.emailspamapi.model.MessageRequest;
import com.example.emailspamapi.model.MessageResponse;
import com.example.emailspamapi.model.SmsMessage;
import com.example.emailspamapi.service.SpamDetectionService;
import com.example.emailspamapi.repository.SmsMessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/email")
public class SpamController {

    @Autowired
    private SpamDetectionService spamDetectionService;

    @Autowired
    private SmsMessageRepository smsMessageRepository;

    @PostMapping("/check-spam")
    public ResponseEntity<MessageResponse> checkEmailSpam(@RequestBody MessageRequest request) {
        // Используем существующий метод classifyMessage, который уже сохраняет сообщение
        MessageResponse response = spamDetectionService.classifyMessage(request.getMessage());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/history")
    public ResponseEntity<List<SmsMessage>> getEmailCheckHistory() {
        List<SmsMessage> messages = smsMessageRepository.findAllByOrderByCreatedAtDesc();
        return ResponseEntity.ok(messages);
    }

    @GetMapping("/history/spam")
    public ResponseEntity<List<SmsMessage>> getSpamEmails() {
        List<SmsMessage> spamMessages = smsMessageRepository.findByClassification("spam");
        return ResponseEntity.ok(spamMessages);
    }

    @GetMapping("/history/ham")
    public ResponseEntity<List<SmsMessage>> getHamEmails() {
        List<SmsMessage> hamMessages = smsMessageRepository.findByClassification("ham");
        return ResponseEntity.ok(hamMessages);
    }
}