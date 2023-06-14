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

import java.util.List;

public class SearchResultsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final OnItemClickListener onItemClickListener;
    public interface OnItemClickListener {
        void OnItemClick (Game game);
    }

    private final List<Game> searchResults;

    private final Application application;



    public SearchResultsAdapter(List<Game> searchResults, Application application, OnItemClickListener onItemClickListener) {
        this.searchResults = searchResults;
        this.application = application;
        this.onItemClickListener = onItemClickListener;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.game_results_item, parent, false);
        return new GameSearchViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ((GameSearchViewHolder)holder).bind(searchResults.get(position));
    }

    @Override
    public int getItemCount() {
        if (searchResults != null) {
            return searchResults.size();
        }
        return 0;
    }

    public class GameSearchViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private final TextView textview_game_title;

        private final TextView textview_pub_date;

        private final ImageView gameCover;

        public GameSearchViewHolder(@NonNull View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            textview_game_title = itemView.findViewById(R.id.textview_game_title);
            textview_pub_date = itemView.findViewById(R.id.textview_pub_date);
            gameCover = itemView.findViewById(R.id.imageview_game_search_cover_image);
        }

        public void bind (Game game){
            textview_game_title.setText(game.getTitle());
            textview_pub_date.setText(game.getReleaseDates().get(0).getDate());

            Glide.with(application)
                    .load("https:" + game.getCover().getUrl().replace("t_thumb", "t_cover_big"))
                    .into(gameCover);
        }

        @Override
        public void onClick (View view) {
            onItemClickListener.OnItemClick(searchResults.get(getAdapterPosition()));
        }
    }
}