package com.example.myapplication.model;

import com.google.gson.annotations.SerializedName;

public class OpenAIResponse {

    @SerializedName("id_conversa")
    private Long idConversa;

    private String thread;
    private String prompt;
    private String response;

    @SerializedName("runId")
    private String runId;

    public Long getIdConversa() {
        return idConversa;
    }

    public void setIdConversa(Long idConversa) {
        this.idConversa = idConversa;
    }

    public String getThread() {
        return thread;
    }

    public void setThread(String thread) {
        this.thread = thread;
    }

    public String getPrompt() {
        return prompt;
    }

    public void setPrompt(String prompt) {
        this.prompt = prompt;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public String getRunId() {
        return runId;
    }

    public void setRunId(String runId) {
        this.runId = runId;
    }
}
