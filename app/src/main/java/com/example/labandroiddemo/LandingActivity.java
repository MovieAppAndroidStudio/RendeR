package com.example.labandroiddemo;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class LandingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landing);

        TextView tvWelcome = findViewById(R.id.tvWelcome);
        Button btnAdmin = findViewById(R.id.btnAdmin);
        Button btnLogout = findViewById(R.id.btnLogout);

        SharedPreferences prefs = getSharedPreferences("auth", MODE_PRIVATE);
        String username = prefs.getString("username", "Unknown");
        boolean isAdmin = prefs.getBoolean("isAdmin", false);

        tvWelcome.setText("Welcome, " + username);

        btnAdmin.setVisibility(isAdmin ? View.VISIBLE : View.INVISIBLE);

        btnLogout.setOnClickListener(v -> {
            prefs.edit().clear().apply();
            startActivity(new Intent(LandingActivity.this, MainActivity.class));
            finish();
        });
    }
}
