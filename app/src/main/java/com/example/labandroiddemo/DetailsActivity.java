package com.example.labandroiddemo;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.example.labandroiddemo.databinding.ActivityDetailsBinding;

public class DetailsActivity extends AppCompatActivity {

    private static final String MOVIE_NAME_EXTRA_KEY = "com.example.labandroiddemo.MOVIE_NAME_EXTRA_KEY";
    private ActivityDetailsBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityDetailsBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        String movieName = getIntent().getStringExtra(MOVIE_NAME_EXTRA_KEY);

        binding.hiWorld.setText(movieName);
    }

    //intent factory constructor to be called in other activities to launch this one (STATIC)
    public static Intent DetailsActivityIntentFactory (Context context, String movieName) {
        Intent intent = new Intent(context, DetailsActivity.class);
        intent.putExtra(MOVIE_NAME_EXTRA_KEY, movieName);
        return intent;
    }

}