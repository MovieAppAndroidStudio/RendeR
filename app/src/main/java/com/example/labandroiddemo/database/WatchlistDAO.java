//package com.example.labandroiddemo.database;
//
//import androidx.lifecycle.LiveData;
//import androidx.room.Dao;
//import androidx.room.Delete;
//import androidx.room.Insert;
//import androidx.room.OnConflictStrategy;
//import androidx.room.Query;
//
//import com.example.labandroiddemo.database.entities.Watchlist;
//
//import java.util.List;
//
///**
// * MovieDAO.java
// * Data access object for watchlist entity
// */
//@Dao
//public interface WatchlistDAO {
//    @Insert(onConflict = OnConflictStrategy.REPLACE)
//    void insert(Watchlist... watchlist);
//
//    @Delete
//    void delete(Watchlist watchlist);
//
//    @Query("DELETE FROM " + MovieDatabase.WATCHLIST_TABLE)
//    void deleteAll();
//
//    @Query("SELECT * FROM " + MovieDatabase.WATCHLIST_TABLE + " WHERE userId == :userId ORDER BY watchlistId DESC")
//    LiveData<List<Watchlist>> getWatchlistByUserId(int userId);
//
//    @Query("SELECT * FROM " + MovieDatabase.WATCHLIST_TABLE + " WHERE userId == :userId AND movieId == :movieId LIMIT 1")
//    LiveData<Watchlist> getWatchlistItem(int userId, int movieId);
//}
