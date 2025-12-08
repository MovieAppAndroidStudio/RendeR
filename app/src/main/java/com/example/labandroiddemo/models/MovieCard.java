package com.example.labandroiddemo.models;

public class MovieCard {
    private final String title;
    private final String posterUrl;
    private final String director;
    private final String genre;

    public MovieCard(String title, String posterUrl, String director, String genre) {
        this.title = title;
        this.posterUrl = posterUrl;
        this.director = director;
        this.genre = genre;
    }

    public String getTitle() {
        return title;
    }

    public String getPosterUrl() {
        return posterUrl;
    }

    public String getDirector() {
        return director;
    }

    public String getGenre() {
        return genre;
    }
}