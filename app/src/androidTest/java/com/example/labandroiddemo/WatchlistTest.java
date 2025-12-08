package com.example.labandroiddemo;

import static junit.framework.TestCase.assertNotNull;
import static org.junit.Assert.assertEquals;

import android.content.Context;

import androidx.room.Room;
import androidx.test.core.app.ApplicationProvider;

import com.example.labandroiddemo.database.MovieDAO;
import com.example.labandroiddemo.database.MovieDatabase;
import com.example.labandroiddemo.database.UserDAO;
import com.example.labandroiddemo.database.WatchlistDAO;
import com.example.labandroiddemo.database.entities.Movie;
import com.example.labandroiddemo.database.entities.User;
import com.example.labandroiddemo.database.entities.Watchlist;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.List;

public class WatchlistTest {
    private WatchlistDAO watchlistDao;
    private UserDAO userDao;
    private MovieDAO movieDao;
    private MovieDatabase db;

    @Before
    public void createDb(){
        Context context = ApplicationProvider.getApplicationContext();
        db = Room.inMemoryDatabaseBuilder(context, MovieDatabase.class).build();
        watchlistDao = db.watchlistDAO();
        userDao = db.userDAO();
        movieDao = db.movieDAO();
    }

    @Test
    public void writeWatchlistAndReadInList(){
        // First, create and insert a User
        User user = new User("testUser", "password", false);
        user.setUserId(1);
        userDao.insert(user);

        // Then, create and insert a Movie
        Movie movie = new Movie("Test Movie", "Test Description", "Test PosterURL", "Test Director");
        movie.setMovieId(2);
        movieDao.insert(movie);

        // Now you can insert the Watchlist
        int userId = 1;
        int movieId = 2;
        String status = "planned";
        Watchlist watchlist = new Watchlist(userId, movieId, status);

        watchlistDao.insert(watchlist);

        List<Watchlist> watchlists = watchlistDao.getWatchlistByUserIdSync(userId);
        assertNotNull(watchlists.get(0));
        assertEquals(userId, watchlists.get(0).getUserId());
    }

    @Test
    public void gettingWatchlist() {
        // create and insert a User
        User user = new User("testuser", "password", false);
        user.setUserId(1);
        userDao.insert(user);

        // create and insert a Movie
        Movie movie = new Movie("Test Movie", "Test Description", "Test PosterURL", "Test Director");
        movie.setMovieId(2);
        movieDao.insert(movie);

        // create and insert the Watchlist
        int userId = 1;
        int movieId = 2;
        String status = "planned";
        Watchlist watchlist1 = new Watchlist(userId, movieId, status);

        watchlistDao.insert(watchlist1);

        Watchlist watchlist2 = watchlistDao.getWatchlistItemSync(userId, movieId);
        assertEquals(watchlist1.getUserId(), watchlist2.getUserId());
    }

    @After
    public void closeDb() throws IOException {
        db.close();
    }
}
