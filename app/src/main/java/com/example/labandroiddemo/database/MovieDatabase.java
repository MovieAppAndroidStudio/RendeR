package com.example.labandroiddemo.database;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.example.labandroiddemo.database.entities.Movie;
import com.example.labandroiddemo.database.entities.User;
import com.example.labandroiddemo.database.entities.Watchlist;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {User.class, Movie.class, Watchlist.class},
        version = 3,
        exportSchema = false)
public abstract class MovieDatabase extends RoomDatabase {

    public static final String DB_NAME = "MovieDatabase.db";

    public static final String USER_TABLE = "users";
    public static final String MOVIE_TABLE = "movies";
    public static final String WATCHLIST_TABLE = "watchlist";

    private static volatile MovieDatabase INSTANCE;

    public abstract UserDAO userDAO();
    public abstract MovieDAO movieDAO();
    public abstract WatchlistDAO watchlistDAO();

    private static final ExecutorService databaseWriteExecutor =
            Executors.newSingleThreadExecutor();

    public static MovieDatabase getInstance(final Context context) {
        if (INSTANCE == null) {
            synchronized (MovieDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(
                                    context.getApplicationContext(),
                                    MovieDatabase.class,
                                    DB_NAME)
                            .addCallback(roomCallback)
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    private static final Callback roomCallback = new Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            seedDatabase();
        }
    };

    private static void seedDatabase() {
        databaseWriteExecutor.execute(() -> {
            UserDAO userDAO = INSTANCE.userDAO();
            userDAO.deleteAll();

            // seed users
            User testUser = new User("testuser1", "testuser1", false);
            User adminUser = new User("admin2", "admin2", true);

            userDAO.insert(testUser, adminUser);
        });
    }
}
