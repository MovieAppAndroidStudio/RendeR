package com.example.labandroiddemo;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {

    private EditText etUsername;
    private EditText etPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        etUsername = findViewById(R.id.editTextUsername);
        etPassword = findViewById(R.id.editTextPassword);
        Button btnLogin = findViewById(R.id.buttonLogin);
        TextView tvGoToSignUp = findViewById(R.id.tvGoToSignUp);

        btnLogin.setOnClickListener(v -> doLogin());

        // Navigate to SignUpActivity
        tvGoToSignUp.setOnClickListener(v ->
                startActivity(new Intent(LoginActivity.this, SignUpActivity.class)));
    }

    private void doLogin() {
        String username = etUsername.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        if (username.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Enter username and password", Toast.LENGTH_SHORT).show();
            return;
        }

        boolean isValid = false;
        boolean isAdmin = false;

        if (username.equals("testuser1") && password.equals("testuser1")) {
            isValid = true;
            isAdmin = false;
        } else if (username.equals("admin2") && password.equals("admin2")) {
            isValid = true;
            isAdmin = true;
        } else {
            // Check for custom account created via SignUpActivity
            SharedPreferences prefs = getSharedPreferences("auth", MODE_PRIVATE);
            String customUser = prefs.getString("custom_username", null);
            String customPass = prefs.getString("custom_password", null);

            if (customUser != null && customPass != null
                    && customUser.equals(username)
                    && customPass.equals(password)) {
                isValid = true;
                isAdmin = false; // sign-up users are regular users
            }
        }

        if (!isValid) {
            Toast.makeText(this, "Invalid credentials", Toast.LENGTH_SHORT).show();
            return;
        }

        SharedPreferences prefs = getSharedPreferences("auth", MODE_PRIVATE);
        prefs.edit()
                .putBoolean("logged_in", true)
                .putString("username", username)
                .putBoolean("isAdmin", isAdmin)
                .apply();

        // Go straight to the movie home screen
        startActivity(new Intent(LoginActivity.this, HomeActivity.class));
        finish();
    }
}

