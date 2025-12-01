package com.example.labandroiddemo;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.labandroiddemo.database.MovieDatabase;
import com.example.labandroiddemo.database.UserDAO;
import com.example.labandroiddemo.database.entities.User;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class LoginActivity extends AppCompatActivity {

    private EditText usernameEditText;
    private EditText passwordEditText;
    private Button loginButton;

    private UserDAO userDAO;
    private final ExecutorService executor = Executors.newSingleThreadExecutor();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        usernameEditText = findViewById(R.id.editTextUsername);
        passwordEditText = findViewById(R.id.editTextPassword);
        loginButton = findViewById(R.id.buttonLogin);

        userDAO = MovieDatabase.getInstance(this).userDAO();

        loginButton.setOnClickListener(v -> attemptLogin());
    }

    private void attemptLogin() {
        final String username = usernameEditText.getText().toString().trim();
        final String password = passwordEditText.getText().toString().trim();

        if (username.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Please enter username and password", Toast.LENGTH_SHORT).show();
            return;
        }

        executor.execute(() -> {
            // Room query on background thread
            User user = userDAO.login(username, password);

            runOnUiThread(() -> {
                if (user == null) {
                    Toast.makeText(this, "Invalid username or password", Toast.LENGTH_SHORT).show();
                } else {
                    // Save auth info for LandingActivity
                    SharedPreferences prefs = getSharedPreferences("auth", MODE_PRIVATE);
                    prefs.edit()
                            .putString("username", user.getUsername())
                            .putBoolean("isAdmin", user.isAdmin())
                            .apply();

                    // Go to LandingActivity (the logged-in screen)
                    Intent intent = new Intent(this, LandingActivity.class);
                    startActivity(intent);
                    finish();
                }
            });
        });
    }
}
