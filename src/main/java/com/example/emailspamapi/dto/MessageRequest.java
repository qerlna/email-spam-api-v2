package com.example.emailspamapi.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import jakarta.validation.constraints.NotBlank;

public class MessageRequest {

    @NotBlank(message = "Text cannot be empty")
    @JsonAlias({"text", "message", "content"}) // Принимает любое из этих полей
    private String text;

    // Геттеры и сеттеры
    public String getText() { return text; }
    public void setText(String text) { this.text = text; }
}