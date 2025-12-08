package com.example.labandroiddemo;

import static junit.framework.TestCase.assertNotNull;
import static org.junit.Assert.assertEquals;

import android.content.Context;

import androidx.room.Room;
import androidx.test.core.app.ApplicationProvider;

import com.example.labandroiddemo.database.MovieDatabase;
import com.example.labandroiddemo.database.WatchlistDAO;
import com.example.labandroiddemo.database.entities.User;
import com.example.labandroiddemo.database.entities.Watchlist;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.List;

public class WatchlistTest {
    private WatchlistDAO watchlistDao;
    private MovieDatabase db;

    @Before
    public void createDb(){
        Context context = ApplicationProvider.getApplicationContext();
        db = Room.inMemoryDatabaseBuilder(context, MovieDatabase.class).build();
        watchlistDao = db.watchlistDAO();
    }

    @Test
    public void writeWatchlistAndReadInList(){
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
