package com.example.labandroiddemo;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {

    private EditText etUsername;
    private EditText etPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        etUsername = findViewById(R.id.etUsername);
        etPassword = findViewById(R.id.etPassword);
        Button btnLogin = findViewById(R.id.btnLogin);

        btnLogin.setOnClickListener(v -> doLogin());
    }

    private void doLogin() {
        String username = etUsername.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        if (username.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Enter username and password", Toast.LENGTH_SHORT).show();
            return;
        }

        // Hard-coded users for this assignment
        boolean isValid = false;
        boolean isAdmin = false;

        if (username.equals("testuser1") && password.equals("testuser1")) {
            isValid = true;
            isAdmin = false;
        } else if (username.equals("admin2") && password.equals("admin2")) {
            isValid = true;
            isAdmin = true;
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

        startActivity(new Intent(LoginActivity.this, LandingActivity.class));
        finish();
    }
}
