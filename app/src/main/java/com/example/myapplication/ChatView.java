package com.example.myapplication;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.api.OpenAIAPI;
import com.example.myapplication.model.ChatRequest;
import com.example.myapplication.model.Message;
import com.example.myapplication.model.OpenAIResponse;
import com.example.myapplication.model.ThreadResponse;
import com.example.myapplication.network.RetrofitClient;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ChatView extends AppCompatActivity {

    private RecyclerView recyclerViewMessages;
    private EditText messageInput;
    private MessagesAdapter adapter;
    private List<Message> messages;
    private OpenAIAPI openAIAPI;
    private String userId;
    private String threadId = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_view);

        recyclerViewMessages = findViewById(R.id.recyclerViewMessages);
        messageInput = findViewById(R.id.messageInput);

        SharedPreferences prefs = getSharedPreferences("user_prefs", MODE_PRIVATE);
        int userIdInt = prefs.getInt("user_id", -1);
        if (userIdInt == -1) {
            Intent intent = new Intent(ChatView.this, LoginActivity.class);
            startActivity(intent);
            finish();
            return;
        }
        userId = String.valueOf(userIdInt);

        ImageView logoutIcon = findViewById(R.id.logoutIcon);
        logoutIcon.setOnClickListener(v -> {

            prefs.edit().remove("userId").apply();

            Intent intent = new Intent(ChatView.this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        });

        messages = new ArrayList<>();
        adapter = new MessagesAdapter(messages);

        recyclerViewMessages.setAdapter(adapter);
        recyclerViewMessages.setLayoutManager(new LinearLayoutManager(this));

        // Retrofit
        openAIAPI = RetrofitClient.getInstance().create(OpenAIAPI.class);

        fetchOrCreateThreadId();

        messageInput.setOnEditorActionListener((v, actionId, event) -> {
            if(actionId == EditorInfo.IME_ACTION_SEND || actionId == EditorInfo.IME_ACTION_DONE) {
                String text = messageInput.getText().toString().trim();
                if(!text.isEmpty()) {
                    addUserMessage(text);
                    messageInput.setText("");
                }
                return true;
            }
            return false;
        });
    }

    private void addUserMessage(String text) {
        messages.add(new Message(text, "user"));
        adapter.notifyItemInserted(messages.size() - 1);
        recyclerViewMessages.scrollToPosition(messages.size() - 1);

        callChatAPI(text);
    }

    private void fetchHistory() {
        openAIAPI.getHistory(threadId, userId).enqueue(new Callback<List<Message>>() {
            @Override
            public void onResponse(Call<List<Message>> call, Response<List<Message>> response) {
                if (response.isSuccessful() && response.body() != null) {

                    messages.clear();
                    messages.addAll(response.body());
                    adapter.notifyDataSetChanged();
                    recyclerViewMessages.scrollToPosition(messages.size() - 1);
                } else {
                    addBotMessage("Erro ao carregar histórico.");
                }
            }

            @Override
            public void onFailure(Call<List<Message>> call, Throwable t) {
                addBotMessage("Falha ao carregar histórico: " + t.getMessage());
            }
        });
    }


    private void callChatAPI(String prompt) {
        ChatRequest request = new ChatRequest(prompt, threadId, userId);
        openAIAPI.chat(request).enqueue(new Callback<OpenAIResponse>() {
            @Override
            public void onResponse(Call<OpenAIResponse> call, Response<OpenAIResponse> response) {
                if(response.isSuccessful() && response.body() != null) {
                    addBotMessage(response.body().getResponse());
                } else {
                    addBotMessage("Erro ao obter resposta do servidor.");
                }
            }

            @Override
            public void onFailure(Call<OpenAIResponse> call, Throwable t) {
                addBotMessage("Falha na conexão: " + t.getMessage());
            }
        });
    }

    private void addBotMessage(String text) {
        messages.add(new Message(text, "assistant"));
        adapter.notifyItemInserted(messages.size() - 1);
        recyclerViewMessages.scrollToPosition(messages.size() - 1);
    }

    private void fetchOrCreateThreadId() {
        openAIAPI.getThreadId(String.valueOf(userId)).enqueue(new Callback<ThreadResponse>() {
            @Override
            public void onResponse(Call<ThreadResponse> call, Response<ThreadResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    threadId = response.body().getThreadId();
                    fetchHistory();
                } else {
                    addBotMessage("Erro ao obter threadId.");
                }
            }

            @Override
            public void onFailure(Call<ThreadResponse> call, Throwable t) {
                addBotMessage("Falha na conexão: " + t.getMessage());
            }
        });
    }

}
