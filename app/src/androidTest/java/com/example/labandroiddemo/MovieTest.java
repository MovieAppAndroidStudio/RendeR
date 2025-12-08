package com.example.labandroiddemo;

import static junit.framework.TestCase.assertNotNull;
import static org.junit.Assert.assertEquals;

import android.content.Context;

import androidx.room.Room;
import androidx.test.core.app.ApplicationProvider;

import com.example.labandroiddemo.database.MovieDAO;
import com.example.labandroiddemo.database.MovieDatabase;
import com.example.labandroiddemo.database.UserDAO;
import com.example.labandroiddemo.database.entities.Movie;
import com.example.labandroiddemo.database.entities.User;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.List;

public class MovieTest {
    private MovieDAO movieDao;
    private MovieDatabase db;

    @Before
    public void createDb(){
        Context context = ApplicationProvider.getApplicationContext();
        db = Room.inMemoryDatabaseBuilder(context, MovieDatabase.class).build();
        movieDao = db.movieDAO();
    }

    //Eraclio
    @Test
    public void writeMovieAndReadInList(){
        String title = "Godzilla";
        String description = "Kaiju movie";
        String posterUrl = "testUrl";
        String director = "Gareth Edwards";
        Movie movie = new Movie(title, description, posterUrl, director);
        movie.setMovieId(1);

        movieDao.insert(movie);

        List<Movie> movies = movieDao.getAllMoviesSync();
        assertNotNull(movies.get(0));
        assertEquals(title, movies.get(0).getTitle());
    }

    //Eraclio
    @Test
    public void gettingMovieById() {
        String title = "Godzilla";
        String description = "Kaiju movie";
        String posterUrl = "testUrl";
        String director = "Gareth Edwards";
        int movieId = 1;
        Movie movie = new Movie(title, description, posterUrl, director);
        movie.setMovieId(movieId);

        movieDao.insert(movie);

        Movie movie2 = movieDao.getMovieByIdSync(movieId);
        assertEquals(movie.getTitle(), movie2.getTitle());
    }

    //Edward
    @Test
    public void gettingMovieListByKeyword() {
        String title = "Godzilla";
        String description = "Kaiju movie";
        String posterUrl = "testUrl";
        String director = "Gareth Edwards";
        int movieId = 1;
        Movie movie = new Movie(title, description, posterUrl, director);
        movie.setMovieId(movieId);

        movieDao.insert(movie);

        List<Movie> movies = movieDao.searchMoviesSync(title);
        assertNotNull(movies.get(0));
        assertEquals(title, movies.get(0).getTitle());
    }

    @After
    public void closeDb() throws IOException {
        db.close();
    }

    // jordan
    @Test
    public void getAllMoviesAlphabeticalOrder() {
        Movie movieB = new Movie("B Movie", "Description B", "posterB", "Director B");
        Movie movieA = new Movie("A Movie", "Description A", "posterA", "Director A");

        movieDao.insert(movieB);
        movieDao.insert(movieA);

        List<Movie> movies = movieDao.getAllMoviesSync();
        assertEquals(2, movies.size());
        // Because of ORDER BY title ASC in the DAO, "A Movie" should be first
        assertEquals("A Movie", movies.get(0).getTitle());
        assertEquals("B Movie", movies.get(1).getTitle());
    }

    // jordan
    @Test
    public void deleteAllMovies() {
        Movie movie1 = new Movie("Movie One", "Desc 1", "poster1", "Director 1");
        Movie movie2 = new Movie("Movie Two", "Desc 2", "poster2", "Director 2");

        movieDao.insert(movie1);
        movieDao.insert(movie2);

        List<Movie> moviesBefore = movieDao.getAllMoviesSync();
        assertEquals(2, moviesBefore.size());

        movieDao.deleteAll();
        List<Movie> moviesAfter = movieDao.getAllMoviesSync();
        assertEquals(0, moviesAfter.size());
    }

}
