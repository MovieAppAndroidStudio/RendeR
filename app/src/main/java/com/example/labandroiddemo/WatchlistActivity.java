package com.example.labandroiddemo;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.labandroiddemo.database.MovieDatabase;
import com.example.labandroiddemo.database.entities.User;
import com.example.labandroiddemo.database.entities.WatchlistMovie;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;

public class WatchlistActivity extends AppCompatActivity {

    private MovieDatabase db;

    private int userId = -1;

    private EditText etWatchlistSearch;
    private RadioGroup rgStatus;
    private ListView lvWatchlist;

    private final List<WatchlistMovie> current = new ArrayList<>();
    private ArrayAdapter<String> adapter;

    private String selectedStatus = "planned";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_watchlist);

        db = MovieDatabase.getInstance(this);

        etWatchlistSearch = findViewById(R.id.etWatchlistSearch);
        Button btnWatchlistSearch = findViewById(R.id.btnWatchlistSearch);
        rgStatus = findViewById(R.id.rgStatus);
        lvWatchlist = findViewById(R.id.lvWatchlist);

        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, new ArrayList<>());
        lvWatchlist.setAdapter(adapter);

        loadUserIdThenRefresh();

        rgStatus.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == R.id.rbPlanned) selectedStatus = "planned";
            else selectedStatus = "watched";
            refresh();
        });

        btnWatchlistSearch.setOnClickListener(v -> refresh());

        // Tap = toggle planned <-> watched
        lvWatchlist.setOnItemClickListener((parent, view, position, id) -> {
            if (userId == -1) return;

            WatchlistMovie item = current.get(position);
            String newStatus = item.status.equals("planned") ? "watched" : "planned";

            Executors.newSingleThreadExecutor().execute(() -> {
                db.watchlistDAO().updateStatus(item.watchlistId, newStatus);
                runOnUiThread(() -> Toast.makeText(this, "Moved to " + newStatus, Toast.LENGTH_SHORT).show());
            });
        });

        // Long press = delete from watchlist
        lvWatchlist.setOnItemLongClickListener((parent, view, position, id) -> {
            WatchlistMovie item = current.get(position);
            Executors.newSingleThreadExecutor().execute(() -> {
                db.watchlistDAO().deleteById(item.watchlistId);
                runOnUiThread(() -> Toast.makeText(this, "Removed: " + item.title, Toast.LENGTH_SHORT).show());
            });
            return true;
        });
    }

    private void loadUserIdThenRefresh() {
        SharedPreferences prefs = getSharedPreferences("auth", MODE_PRIVATE);
        String username = prefs.getString("username", null);

        if (username == null) return;

        Executors.newSingleThreadExecutor().execute(() -> {
            User u = db.userDAO().getUserSync(username);
            if (u != null) userId = u.getUserId();
            runOnUiThread(this::refresh);
        });
    }

    private void refresh() {
        if (userId == -1) return;

        String keyword = etWatchlistSearch.getText().toString().trim();
        db.watchlistDAO().getWatchlistMoviesFiltered(userId, selectedStatus, keyword)
                .observe(this, items -> {
                    current.clear();
                    current.addAll(items);

                    adapter.clear();
                    for (WatchlistMovie wm : items) {
                        adapter.add(wm.title + "  (" + wm.status + ")");
                    }
                    adapter.notifyDataSetChanged();
                });
    }
}
