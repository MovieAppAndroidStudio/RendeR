package com.example.labandroiddemo.database.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.labandroiddemo.database.entities.Watchlist;

import java.util.List;

@Dao
public interface WatchlistDAO {

    // Return a single watchlist item as LiveData (for observing in UI)
    @Query("SELECT * FROM watchlist WHERE userId = :userId AND movieId = :movieId LIMIT 1")
    LiveData<Watchlist> getWatchlistItem(int userId, int movieId);

    // Return a single watchlist item synchronously (for background-thread work)
    @Query("SELECT * FROM watchlist WHERE userId = :userId AND movieId = :movieId LIMIT 1")
    Watchlist getWatchlistItemSync(int userId, int movieId);

    // Helper to get the primary key for a given user/movie pair
    @Query("SELECT watchlistId FROM watchlist WHERE userId = :userId AND movieId = :movieId LIMIT 1")
    int getWatchlistId(int userId, int movieId);

    // Insert a new watchlist entry
    @Insert
    void insert(Watchlist item);

    // Update only the status column of an existing entry by primary key
    @Query("UPDATE watchlist SET status = :status WHERE watchlistId = :id")
    void updateStatus(int id, String status);

    // (Optional) Get all watchlist rows for a user
    @Query("SELECT * FROM watchlist WHERE userId = :userId")
    LiveData<List<Watchlist>> getWatchlistForUser(int userId);

    // (You should already have this one in your project – leave your existing query here)
    // LiveData<List<WatchlistMovie>> getWatchlistMoviesFiltered(int userId, String status, String keyword);
}
