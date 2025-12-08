package com.example.labandroiddemo.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.labandroiddemo.database.entities.User;

import java.util.List;

/**
 * UserDAO.java
 * Data access object for user entity
 */
@Dao
public interface UserDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(User... user);

    @Delete
    void delete(User user);

    @Query("DELETE FROM " + MovieDatabase.USER_TABLE)
    void deleteAll();

    @Query("SELECT * FROM " + MovieDatabase.USER_TABLE)
    LiveData<List<User>> getAllUsers();

    @Query("SELECT * FROM " + MovieDatabase.USER_TABLE)
    List<User> getAllUsersSync();

    @Query("SELECT * FROM " + MovieDatabase.USER_TABLE + " WHERE username == :username")
    LiveData<User> getUserByUsername(String username);

    @Query("SELECT * FROM " + MovieDatabase.USER_TABLE + " WHERE username == :username")
    User getUserByUsernameSync(String username);

    @Query("SELECT * FROM " + MovieDatabase.USER_TABLE + " WHERE userId == :userId")
    LiveData<User> getUserByUserId(int userId);

    @Query("SELECT * FROM " + MovieDatabase.USER_TABLE + " WHERE userId == :userId")
    User getUserByUserIdSync(int userId);

    @Query("SELECT * FROM " + MovieDatabase.USER_TABLE +
            " WHERE username = :username AND password = :password LIMIT 1")
    User login(String username, String password);

    @Query("SELECT * FROM " + MovieDatabase.USER_TABLE + " WHERE username = :username LIMIT 1")
    User getUserSync(String username);

}

