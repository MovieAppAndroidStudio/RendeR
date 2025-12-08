package com.example.labandroiddemo;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import android.content.Context;

import androidx.room.Room;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.example.labandroiddemo.database.MovieDatabase;
import com.example.labandroiddemo.database.dao.WatchlistDAO;
import com.example.labandroiddemo.database.entities.Watchlist;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Instrumented tests for WatchlistDAO.
 * Verifies insert + get + updateStatus.
 */
@RunWith(AndroidJUnit4.class)
public class WatchlistDaoTest {

    private MovieDatabase db;
    private WatchlistDAO watchlistDAO;

    @Before
    public void createDb() {
        Context context = ApplicationProvider.getApplicationContext();
        db = Room.inMemoryDatabaseBuilder(context, MovieDatabase.class)
                .allowMainThreadQueries()
                .build();
        watchlistDAO = db.watchlistDAO();
    }

    @After
    public void closeDb() {
        db.close();
    }

    @Test
    public void insertAndGetWatchlistItem() {
        int userId = 1;
        int movieId = 10;
        String status = "Plan to Watch";

        Watchlist item = new Watchlist(userId, movieId, status);

        watchlistDAO.insert(item);
        Watchlist fromDb = watchlistDAO.getWatchlistItemSync(userId, movieId);

        assertNotNull("Watchlist item should not be null after insert", fromDb);
        assertEquals(userId, fromDb.getUserId());
        assertEquals(movieId, fromDb.getMovieId());
        assertEquals(status, fromDb.getStatus());
    }

    @Test
    public void updateStatus_updatesExistingRow() {
        int userId = 2;
        int movieId = 20;
        String initialStatus = "Watching";
        String updatedStatus = "Finished";

        Watchlist item = new Watchlist(userId, movieId, initialStatus);
        watchlistDAO.insert(item);

        int id = watchlistDAO.getWatchlistId(userId, movieId);
        assertTrue("watchlistId should be positive after insert", id > 0);

        watchlistDAO.updateStatus(id, updatedStatus);
        Watchlist fromDb = watchlistDAO.getWatchlistItemSync(userId, movieId);

        assertNotNull("Watchlist item should still exist after update", fromDb);
        assertEquals(updatedStatus, fromDb.getStatus());
    }
}

}
