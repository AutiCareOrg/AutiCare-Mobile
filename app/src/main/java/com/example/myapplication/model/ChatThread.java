package com.example.myapplication.model;

import com.google.gson.annotations.SerializedName;

public class ChatThread {

    private String id;

    @SerializedName("userId")
    private Long userId;

    public ChatThread() {}

    public ChatThread(String id, Long userId) {
        this.id = id;
        this.userId = userId;
    }

    public String getId() {
        return id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
}
