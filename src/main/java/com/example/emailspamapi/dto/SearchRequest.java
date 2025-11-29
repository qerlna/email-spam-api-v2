package com.example.emailspamapi.dto;

public class SearchRequest {
    private String query;

    // Конструкторы, геттеры и сеттеры
    public SearchRequest() {}

    public SearchRequest(String query) {
        this.query = query;
    }

    public String getQuery() { return query; }
    public void setQuery(String query) { this.query = query; }
}