package com.example.emailspamapi.controller;

import com.example.emailspamapi.dto.MessageRequest;
import com.example.emailspamapi.dto.MessageResponse;
import com.example.emailspamapi.model.SmsMessage;
import com.example.emailspamapi.service.SpamDetectionService;
import com.example.emailspamapi.repository.SmsMessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/email")
public class SpamController {

    @Autowired
    private SpamDetectionService spamDetectionService;

    @Autowired
    private SmsMessageRepository smsMessageRepository;

    @PostMapping("/check-spam")
    public ResponseEntity<?> checkEmailSpam(@RequestBody MessageRequest request) {
        System.out.println("=== CONTROLLER DEBUG ===");
        System.out.println("Request object: " + request);

        String text = null;
        if (request != null) {
            try {
                java.lang.reflect.Method getTextMethod = request.getClass().getMethod("getText");
                text = (String) getTextMethod.invoke(request);
                System.out.println("Got text via getText(): " + text);
            } catch (Exception e1) {
                try {
                    java.lang.reflect.Method getMessageMethod = request.getClass().getMethod("getMessage");
                    text = (String) getMessageMethod.invoke(request);
                    System.out.println("Got text via getMessage(): " + text);
                } catch (Exception e2) {
                    System.out.println("Cannot get text from request");
                }
            }
        }

        System.out.println("Final text: " + text);
        System.out.println("=== END DEBUG ===");

        if (text == null || text.trim().isEmpty()) {
            return ResponseEntity.badRequest().body(
                    Map.of(
                            "error", "Text is required",
                            "receivedRequest", request != null ? request.toString() : "null"
                    )
            );
        }

        MessageResponse response = spamDetectionService.classifyMessage(text);
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

    @GetMapping("/health")
    public ResponseEntity<Map<String, Object>> health() {
        Map<String, Object> healthStatus = new HashMap<>();
        healthStatus.put("status", "UP");
        healthStatus.put("service", "email-spam-api");
        healthStatus.put("timestamp", LocalDateTime.now().toString());
        healthStatus.put("messageCount", smsMessageRepository.count());
        return ResponseEntity.ok(healthStatus);
    }

    @GetMapping("/")
    public String home() {
        return """
            <!DOCTYPE html>
            <html>
            <head>
                <title>Email Spam API</title>
                <style>
                    body { font-family: Arial; padding: 40px; }
                    h1 { color: #333; }
                    .link { display: block; margin: 10px 0; padding: 10px; background: #007bff; color: white; text-decoration: none; border-radius: 5px; }
                </style>
            </head>
            <body>
                <h1>📧 Email Spam Detection API</h1>
                <p>emails</p>
                <a href="/swagger-ui.html" class="link">📖 </a>
                <a href="/api/email/health" class="link">🩺 </a>
                <a href="/api/auth/health" class="link">🔐 </a>
            </body>
            </html>
            """;
    }
}