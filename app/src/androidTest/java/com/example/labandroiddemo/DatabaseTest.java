package com.example.labandroiddemo;

import static junit.framework.TestCase.assertNotNull;
import static org.junit.Assert.assertEquals;
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

    //Austin
    @Test
    public void writeUserAndReadInList(){
        String username = "testuser2";
        String password = "password";
        User user = new User(username, password, false);

        userDao.insert(user);

        List<User> users = userDao.getAllUsersSync();
        assertNotNull(users.get(0));
        assertEquals(username, users.get(0).getUsername());
    }

    //Austin
    @Test
    public void gettingUserByUsername() {
        String username = "Starscream";
        String password = "password";
        User user = new User(username, password, false);

        userDao.insert(user);

        User user2 = userDao.getUserByUsernameSync(username);
        assertEquals(user.getUsername(), user2.getUsername());
    }

    //Edward
    @Test
    public void gettingUserById() {
        String username = "Starscream";
        String password = "password";
        int userId = 1;
        User user = new User(username, password, false);
        user.setUserId(userId);

        userDao.insert(user);

        User user2 = userDao.getUserByUserIdSync(userId);
        assertEquals(user.getUsername(), user2.getUsername());
    }

    //Edward
    @Test
    public void gettingUserByUsernameAndPassword(){
        String username = "Starscream";
        String password = "password";
        int userId = 1;
        User user = new User(username, password, false);
        user.setUserId(userId);

        userDao.insert(user);

        User user2 = userDao.login(username, password);
        assertEquals(user.getUsername(), user2.getUsername());
    }

    @After
    public void closeDb() throws IOException {
        db.close();
    }

    // jordan
    @Test
    public void insertingMultipleUsersReturnsAllUsers() {
        User user1 = new User("alphaUser", "password1", false);
        User user2 = new User("betaUser", "password2", false);

        userDao.insert(user1);
        userDao.insert(user2);

        List<User> users = userDao.getAllUsersSync();
        assertEquals(2, users.size());
    }

    // jordan
    @Test
    public void deleteAllClearsAllUsers() {
        User user1 = new User("userDelete1", "password1", false);
        User user2 = new User("userDelete2", "password2", false);

        userDao.insert(user1);
        userDao.insert(user2);

        // ensure they are there
        List<User> usersBefore = userDao.getAllUsersSync();
        assertEquals(2, usersBefore.size());

        // clear and verify
        userDao.deleteAll();
        List<User> usersAfter = userDao.getAllUsersSync();
        assertEquals(0, usersAfter.size());
    }

}

