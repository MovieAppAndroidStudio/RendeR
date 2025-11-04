package com.example.labandroiddemo.database.entities;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.example.labandroiddemo.database.MovieDatabase;

/**
 * User.java
 * Entity class for user in database
 **/

@Entity(tableName = MovieDatabase.USER_TABLE)
public class User {
    @PrimaryKey(autoGenerate = true)
    private int userId;

    private String username;
    private String password;

    // ---- Constructor ----
    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    // ---- Getters and Setters ----

    public int getId() {
        return userId;
    }

    public void setId(int id) {
        this.userId = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
