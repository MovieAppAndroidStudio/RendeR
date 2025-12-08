package com.example.labandroiddemo;

import static org.junit.Assert.*;

import android.content.Context;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.room.Room;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.example.labandroiddemo.database.MovieDatabase;
import com.example.labandroiddemo.database.entities.Movie;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

@RunWith(AndroidJUnit4.class)
public class MovieDaoSearchTest {

    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    private MovieDatabase db;

    @Before
    public void setUp() {
        Context context = ApplicationProvider.getApplicationContext();

        db = Room.inMemoryDatabaseBuilder(context, MovieDatabase.class)
                .allowMainThreadQueries()
                .build();

        // seed 3 movies
        db.movieDAO().insert(
                new Movie("The Dark Knight", "Batman vs Joker", "", "Christopher Nolan"),
                new Movie("Interstellar", "Space travel", "", "Christopher Nolan"),
                new Movie("Parasite", "Thriller", "", "Bong Joon-ho")
        );
    }

    @After
    public void tearDown() {
        db.close();
    }

    @Test
    public void searchMovies_returnsOnlyMatches() {
        List<Movie> results = TestUtils.getOrAwaitValue(db.movieDAO().searchMovies("dark"));

        assertNotNull(results);
        assertEquals(1, results.size());
        assertEquals("The Dark Knight", results.get(0).getTitle());
    }
}
