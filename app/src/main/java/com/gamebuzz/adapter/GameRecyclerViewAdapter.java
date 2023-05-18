package com.gamebuzz.adapter;

import android.app.Application;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.gamebuzz.R;
import com.gamebuzz.model.Game;
import com.gamebuzz.ui.main.AppActivity;

import java.util.List;

public class GameRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public interface OnItemClickListener {
        void onGameItemClick(Game game);
    }

    private final List<Game> gameList;
    private final Application application;
    private final OnItemClickListener onItemClickListener;

    public GameRecyclerViewAdapter(List<Game> gameList, Application application, OnItemClickListener onItemClickListener) {
        this.gameList = gameList;
        this.onItemClickListener = onItemClickListener;
        this.application = application;
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

        private final ImageView cover;

        public GameViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewTitle = itemView.findViewById(R.id.textview_game_title);
            cover = itemView.findViewById(R.id.imageview_game_cover_image);

            // When user clicks, onClick is invoked
            itemView.setOnClickListener(this);
        }

        public void bind(Game game) {
            textViewTitle.setText(game.getTitle());
            Glide.with(application)
                    .load("https:" + game.getCover().getUrl().replace("t_thumb", "t_cover_big"))
                    .into(cover);
        }

        @Override
        public void onClick (View view) {
            onItemClickListener.onGameItemClick(gameList.get(getAdapterPosition()));
        }

    }
}
