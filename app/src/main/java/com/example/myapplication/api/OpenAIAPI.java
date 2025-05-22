package com.example.myapplication.api;

import com.example.myapplication.model.ChatRequest;
import com.example.myapplication.model.Message;
import com.example.myapplication.model.OpenAIResponse;
import com.example.myapplication.model.ThreadResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface OpenAIAPI {
    @POST("/api/openai/chat")
    Call<OpenAIResponse> chat(@Body ChatRequest request);

    @GET("/api/openai/chat/history")
    Call<List<Message>> getHistory(@Query("threadId") String threadId, @Query("userId") String userId);

    @GET("/api/openai/thread/{userId}")
    Call<ThreadResponse> getThreadId(@Path("userId") String userId);
}
