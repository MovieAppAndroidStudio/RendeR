package com.example.labandroiddemo;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;

import com.example.labandroiddemo.database.MovieDAO;
import com.example.labandroiddemo.database.MovieDatabase;
import com.example.labandroiddemo.database.UserDAO;
import com.example.labandroiddemo.database.WatchlistDAO;
import com.example.labandroiddemo.database.entities.Movie;
import com.example.labandroiddemo.database.entities.Watchlist;
import com.example.labandroiddemo.databinding.ActivityAccountBinding;

import java.util.ArrayList;
import java.util.List;

public class AccountActivity extends AppCompatActivity {

    private static final String USER_ID_EXTRA_KEY = "com.example.labandroiddemo.USER_ID_EXTRA_KEY";

    private ActivityAccountBinding binding;

    private MovieDatabase db;
    private WatchlistDAO watchlistDAO;
    private UserDAO userDAO;
    private MovieDAO movieDAO;

    private List<Integer> movieIdList = new ArrayList<>();
    private String movieIdString = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityAccountBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();

        setContentView(view);

        int userId = getIntent().getIntExtra(USER_ID_EXTRA_KEY, -1);

        db = MovieDatabase.getInstance(getApplicationContext());
        watchlistDAO = db.watchlistDAO();
        userDAO = db.userDAO();
        movieDAO = db.movieDAO();

        if (userId == -1) {
            Toast.makeText(this, "User id = -1", Toast.LENGTH_SHORT).show();
        }
        LiveData<List<Watchlist>> watchListLive = watchlistDAO.getWatchlistByUserId(userId);

        watchListLive.observe(this, watchList -> {
            if (watchList != null && !watchList.isEmpty()) {
                // Build the string fresh each time
                StringBuilder movieIdString = new StringBuilder();

                for (Watchlist item : watchList) {
                    LiveData<Movie> movieLiveData = movieDAO.getMovieById(item.getMovieId());
                    movieLiveData.observe(this, movie -> {
                        if (movie != null) {
                            String movieTitle = movie.getTitle();

                            movieIdString.append("Movie Title: ")
                                    .append(movieTitle)
                                    .append(" (")
                                    .append(item.getStatus())
                                    .append(")\n");

                            binding.moviesList.setText(movieIdString.toString());
                        }
                    });
                }
            } else {
                binding.moviesList.setText("No movies in watchlist");
            }
        });

//        for (int i = 0; i < movieIdList.size(); i++) {
//            movieIdString += movieIdList.get(i);
//        }
//
//        binding.moviesList.setText(movieIdString);

    }

    public static Intent AccountActivityIntentFactory (Context context, int userId) {
        Intent intent = new Intent(context, AccountActivity.class);
        intent.putExtra(USER_ID_EXTRA_KEY, userId);
        return intent;
    }

}