package com.example.myapplication.model;

public class Message {
    private Long id;           // pode deixar nulo no Android se quiser
    private String role;       // "user" ou "assistant"
    private String content;
    private String threadId;   // se quiser usar

    public Message() {}

    public Message(String content, String role) {
        this.content = content;
        this.role = role;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getThreadId() {
        return threadId;
    }

    public void setThreadId(String threadId) {
        this.threadId = threadId;
    }
}
