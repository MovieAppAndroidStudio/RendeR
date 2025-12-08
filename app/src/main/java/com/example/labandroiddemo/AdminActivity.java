package com.example.labandroiddemo;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class AdminActivity extends AppCompatActivity {

    private EditText textInputUsername;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        textInputUsername = findViewById(R.id.searchBarName);

        Button btnAddUser = findViewById(R.id.addUser);
//        Button btnRemoveUser = findViewById(R.id.removeUser);
//        Button btnAddMovie = findViewById(R.id.addMovie);
//        Button btnRemoveMovie = findViewById(R.id.removeMovie);
        Button btnAddAdmin = findViewById(R.id.addAdmin);
//        Button btnRemoveAdmin = findViewById(R.id.removeAdmin);
//        Button btnBecomeUser = findViewById(R.id.becomeUser);
        Button btnLogout = findViewById(R.id.btnLogout);

        btnAddUser.setOnClickListener(v -> doAddUser());
//        btnAddMovie.setOnClickListener(v -> doAddMovie());
//        btnRemoveMovie.setOnClickListener(v -> doRemoveMovie());
        btnAddAdmin.setOnClickListener(v -> doAddAdmin());
//        btnRemoveAdmin.setOnClickListener(v -> doRemoveAdmin());
//        btnBecomeUser.setOnClickListener(v -> doBecomeUser());
        btnLogout.setOnClickListener(v -> doLogout());
    }

    private void doAddUser()  {
        String username = textInputUsername.getText().toString().trim();
        String password = "password";
        boolean isAdmin = false;

        if (username.isEmpty()) {
            Toast.makeText(this, "Enter username and password", Toast.LENGTH_SHORT).show();
            return;
        }

        // Store ONE custom account in SharedPreferences (no DB changes)
        SharedPreferences prefs = getSharedPreferences("auth", MODE_PRIVATE);
        prefs.edit()
                .putString("custom_username", username)
                .putString("custom_password", password)
                .putBoolean("isAdmin", isAdmin)
                .apply();
        Toast.makeText(this, "Account created.", Toast.LENGTH_SHORT).show();
    }

    private void doAddAdmin()  {
        String username = textInputUsername.getText().toString().trim();
        String password = "password";
        boolean isAdmin = true;

        if (username.isEmpty()) {
            Toast.makeText(this, "Enter username and password", Toast.LENGTH_SHORT).show();
            return;
        }

        // Store ONE custom account in SharedPreferences (no DB changes)
        SharedPreferences prefs = getSharedPreferences("auth", MODE_PRIVATE);
        prefs.edit()
                .putString("custom_username", username)
                .putString("custom_password", password)
                .putBoolean("isAdmin", isAdmin)
                .apply();

        Toast.makeText(this, "Admin created.", Toast.LENGTH_SHORT).show();
    }

    private void doLogout() {
        startActivity(new Intent(AdminActivity.this, LoginActivity.class));
        finish();
    }

}
