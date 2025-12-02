package com.example.labandroiddemo;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.labandroiddemo.adapters.HomeMoviesAdapter;
import com.example.labandroiddemo.models.MovieCard;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class HomeActivity extends AppCompatActivity {

    private static final String TMDB_TOKEN =
            "eyJhbGciOiJIUzI1NiJ9.eyJhdWQiOiJkYWMxYmExZWI3OGMyNTczMzRmYWQ3ZWQ3ZjVmOWUyYyIsIm5iZiI6MTc0NjAzMTgxNi41MjgsInN1YiI6IjY4MTI1NGM4OTVmMTVlODEwMmEwZWM0YiIsInNjb3BlcyI6WyJhcGlfcmVhZCJdLCJ2ZXJzaW9uIjoxfQ.sqSA6E6kjKu0yto67fTNkBNZl_cs50x0H72xZ3Egnj8";

    private static final String BASE_URL = "https://api.themoviedb.org/3";
    private static final String IMAGE_URL = "https://image.tmdb.org/t/p/w500";

    private static final OkHttpClient client = new OkHttpClient();
    private static final ExecutorService executor = Executors.newSingleThreadExecutor();

    // Simple genre map for first genre id
    private static final Map<Integer, String> GENRES = new HashMap<>();
    static {
        GENRES.put(28, "Action");
        GENRES.put(12, "Adventure");
        GENRES.put(16, "Animation");
        GENRES.put(35, "Comedy");
        GENRES.put(80, "Crime");
        GENRES.put(18, "Drama");
        GENRES.put(14, "Fantasy");
        GENRES.put(27, "Horror");
        GENRES.put(53, "Thriller");
        GENRES.put(878, "Sci-Fi");
    }

    private HomeMoviesAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        RecyclerView recyclerView = findViewById(R.id.recyclerMovies);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));

        adapter = new HomeMoviesAdapter(this);
        recyclerView.setAdapter(adapter);

        loadMovies();
    }

    private void loadMovies() {
        executor.execute(() -> {
            List<MovieCard> movies = new ArrayList<>();

            try {
                // popular movies
                JsonObject root = getJson("/movie/popular?language=en-US&page=1");
                if (root == null) throw new IOException("Empty response");

                JsonArray results = root.getAsJsonArray("results");
                int limit = Math.min(results.size(), 10);

                for (int i = 0; i < limit; i++) {
                    JsonObject m = results.get(i).getAsJsonObject();

                    int id = m.get("id").getAsInt();
                    String title = m.get("title").getAsString();

                    String posterPath = m.get("poster_path").isJsonNull()
                            ? null : m.get("poster_path").getAsString();
                    String posterUrl = posterPath != null ? IMAGE_URL + posterPath : null;

                    // first genre id mapped via GENRES
                    String genre = "Unknown";
                    JsonArray genreIds = m.getAsJsonArray("genre_ids");
                    if (genreIds != null && genreIds.size() > 0) {
                        int gid = genreIds.get(0).getAsInt();
                        genre = GENRES.getOrDefault(gid, "Unknown");
                    }

                    String director = getDirector(id);

                    movies.add(new MovieCard(title, posterUrl, director, genre));
                }
            } catch (Exception e) {
                e.printStackTrace();
                runOnUiThread(() ->
                        Toast.makeText(this, "Failed to load movies", Toast.LENGTH_SHORT).show());
                return;
            }

            List<MovieCard> finalMovies = movies;
            runOnUiThread(() -> adapter.setMovies(finalMovies));
        });
    }

    private String getDirector(int movieId) {
        try {
            JsonObject root = getJson("/movie/" + movieId + "/credits");
            if (root == null) return "Unknown";

            JsonArray crew = root.getAsJsonArray("crew");
            if (crew == null) return "Unknown";

            for (JsonElement e : crew) {
                JsonObject person = e.getAsJsonObject();
                if ("Director".equalsIgnoreCase(person.get("job").getAsString())) {
                    return person.get("name").getAsString();
                }
            }
        } catch (Exception ignored) { }
        return "Unknown";
    }

    // Shared helper: GET + parse JSON with Bearer auth
    private JsonObject getJson(String pathAndQuery) throws IOException {
        String url = BASE_URL + pathAndQuery;

        Request request = new Request.Builder()
                .url(url)
                .addHeader("accept", "application/json")
                .addHeader("Authorization", "Bearer " + TMDB_TOKEN)
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful() || response.body() == null) {
                throw new IOException("Bad response: " + response);
            }
            String body = response.body().string();
            return JsonParser.parseString(body).getAsJsonObject();
        }
    }
}
