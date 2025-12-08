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

    //Justin
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

    //Justin
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

    // jordan
    @Test
    public void multipleWatchlistItemsReturnedForUser() {
        // User
        User user = new User("watchUser", "password", false);
        user.setUserId(1);
        userDao.insert(user);

        // Movies
        Movie movie1 = new Movie("Movie 1", "Desc 1", "Poster1", "Director 1");
        movie1.setMovieId(1);
        movieDao.insert(movie1);

        Movie movie2 = new Movie("Movie 2", "Desc 2", "Poster2", "Director 2");
        movie2.setMovieId(2);
        movieDao.insert(movie2);

        // Watchlist entries
        Watchlist wl1 = new Watchlist(1, 1, "planned");
        Watchlist wl2 = new Watchlist(1, 2, "planned");
        watchlistDao.insert(wl1);
        watchlistDao.insert(wl2);

        List<Watchlist> watchlists = watchlistDao.getWatchlistByUserIdSync(1);
        assertNotNull(watchlists.get(0));
        assertEquals(2, watchlists.size());
    }

    // jordan
    @Test
    public void deleteAllWatchlist() {
        // User
        User user = new User("deleteWatchUser", "password", false);
        user.setUserId(1);
        userDao.insert(user);

        // Movie
        Movie movie = new Movie("Movie Delete", "Desc", "Poster", "Director");
        movie.setMovieId(1);
        movieDao.insert(movie);

        // Watchlist
        Watchlist watchlist = new Watchlist(1, 1, "planned");
        watchlistDao.insert(watchlist);

        List<Watchlist> beforeDelete = watchlistDao.getWatchlistByUserIdSync(1);
        assertEquals(1, beforeDelete.size());

        watchlistDao.deleteAll();
        List<Watchlist> afterDelete = watchlistDao.getWatchlistByUserIdSync(1);
        assertEquals(0, afterDelete.size());
    }

}
