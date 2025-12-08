package com.example.labandroiddemo;

import static org.junit.Assert.*;

import android.content.Context;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.room.Room;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.example.labandroiddemo.database.MovieDatabase;
import com.example.labandroiddemo.database.entities.Movie;
import com.example.labandroiddemo.database.entities.User;
import com.example.labandroiddemo.database.entities.Watchlist;
import com.example.labandroiddemo.database.entities.WatchlistMovie;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

@RunWith(AndroidJUnit4.class)
public class WatchlistDaoFilterTest {

    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    private MovieDatabase db;

    @Before
    public void setUp() {
        Context context = ApplicationProvider.getApplicationContext();

        db = Room.inMemoryDatabaseBuilder(context, MovieDatabase.class)
                .allowMainThreadQueries()
                .build();

        // Create user
        db.userDAO().insert(new User("tester", "password", false));
        int userId = db.userDAO().getUserSync("tester").getUserId();

        // Create movies
        db.movieDAO().insert(
                new Movie("Interstellar", "Space travel", "", "Christopher Nolan"),
                new Movie("The Matrix", "Simulation", "", "Wachowskis"),
                new Movie("Parasite", "Thriller", "", "Bong Joon-ho")
        );

        // Get inserted IDs (requires MovieDAO.getAllMoviesNow())
        List<Movie> all = db.movieDAO().getAllMoviesNow();
        int interstellarId = all.stream().filter(m -> m.getTitle().equals("Interstellar")).findFirst().get().getMovieId();
        int matrixId = all.stream().filter(m -> m.getTitle().equals("The Matrix")).findFirst().get().getMovieId();
        int parasiteId = all.stream().filter(m -> m.getTitle().equals("Parasite")).findFirst().get().getMovieId();

        // Watchlist: 2 planned, 1 watched
        db.watchlistDAO().insert(
                new Watchlist(userId, interstellarId, "planned"),
                new Watchlist(userId, matrixId, "planned"),
                new Watchlist(userId, parasiteId, "watched")
        );
    }

    @After
    public void tearDown() {
        db.close();
    }

    @Test
    public void watchlistFiltered_byStatusAndKeyword_returnsCorrect() {
        int userId = db.userDAO().getUserSync("tester").getUserId();

        // planned + keyword "Ma" should return "The Matrix" only
        List<WatchlistMovie> results =
                TestUtils.getOrAwaitValue(db.watchlistDAO().getWatchlistMoviesFiltered(userId, "planned", "Ma"));

        assertNotNull(results);
        assertEquals(1, results.size());
        assertEquals("The Matrix", results.get(0).title);
        assertEquals("planned", results.get(0).status);
    }
}
