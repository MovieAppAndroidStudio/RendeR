package com.example.labandroiddemo;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.labandroiddemo.database.MovieDatabase;
import com.example.labandroiddemo.database.entities.Movie;
import com.example.labandroiddemo.database.entities.User;
import com.example.labandroiddemo.database.entities.Watchlist;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;

public class SearchActivity extends AppCompatActivity {

    private MovieDatabase db;

    private EditText etSearch;
    private ArrayAdapter<String> adapter;
    private final List<Movie> currentMovies = new ArrayList<>();

    private int userId = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        db = MovieDatabase.getInstance(this);

        etSearch = findViewById(R.id.etSearch);
        Button btnSearch = findViewById(R.id.btnSearch);
        Button btnRandom = findViewById(R.id.btnRandom);
        ListView lvMovies = findViewById(R.id.lvMovies);

        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, new ArrayList<>());
        lvMovies.setAdapter(adapter);

        // Resolve logged-in userId
        loadUserId();

        // Default: show all movies
        db.movieDAO().getAllMovies().observe(this, movies -> {
            currentMovies.clear();
            currentMovies.addAll(movies);

            adapter.clear();
            for (Movie m : movies) adapter.add(m.getTitle());
            adapter.notifyDataSetChanged();
        });

        btnSearch.setOnClickListener(v -> {
            String keyword = etSearch.getText().toString().trim();
            db.movieDAO().searchMovies(keyword).observe(this, movies -> {
                currentMovies.clear();
                currentMovies.addAll(movies);

                adapter.clear();
                for (Movie m : movies) adapter.add(m.getTitle());
                adapter.notifyDataSetChanged();
            });
        });

        // Tap a movie -> details
        lvMovies.setOnItemClickListener((parent, view, position, id) -> {
            Movie selected = currentMovies.get(position);
            Intent i = new Intent(SearchActivity.this, MovieDetailsActivity.class);
            i.putExtra("movieId", selected.getMovieId());
            startActivity(i);
        });

        // Random movie -> add to watchlist (planned)
        btnRandom.setOnClickListener(v -> {
            if (userId == -1) {
                Toast.makeText(this, "Not logged in.", Toast.LENGTH_SHORT).show();
                return;
            }

            Executors.newSingleThreadExecutor().execute(() -> {
                Movie random = db.movieDAO().getRandomMovie();
                if (random == null) {
                    runOnUiThread(() -> Toast.makeText(this, "No movies in database.", Toast.LENGTH_SHORT).show());
                    return;
                }

                Watchlist existing = db.watchlistDAO().getWatchlistItemSync(userId, random.getMovieId());
                if (existing == null) {
                    db.watchlistDAO().insert(new Watchlist(userId, random.getMovieId(), "planned"));
                    runOnUiThread(() -> Toast.makeText(this, "Random added: " + random.getTitle(), Toast.LENGTH_SHORT).show());
                } else {
                    runOnUiThread(() -> Toast.makeText(this, "Already in watchlist: " + random.getTitle(), Toast.LENGTH_SHORT).show());
                }

                // open details after adding
                Intent i = new Intent(SearchActivity.this, MovieDetailsActivity.class);
                i.putExtra("movieId", random.getMovieId());
                startActivity(i);
            });
        });
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
}
