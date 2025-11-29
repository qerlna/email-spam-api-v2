package com.example.emailspamapi.repository;

import com.example.emailspamapi.model.SmsMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SmsMessageRepository extends JpaRepository<SmsMessage, Long> {

    List<SmsMessage> findByClassification(String classification);

    List<SmsMessage> findByMessageContainingIgnoreCase(String keyword);

    long countByClassification(String classification);

    // ИЗМЕНИ ЭТУ СТРОКУ - добавь "By" после "All"
    @Query("SELECT s FROM SmsMessage s ORDER BY s.createdAt DESC")
    List<SmsMessage> findAllByOrderByCreatedAtDesc();
}