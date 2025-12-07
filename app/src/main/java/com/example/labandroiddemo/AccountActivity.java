package com.example.labandroiddemo;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.example.labandroiddemo.databinding.ActivityAccountBinding;

public class AccountActivity extends AppCompatActivity {

    private ActivityAccountBinding binding;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityAccountBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();

        setContentView(view);

        binding.moviesList.setText("Ts Working ONG");

    }

    public static Intent AccountActivityIntentFactory (Context context) {
        Intent intent = new Intent(context, AccountActivity.class);
        return intent;
    }

}