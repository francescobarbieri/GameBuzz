package com.gamebuzz.adapter;

import android.app.Application;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.gamebuzz.R;
import com.gamebuzz.model.Game;
import com.gamebuzz.model.GameGenre;

import java.util.List;

public class GenresRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final List<GameGenre> gameGenres;

    private final Application application;

    public GenresRecyclerViewAdapter(List<GameGenre> gameGenres, Application application) {
        this.gameGenres = gameGenres;
        this.application = application;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.genre_list_item, parent, false);
        return new GenreViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ((GenreViewHolder) holder).bind(gameGenres.get(position));
    }


    @Override
    public int getItemCount() {
        if (gameGenres != null) {
            return gameGenres.size();
        }
        return 0;
    }

    public class GenreViewHolder extends RecyclerView.ViewHolder {

        private final TextView genreName;

        public GenreViewHolder(@NonNull View view) {
            super(view);
            genreName = view.findViewById(R.id.genre_name);
        }

        public void bind(GameGenre gameGenres) {
            genreName.setText(gameGenres.getName());
        }

    }
}
