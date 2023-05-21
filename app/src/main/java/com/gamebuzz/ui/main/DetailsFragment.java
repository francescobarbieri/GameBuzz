package com.gamebuzz.ui.main;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.gamebuzz.R;
import com.gamebuzz.adapter.GenresRecyclerViewAdapter;
import com.gamebuzz.adapter.ScreenshotRecyclerViewAdapter;
import com.gamebuzz.data.repository.game.IGamesRepositoryWithLiveData;
import com.gamebuzz.model.Game;
import com.gamebuzz.util.ServiceLocator;
import com.google.android.material.snackbar.Snackbar;


public class DetailsFragment extends Fragment {

    private static final String TAG = DetailsFragment.class.getSimpleName();
    private GenresRecyclerViewAdapter genresRecyclerViewAdapter;
    private ScreenshotRecyclerViewAdapter screenshotRecyclerViewAdapter;

    private GameViewModel gameViewModel;

    public DetailsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        IGamesRepositoryWithLiveData gamesRepositoryWithLiveData =
                ServiceLocator.getInstance().getGameRepository(
                        requireActivity().getApplication(),
                        false
                );

        if(gamesRepositoryWithLiveData != null) {
            gameViewModel = new ViewModelProvider(
                    requireActivity(),
                    new GameViewModelFactory(gamesRepositoryWithLiveData)).get(GameViewModel.class);
        } else {
            Log.e(TAG, "Unexpected error");
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_details, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        final String TAG = DetailsFragment.class.getSimpleName();

        Game game = DetailsFragmentArgs.fromBundle(getArguments()).getGame();

        ImageView cover = view.findViewById(R.id.details_cover);
        TextView title = view.findViewById(R.id.details_game_title);
        TextView summary = view.findViewById(R.id.details_game_summary);
        TextView date = view.findViewById(R.id.details_date);
        TextView platforms = view.findViewById(R.id.details_platforms);
        Button favoriteButton = view.findViewById(R.id.details_favorite_button);

        title.setText(game.getTitle());
        summary.setText(game.getSummary());
        date.setText(game.getReleaseDates().get(0).getDate());

        String platformString = "";
        for(int i = 0; i < game.getGamePlatform().size(); i++) {
            platformString += game.getGamePlatform().get(i).getName() + ", ";
        }

        platforms.setText(platformString);

        Glide.with(getContext())
                .load("https://" + game.getCover().getUrl().replace("t_thumb", "t_cover_big"))
                .into(cover);

        RecyclerView recyclerViewGameGenres = view.findViewById(R.id.details_genres_recyclerView);
        LinearLayoutManager layoutManagerGenres = new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false);

        genresRecyclerViewAdapter = new GenresRecyclerViewAdapter(game.getGameGenre(), requireActivity().getApplication());
        recyclerViewGameGenres.setLayoutManager(layoutManagerGenres);
        recyclerViewGameGenres.setAdapter(genresRecyclerViewAdapter);



        RecyclerView recyclerViewGameScreenshots = view.findViewById(R.id.details_screenshot_recyclerView);
        LinearLayoutManager layoutManagerScreenshots = new LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false);

        screenshotRecyclerViewAdapter = new ScreenshotRecyclerViewAdapter(game.getScreenshotList(), requireActivity().getApplication());
        recyclerViewGameScreenshots.setLayoutManager(layoutManagerScreenshots);
        recyclerViewGameScreenshots.setAdapter(screenshotRecyclerViewAdapter);
        recyclerViewGameScreenshots.setNestedScrollingEnabled(false);

        if(game.getFavorite()) {
            favoriteButton.setCompoundDrawablesWithIntrinsicBounds(R.drawable.bookmarkcheckfill, 0, 0 , 0);
        }

        favoriteButton.setOnClickListener(v -> {
            onFavoriteButtonPressed(game);
        });

    }


    public void onFavoriteButtonPressed(Game game) {
        game.setFavorite(!game.getFavorite());
        gameViewModel.updateGames(game);
    }
}