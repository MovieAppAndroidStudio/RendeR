package com.example.labandroiddemo;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView tvAppName = findViewById(R.id.tvAppName);
        Button btnLoginMain = findViewById(R.id.btnLoginMain);

        // App name on first screen
        tvAppName.setText("rendR");

        // Go to login screen
        btnLoginMain.setOnClickListener(v ->
                startActivity(new Intent(MainActivity.this, LoginActivity.class))
        );
    }

    public static Intent MainActivityIntentFactory (Context context) {
        Intent intent = new Intent(context, MainActivity.class);
        return intent;
    }
}
