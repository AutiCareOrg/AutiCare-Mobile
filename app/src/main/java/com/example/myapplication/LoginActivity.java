package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.api.AuthService;
import com.example.myapplication.model.LoginRequest;
import com.example.myapplication.model.LoginResponse;
import com.example.myapplication.network.RetrofitClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    EditText editTextEmail, editTextPassword;
    Button btnLogin;
    TextView textViewRegister;
    ImageButton btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPassword = findViewById(R.id.editTextPassword);
        btnLogin = findViewById(R.id.btnLogin);
        textViewRegister = findViewById(R.id.textViewRegister);
        btnBack = findViewById(R.id.btnBack);

        btnLogin.setOnClickListener(v -> {
            String email = editTextEmail.getText().toString();
            String senha = editTextPassword.getText().toString();

            if(email.isEmpty() || senha.isEmpty()) {
                Toast.makeText(this, "Preencha todos os campos", Toast.LENGTH_SHORT).show();
            } else {
                login(email, senha);
            }
        });

        textViewRegister.setOnClickListener(v -> {
            // Sua lógica para registro, se tiver
        });

        btnBack.setOnClickListener(v -> finish());
    }

    private void login(String email, String senha) {
        AuthService authService = RetrofitClient.getInstance().create(AuthService.class);

        LoginRequest request = new LoginRequest(email, senha);

        Call<LoginResponse> call = authService.login(request);
        call.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                if(response.isSuccessful() && response.body() != null) {
                    LoginResponse loginResponse = response.body();

                    getSharedPreferences("user_prefs", MODE_PRIVATE)
                            .edit()
                            .putInt("user_id", loginResponse.getId_usuario())
                            .apply();

                    Toast.makeText(LoginActivity.this, "Login realizado com sucesso! Bem-vindo " + loginResponse.getEmail(), Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(LoginActivity.this, ChatView.class);
                    startActivity(intent);
                    finish();

                } else {
                    Toast.makeText(LoginActivity.this, "Email ou senha incorretos!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                Log.d("LOGIN", t.getMessage());
                Toast.makeText(LoginActivity.this, "Erro: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
}
