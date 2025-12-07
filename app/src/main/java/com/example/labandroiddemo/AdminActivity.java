package com.example.labandroiddemo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.labandroiddemo.database.entities.User;

import java.util.ArrayList;
import java.util.List;

public class AdminActivity extends Activity {

    private EditText textInputUsername;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        textInputUsername = findViewById(R.id.searchBarName);

        Button btnAddUser = findViewById(R.id.addUser);
        Button btnRemoveUser = findViewById(R.id.removeUser);
        Button btnAddMovie = findViewById(R.id.addMovie);
        Button btnRemoveMovie = findViewById(R.id.removeMovie);
        Button btnAddAdmin = findViewById(R.id.addAdmin);
        Button btnRemoveAdmin = findViewById(R.id.removeAdmin);
        Button btnBecomeUser = findViewById(R.id.becomeUser);
        Button btnLogout = findViewById(R.id.btnLogout);

        btnAddUser.setOnClickListener(v -> doAddUser());
        btnRemoveUser.setOnClickListener(v -> doRemoveUser());
        btnAddMovie.setOnClickListener(v -> doAddMovie());
        btnRemoveMovie.setOnClickListener(v -> doRemoveMovie());
        btnAddAdmin.setOnClickListener(v -> doAddAdmin());
        btnRemoveAdmin.setOnClickListener(v -> doRemoveAdmin());
        btnBecomeUser.setOnClickListener(v -> doBecomeUser());
        btnLogout.setOnClickListener(v -> doLogout());
    }

    private void doAddUser()  {
        String username = textInputUsername.getText().toString().trim();
        String password = "password";
        List<User> userList = new ArrayList<>();
        userList =

        if (username.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Enter username and password", Toast.LENGTH_SHORT).show();
            return;
        }

        boolean isValid = false;
        boolean isAdmin = false;


    }
}
