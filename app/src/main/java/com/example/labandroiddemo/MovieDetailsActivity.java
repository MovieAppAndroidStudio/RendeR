package com.example.labandroiddemo;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.labandroiddemo.database.MovieDatabase;
import com.example.labandroiddemo.database.entities.Movie;
import com.example.labandroiddemo.database.entities.User;
import com.example.labandroiddemo.database.entities.Watchlist;

import java.util.concurrent.Executors;

public class MovieDetailsActivity extends AppCompatActivity {

    private MovieDatabase db;
    private int movieId;
    private int userId = -1;

    private TextView tvTitle, tvDirector, tvDesc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);

        db = MovieDatabase.getInstance(this);

        tvTitle = findViewById(R.id.tvTitle);
        tvDirector = findViewById(R.id.tvDirector);
        tvDesc = findViewById(R.id.tvDesc);

        Button btnAddPlanned = findViewById(R.id.btnAddPlanned);
        Button btnAddWatched = findViewById(R.id.btnAddWatched);

        movieId = getIntent().getIntExtra("movieId", -1);

        loadUserId();

        db.movieDAO().getMovieById(movieId).observe(this, movie -> {
            if (movie == null) return;
            tvTitle.setText(movie.getTitle());
            tvDirector.setText("Director: " + movie.getDirector());
            tvDesc.setText(movie.getDescription());
        });

        btnAddPlanned.setOnClickListener(v -> addOrUpdateWatchlist("planned"));
        btnAddWatched.setOnClickListener(v -> addOrUpdateWatchlist("watched"));
    }

    private void loadUserId() {
        SharedPreferences prefs = getSharedPreferences("auth", MODE_PRIVATE);
        String username = prefs.getString("username", null);

        if (username == null) return;

        Executors.newSingleThreadExecutor().execute(() -> {
            User u = db.userDAO().getUserSync(username);
            if (u != null) userId = u.getUserId();
        });
    }

    private void addOrUpdateWatchlist(String status) {
        if (userId == -1 || movieId == -1) {
            Toast.makeText(this, "Not ready (user/movie).", Toast.LENGTH_SHORT).show();
            return;
        }

        Executors.newSingleThreadExecutor().execute(() -> {
            Watchlist existing = db.watchlistDAO().getWatchlistItemSync(userId, movieId);
            if (existing == null) {
                db.watchlistDAO().insert(new Watchlist(userId, movieId, status));
                runOnUiThread(() -> Toast.makeText(this, "Added to watchlist (" + status + ")", Toast.LENGTH_SHORT).show());
            } else {
                db.watchlistDAO().updateStatus(existing.getWatchlistId(), status);
                runOnUiThread(() -> Toast.makeText(this, "Updated to (" + status + ")", Toast.LENGTH_SHORT).show());
            }
        });
    }
}
