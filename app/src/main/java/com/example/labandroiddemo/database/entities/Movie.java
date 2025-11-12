package com.example.labandroiddemo.database.entities;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.example.labandroiddemo.database.MovieDatabase;

/**
 * Movie.java
 * Entity class for movie in database
 */
@Entity(tableName = MovieDatabase.MOVIE_TABLE)
public class Movie {
    @PrimaryKey(autoGenerate = true)
    private int movieId;

    private String title;
    private String description;
    private String posterURL;
    private String director;

    // ----- constructor ------
    public Movie(String title, String description, String posterURL, String director) {
        this.title = title;
        this.description = description;
        this.posterURL = posterURL;
        this.director = director;
    }

    // ----- getters and setters ------
    public int getMovieId() {
        return movieId;
    }

    public void setMovieId(int movieId) {
        this.movieId = movieId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPosterURL() {
        return posterURL;
    }

    public void setPosterURL(String posterURL) {
        this.posterURL = posterURL;
    }

    public String getDirector() {
        return director;
    }

    public void setDirector(String director) {
        this.director = director;
    }
}
