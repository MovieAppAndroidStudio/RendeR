package com.example.labandroiddemo;

import static junit.framework.TestCase.assertNotNull;
import static org.junit.Assert.assertEquals;

import android.content.Context;

import androidx.room.Room;
import androidx.test.core.app.ApplicationProvider;

import com.example.labandroiddemo.database.MovieDAO;
import com.example.labandroiddemo.database.MovieDatabase;
import com.example.labandroiddemo.database.UserDAO;
import com.example.labandroiddemo.database.entities.Movie;
import com.example.labandroiddemo.database.entities.User;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.List;

public class MovieTest {
    private MovieDAO movieDao;
    private MovieDatabase db;

    @Before
    public void createDb(){
        Context context = ApplicationProvider.getApplicationContext();
        db = Room.inMemoryDatabaseBuilder(context, MovieDatabase.class).build();
        movieDao = db.movieDAO();
    }

    @Test
    public void writeMovieAndReadInList(){
        String title = "Godzilla";
        String description = "Kaiju movie";
        String posterUrl = "testUrl";
        String director = "Gareth Edwards";
        Movie movie = new Movie(title, description, posterUrl, director);
        movie.setMovieId(1);

        movieDao.insert(movie);

        List<Movie> movies = movieDao.getAllMoviesSync();
        assertNotNull(movies.get(0));
        assertEquals(title, movies.get(0).getTitle());
    }

    @Test
    public void gettingUser() {
        String title = "Godzilla";
        String description = "Kaiju movie";
        String posterUrl = "testUrl";
        String director = "Gareth Edwards";
        int movieId = 1;
        Movie movie = new Movie(title, description, posterUrl, director);
        movie.setMovieId(movieId);

        movieDao.insert(movie);

        Movie movie2 = movieDao.getMovieByIdSync(movieId);
        assertEquals(movie.getTitle(), movie2.getTitle());
    }

    @After
    public void closeDb() throws IOException {
        db.close();
    }
}
