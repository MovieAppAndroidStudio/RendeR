package com.example.labandroiddemo.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.labandroiddemo.database.entities.Movie;

import java.util.List;

/**
 * MovieDAO.java
 * Data access object for movie entity
 */
@Dao
public interface MovieDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Movie... movie);

    @Delete
    void delete(Movie movie);

    @Query("DELETE FROM " + MovieDatabase.MOVIE_TABLE)
    void deleteAll();

    @Query("SELECT * FROM " + MovieDatabase.MOVIE_TABLE + " ORDER BY title ASC")
    LiveData<List<Movie>> getAllMovies();

    @Query("SELECT * FROM " + MovieDatabase.MOVIE_TABLE + " ORDER BY title ASC")
    List<Movie> getAllMoviesSync();

    @Query("SELECT * FROM " + MovieDatabase.MOVIE_TABLE + " WHERE movieId == :movieId")
    LiveData<Movie> getMovieById(int movieId);

    @Query("SELECT * FROM " + MovieDatabase.MOVIE_TABLE + " WHERE movieId == :movieId")
    Movie getMovieByIdSync(int movieId);

    @Query("SELECT * FROM " + MovieDatabase.MOVIE_TABLE + " WHERE title LIKE '%' || :keyword || '%'")
    LiveData<List<Movie>> searchMovies(String keyword);

    @Query("SELECT * FROM " + MovieDatabase.MOVIE_TABLE + " WHERE title LIKE '%' || :keyword || '%'")
    List<Movie> searchMoviesSync(String keyword);


}
