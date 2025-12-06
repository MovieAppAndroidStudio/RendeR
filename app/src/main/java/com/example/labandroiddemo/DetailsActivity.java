package com.example.labandroiddemo;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.labandroiddemo.databinding.ActivityDetailsBinding;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class DetailsActivity extends AppCompatActivity {

    private static final String TMDB_TOKEN =
            "eyJhbGciOiJIUzI1NiJ9.eyJhdWQiOiJkYWMxYmExZWI3OGMyNTczMzRmYWQ3ZWQ3ZjVmOWUyYyIsIm5iZiI6MTc0NjAzMTgxNi41MjgsInN1YiI6IjY4MTI1NGM4OTVmMTVlODEwMmEwZWM0YiIsInNjb3BlcyI6WyJhcGlfcmVhZCJdLCJ2ZXJzaW9uIjoxfQ.sqSA6E6kjKu0yto67fTNkBNZl_cs50x0H72xZ3Egnj8";

    public static final String TAG = "AUSTIN_ERROR";
    private static final String BASE_URL = "https://api.themoviedb.org/3/search/movie?query=";
    private static final String IMAGE_URL = "https://image.tmdb.org/t/p/w500";
    private static final OkHttpClient client = new OkHttpClient();
    private static final ExecutorService executor = Executors.newSingleThreadExecutor();

    private static final String MOVIE_NAME_EXTRA_KEY = "com.example.labandroiddemo.MOVIE_NAME_EXTRA_KEY";
    private static final String DIRECTOR_NAME_EXTRA_KEY = "com.example.labandroiddemo.DIRECTOR_NAME_EXTRA_KEY";
    private static final String GENRE_NAME_EXTRA_KEY = "com.example.labandroiddemo.GENRE_NAME_EXTRA_KEY";
    private ActivityDetailsBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityDetailsBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        String movieName = getIntent().getStringExtra(MOVIE_NAME_EXTRA_KEY);
        String directorName = getIntent().getStringExtra(DIRECTOR_NAME_EXTRA_KEY);
        String genreName = getIntent().getStringExtra(GENRE_NAME_EXTRA_KEY);

        binding.hiWorld.setText(movieName);
        binding.textView2.setText(directorName);
        binding.textView3.setText(genreName);

        loadMovie(movieName);

    }

    private void loadMovie(String movieName) {
        executor.execute(() ->{
            try {
                JsonObject root = getJson(movieName);
                if (root == null) throw new IOException("Empty response");

                JsonArray results = root.getAsJsonArray("results");

                JsonObject movieJson = results.get(0).getAsJsonObject();

                String resultText = movieJson.get("overview").getAsString();

                String posterPath = movieJson.get("poster_path").getAsString();

                runOnUiThread(() -> {
                    binding.textView4.setText(resultText);
                    binding.textView5.setText(IMAGE_URL + posterPath);

                    Glide.with(this)
                            .load(IMAGE_URL + posterPath)
                            .placeholder(R.drawable.ic_launcher_background) // simple placeholder
                            .into(binding.tmbdPoster);
                });

            } catch (Exception e) {
                e.printStackTrace();
                runOnUiThread(() ->
                        Toast.makeText(this, "Failed to load movies", Toast.LENGTH_SHORT).show());
                return;
            }
        });
    }

    //intent factory constructor to be called in other activities to launch this one (STATIC)
    public static Intent DetailsActivityIntentFactory (Context context, String movieName, String director, String genre) {
        Intent intent = new Intent(context, DetailsActivity.class);
        intent.putExtra(MOVIE_NAME_EXTRA_KEY, movieName);
        intent.putExtra(DIRECTOR_NAME_EXTRA_KEY, director);
        intent.putExtra(GENRE_NAME_EXTRA_KEY, genre);
        return intent;
    }

    private JsonObject getJson(String pathAndQuery) throws IOException {
        String url = BASE_URL + pathAndQuery;

        Request request = new Request.Builder()
                .url(url)
                .addHeader("accept", "application/json")
                .addHeader("Authorization", "Bearer " + TMDB_TOKEN)
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful() || response.body() == null) {
                throw new IOException("Bad response: " + response);
            }
            String body = response.body().string();
            return JsonParser.parseString(body).getAsJsonObject();
        }
    }

}