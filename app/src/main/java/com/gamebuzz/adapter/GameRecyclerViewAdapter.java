package com.gamebuzz.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.gamebuzz.R;
import com.gamebuzz.model.Game;

import java.util.List;

public class GameRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public interface OnItemClickListener {
        void onGameItemClick(Game game);
    }

    private final List<Game> gameList;

    private final OnItemClickListener onItemClickListener;

    public GameRecyclerViewAdapter(List<Game> gameList, OnItemClickListener onItemClickListener) {
        this.gameList = gameList;
        this.onItemClickListener = onItemClickListener;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.game_list_item, parent, false);

        return new GameViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ((GameViewHolder) holder).bind(gameList.get(position));
    }

    @Override
    public int getItemCount() {
        if (gameList != null) {
            return gameList.size();
        }
        return 0;
    }

    public class GameViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private final TextView textViewTitle;

        public GameViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewTitle = itemView.findViewById(R.id.textview_game_title);
        }

        public void bind(Game game) {
            textViewTitle.setText(game.getTitle());
        }

        @Override
        public void onClick (View view) {
            onItemClickListener.onGameItemClick(gameList.get(getAdapterPosition()));
        }

    }
}
