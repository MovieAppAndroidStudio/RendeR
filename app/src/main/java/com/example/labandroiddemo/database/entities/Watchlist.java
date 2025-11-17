//package com.example.labandroiddemo.database.entities;
//
//import androidx.room.Entity;
//import androidx.room.ForeignKey;
//import androidx.room.Index;
//import androidx.room.PrimaryKey;
//
//import com.example.labandroiddemo.database.MovieDatabase;
//
///**
// * Watchlist.java
// * Entity class for user's movie watchlist
// */
//
//@Entity(tableName = MovieDatabase.WATCHLIST_TABLE,
//        foreignKeys = {
//                @ForeignKey(entity = User.class,
//                        parentColumns = "id",
//                        childColumns = "user_id"), // I have no clue if this entity actually works, I had to search this up
//                @ForeignKey(entity = Movie.class, // TODO: look into this section more about foreign keys
//                        parentColumns = "id",   // TODO: ask TA/Dr.C about this
//                        childColumns = "movie_id")
//        },
//        indices = {@Index("user_id"), @Index("movie_id")})
//public class Watchlist {
//    @PrimaryKey(autoGenerate = true)
//    private int watchlistId;
//
//    private int userId;
//    private int movieId;
//    private String status; // planned or watched
//
//    // ---- Constructor ----
//    public Watchlist(int userId, int movieId, String status) {
//        this.userId = userId;
//        this.movieId = movieId;
//        this.status = status;
//    }
//
//    // ---- Getters and Setters ----
//
//    public int getWatchlistId() {
//        return watchlistId;
//    }
//
//    public void setWatchlistId(int watchlistId) {
//        this.watchlistId = watchlistId;
//    }
//
//    public int getUserId() {
//        return userId;
//    }
//
//    public void setUserId(int userId) {
//        this.userId = userId;
//    }
//
//    public String getStatus() {
//        return status;
//    }
//
//    public void setStatus(String status) {
//        this.status = status;
//    }
//
//    public int getMovieId() {
//        return movieId;
//    }
//
//    public void setMovieId(int movieId) {
//        this.movieId = movieId;
//    }
//}
