package com.example.labandroiddemo.database.entities;

/**
 * POJO for JOIN query: Watchlist + Movie
 * (Not an @Entity)
 */
public class WatchlistMovie {
    public int watchlistId;
    public int movieId;
    public String status;

    public String title;
    public String description;
    public String posterURL;
    public String director;
}
