package com.example.labandroiddemo.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.labandroiddemo.DetailsActivity;
import com.example.labandroiddemo.R;
import com.example.labandroiddemo.models.MovieCard;

import java.util.ArrayList;
import java.util.List;

public class HomeMoviesAdapter extends RecyclerView.Adapter<HomeMoviesAdapter.MovieViewHolder> {

    private final Context context;
    private final List<MovieCard> movies = new ArrayList<>();

    public HomeMoviesAdapter(Context context) {
        this.context = context;
    }

    public void setMovies(List<MovieCard> newMovies) {
        movies.clear();
        movies.addAll(newMovies);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MovieViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.item_movie_card, parent, false);
        return new MovieViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieViewHolder holder, int position) {
        MovieCard movie = movies.get(position);

        holder.textTitle.setText(movie.getTitle());
        holder.textDirector.setText(movie.getDirector());
        holder.textGenre.setText(movie.getGenre());

        Glide.with(context)
                .load(movie.getPosterUrl())
                .placeholder(R.drawable.ic_launcher_background) // simple placeholder
                .into(holder.imagePoster);
    }

    @Override
    public int getItemCount() {
        return movies.size();
    }

    static class MovieViewHolder extends RecyclerView.ViewHolder {

        ImageView imagePoster;
        TextView textTitle;
        TextView textDirector;
        TextView textGenre;

        MovieViewHolder(@NonNull View itemView) {
            super(itemView);
            imagePoster = itemView.findViewById(R.id.imagePoster);
            textTitle = itemView.findViewById(R.id.textTitle);
            textDirector = itemView.findViewById(R.id.textDirector);
            textGenre = itemView.findViewById(R.id.textGenre);

            imagePoster.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = DetailsActivity.DetailsActivityIntentFactory(
                            v.getContext(),
                            textTitle.getText().toString(),
                            textDirector.getText().toString(),
                            textGenre.getText().toString()
                            );

                    v.getContext().startActivity(intent);
                }
            });

        }
    }
}

