package com.example.labandroiddemo;

import static junit.framework.TestCase.assertNotNull;
import static org.junit.Assert.assertNotEquals;

import android.content.Context;

import androidx.room.Room;
import androidx.test.core.app.ApplicationProvider;

import com.example.labandroiddemo.database.MovieDatabase;
import com.example.labandroiddemo.database.UserDAO;
import com.example.labandroiddemo.database.entities.User;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.List;

public class DatabaseTest {
    private UserDAO userDao;
    private MovieDatabase db;

    @Before
    public void createDb(){
        Context context = ApplicationProvider.getApplicationContext();
        db = Room.inMemoryDatabaseBuilder(context, MovieDatabase.class).build();
        userDao = db.userDAO();
    }

    @Test
    public void writeUserAndReadInList(){
        String username = "testuser2";
        String password = "password";
        User user = new User(username, password, false);

        userDao.insert(user);

        List<User> users = userDao.getAllUsers().getValue();
        assertNotNull(users.get(0));
        assertNotEquals(username, users.get(0).getUsername());
    }

    @After
    public void closeDb() throws IOException {
        db.close();
    }
}

