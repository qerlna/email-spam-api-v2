package com.example.emailspamapi.service;

import com.example.emailspamapi.dto.MessageResponse;
import com.example.emailspamapi.model.SmsMessage;
import com.example.emailspamapi.repository.SmsMessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

@Service
public class SpamDetectionService {

    @Autowired
    private SmsMessageRepository messageRepository;

    private final List<String> spamKeywords = Arrays.asList(
            "free", "win", "prize", "click", "now", "offer", "limited",
            "winner", "congratulations", "congrats", "urgent", "cash",
            "gift", "card", "claim", "selected", "reward", "guaranteed",
            "risk-free", "bonus", "discount", "sale", "deal", "won",
            "winning", "lucky", "exclusive", "instant", "miracle",
            "amazing", "incredible", "secret", "trick", "method"
    );

    public MessageResponse classifyMessage(String text) {
        if (text == null || text.trim().isEmpty()) {
            return new MessageResponse("ham", 0.0, "Empty message");
        }

        SpamAnalysisResult analysis = analyzeText(text);

        System.out.println("=== SPAM ANALYSIS ===");
        System.out.println("Text: " + text);
        System.out.println("Found spam words: " + analysis.foundSpamWords);
        System.out.println("Spam word count: " + analysis.spamWordCount);
        System.out.println("Exclamation count: " + analysis.exclamationCount);
        System.out.println("Has money symbols: " + analysis.hasMoneySymbols);
        System.out.println("Uppercase ratio: " + analysis.uppercaseRatio);
        System.out.println("Total score: " + analysis.totalScore);
        System.out.println("Is spam: " + analysis.isSpam);
        System.out.println("=====================");

        SmsMessage message = new SmsMessage();
        message.setMessage(text);
        message.setClassification(analysis.isSpam ? "spam" : "ham");
        message.setDetails(analysis.details);
        message.setCreatedAt(LocalDateTime.now());
        messageRepository.save(message);

        return new MessageResponse(
                analysis.isSpam ? "spam" : "ham",
                analysis.confidence,
                analysis.details
        );
    }

    private SpamAnalysisResult analyzeText(String text) {
        String lowerText = text.toLowerCase();
        StringBuilder details = new StringBuilder();


        List<String> foundSpamWords = spamKeywords.stream()
                .filter(keyword -> containsWord(lowerText, keyword))
                .toList();

        int spamWordCount = foundSpamWords.size();
        if (!foundSpamWords.isEmpty()) {
            details.append("Spam words: ").append(String.join(", ", foundSpamWords));
        }

        long exclamationCount = text.chars().filter(ch -> ch == '!').count();
        if (exclamationCount > 0) {
            if (details.length() > 0) details.append(" | ");
            details.append("! count: ").append(exclamationCount);
        }

        boolean hasMoneySymbols = text.contains("$") ||
                text.contains("€") ||
                text.contains("£") ||
                lowerText.contains("cash") ||
                lowerText.contains("money") ||
                lowerText.contains("dollar");
        if (hasMoneySymbols) {
            if (details.length() > 0) details.append(" | ");
            details.append("Has money symbols");
        }

        long uppercaseCount = text.chars().filter(Character::isUpperCase).count();
        double uppercaseRatio = text.length() > 0 ? (double) uppercaseCount / text.length() : 0;

        if (uppercaseRatio > 0.3) {
            if (details.length() > 0) details.append(" | ");
            details.append(String.format("UPPERCASE: %.0f%%", uppercaseRatio * 100));
        }

        boolean hasUrl = containsUrl(text);
        if (hasUrl) {
            if (details.length() > 0) details.append(" | ");
            details.append("Contains URL");
        }

        boolean hasPhone = Pattern.compile("\\d{10,}").matcher(text).find();
        if (hasPhone) {
            if (details.length() > 0) details.append(" | ");
            details.append("Has phone number");
        }

        double totalScore = 0;

        totalScore += spamWordCount * 3;

        if (exclamationCount > 2) totalScore += 2;
        else if (exclamationCount > 0) totalScore += 1;

        if (hasMoneySymbols) totalScore += 3;

        if (uppercaseRatio > 0.5) totalScore += 2;

        if (hasUrl || hasPhone) totalScore += 4;

        boolean isSpam = totalScore >= 5;

        double confidence = Math.min(0.95, totalScore / 10.0);
        if (isSpam && confidence < 0.6) confidence = 0.6;
        if (!isSpam && confidence < 0.7) confidence = 0.7;

        return new SpamAnalysisResult(
                foundSpamWords,
                spamWordCount,
                exclamationCount,
                hasMoneySymbols,
                uppercaseRatio,
                hasUrl,
                hasPhone,
                totalScore,
                isSpam,
                confidence,
                details.toString()
        );
    }

    private boolean containsWord(String text, String word) {
        return Pattern.compile("\\b" + Pattern.quote(word) + "\\b", Pattern.CASE_INSENSITIVE)
                .matcher(text)
                .find();
    }

    private boolean containsUrl(String text) {
        return text.matches(".*https?://.*") ||
                text.matches(".*www\\..*\\..*") ||
                text.toLowerCase().contains(".com") ||
                text.toLowerCase().contains(".net") ||
                text.toLowerCase().contains(".org");
    }

    private static class SpamAnalysisResult {
        List<String> foundSpamWords;
        int spamWordCount;
        long exclamationCount;
        boolean hasMoneySymbols;
        double uppercaseRatio;
        boolean hasUrl;
        boolean hasPhone;
        double totalScore;
        boolean isSpam;
        double confidence;
        String details;

        public SpamAnalysisResult(List<String> foundSpamWords, int spamWordCount,
                                  long exclamationCount, boolean hasMoneySymbols,
                                  double uppercaseRatio, boolean hasUrl, boolean hasPhone,
                                  double totalScore, boolean isSpam, double confidence,
                                  String details) {
            this.foundSpamWords = foundSpamWords;
            this.spamWordCount = spamWordCount;
            this.exclamationCount = exclamationCount;
            this.hasMoneySymbols = hasMoneySymbols;
            this.uppercaseRatio = uppercaseRatio;
            this.hasUrl = hasUrl;
            this.hasPhone = hasPhone;
            this.totalScore = totalScore;
            this.isSpam = isSpam;
            this.confidence = confidence;
            this.details = details;
        }
    }
}