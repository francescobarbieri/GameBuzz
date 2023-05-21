package com.gamebuzz.ui.main;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gamebuzz.R;
import com.gamebuzz.adapter.GameRecyclerViewAdapter;
import com.gamebuzz.model.Game;
import com.gamebuzz.model.Result;
import com.gamebuzz.util.ErrorMessagesUtil;

import java.util.ArrayList;
import java.util.List;

public class FavoriteFragment extends Fragment {

    private static final String TAG = FavoriteFragment.class.getSimpleName();

    private List<Game> gameList;
    private GameRecyclerViewAdapter gameRecyclerViewAdapter;
    private GameViewModel gameViewModel;

    public FavoriteFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        gameList = new ArrayList<>();
        gameViewModel = new ViewModelProvider(requireActivity()).get(GameViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_favorite, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        RecyclerView recyclerView = view.findViewById(R.id.recyclerview_game_fav);
        GridLayoutManager layoutManager = new GridLayoutManager(requireContext(), 2, RecyclerView.VERTICAL, false);

        gameRecyclerViewAdapter = new GameRecyclerViewAdapter(gameList, requireActivity().getApplication(), new GameRecyclerViewAdapter.OnItemClickListener() {
            @Override
            public void onGameItemClick(Game game) {
                FavoriteFragmentDirections.ActionFavoriteFragmentToDetailsFragment action = FavoriteFragmentDirections.actionFavoriteFragmentToDetailsFragment(game);
                Navigation.findNavController(view).navigate(action);
            }
        });

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(gameRecyclerViewAdapter);

        boolean isFirstLoading = false;

        // TODO: edit true accordingly
        gameViewModel.getFavoriteGamesLiveData(isFirstLoading).observe(getViewLifecycleOwner(), result -> {
            if(result != null) {
                if(result instanceof Result.GameResponseSuccess) {
                    gameList.clear();
                    gameList.addAll(((Result.GameResponseSuccess) result).getData().getGamesList());
                    gameRecyclerViewAdapter.notifyDataSetChanged();
                    if(isFirstLoading) {
                        // TODO: change firstLoading
                    }
                } else {
                    ErrorMessagesUtil errorMessagesUtil = new ErrorMessagesUtil(requireActivity().getApplication());
                }
            }
        });
    }

}