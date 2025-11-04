package com.example.labandroiddemo.database;


import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.example.labandroiddemo.database.entities.Movie;
import com.example.labandroiddemo.database.entities.User;
import com.example.labandroiddemo.database.entities.Watchlist;

/**
 * MovieDatabase.java
 * Main room database class for RendeR app
 *
 * Holds references to DAO interfaces and table constants
 */
@Database(entities = {User.class, Movie.class, Watchlist.class}, version = 1, exportSchema = false)
public abstract class MovieDatabase extends RoomDatabase{

    // table names
    public static final String TABLE_USER = "user_table";
    public static final String TABLE_MOVIE = "movie_table";
    public static final String TABLE_WATCHLIST = "watchlist_table";


    // DAO access objects
    public abstract UserDao userDao();
    public abstract MovieDao movieDao();
    public abstract WatchlistDao watchlistDao();

}
