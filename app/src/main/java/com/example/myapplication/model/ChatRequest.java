package com.example.myapplication.model;

public class ChatRequest {
    private String prompt;
    private String threadId;
    private String userId;

    public ChatRequest(String prompt, String threadId, String userId) {
        this.prompt = prompt;
        this.threadId = threadId;
        this.userId = userId;
    }
}
