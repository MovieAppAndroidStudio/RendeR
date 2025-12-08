package com.example.labandroiddemo;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.labandroiddemo.database.MovieDatabase;
import com.example.labandroiddemo.database.UserDAO;
import com.example.labandroiddemo.database.WatchlistDAO;
import com.example.labandroiddemo.database.entities.User;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SignUpActivity extends AppCompatActivity {

    private EditText etSignUpUsername;
    private EditText etSignUpPassword;
    private EditText etSignUpConfirmPassword;

    private MovieDatabase db;
    private UserDAO userDAO;
    private static final ExecutorService executor = Executors.newSingleThreadExecutor();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        etSignUpUsername = findViewById(R.id.etSignUpUsername);
        etSignUpPassword = findViewById(R.id.etSignUpPassword);
        etSignUpConfirmPassword = findViewById(R.id.etSignUpConfirmPassword);

        db = MovieDatabase.getInstance(getApplicationContext());
        userDAO = db.userDAO();

        Button btnSignUp = findViewById(R.id.btnSignUp);
        TextView tvAlreadyHaveAccount = findViewById(R.id.tvAlreadyHaveAccount);

        // Sign up button – creates a simple account
        btnSignUp.setOnClickListener(v -> doSignUp());

        // "Already have an account?" – go back to login
        tvAlreadyHaveAccount.setOnClickListener(v -> {
            startActivity(new Intent(SignUpActivity.this, LoginActivity.class));
            finish();
        });
    }

    private void doSignUp() {
        String username = etSignUpUsername.getText().toString().trim();
        String password = etSignUpPassword.getText().toString().trim();
        String confirm = etSignUpConfirmPassword.getText().toString().trim();

        if (username.isEmpty() || password.isEmpty() || confirm.isEmpty()) {
            Toast.makeText(this, "Please fill out all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!password.equals(confirm)) {
            Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show();
            return;
        }

        // Don't let people "sign up" as the built-in demo users
        if (username.equals("testuser1") || username.equals("admin2")) {
            Toast.makeText(this, "Please choose a different username", Toast.LENGTH_SHORT).show();
            return;
        }

        // Store ONE custom account in SharedPreferences (no DB changes)
        SharedPreferences prefs = getSharedPreferences("auth", MODE_PRIVATE);
        prefs.edit()
                .putString("custom_username", username)
                .putString("custom_password", password)
                .apply();

        User newUser = new User(username, password, false);

        executor.execute(() -> {
            userDAO.insert(newUser);
        });

        Toast.makeText(this, "Account created. Please log in.", Toast.LENGTH_SHORT).show();

        // Go back to login screen
        startActivity(new Intent(SignUpActivity.this, LoginActivity.class));
        finish();
    }
}
