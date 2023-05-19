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
import android.widget.ProgressBar;

import com.gamebuzz.R;
import com.gamebuzz.adapter.GameRecyclerViewAdapter;
import com.gamebuzz.data.repository.game.GameResponseCallback;
import com.gamebuzz.data.repository.game.IGameRepository;
import com.gamebuzz.data.repository.game.IGamesRepositoryWithLiveData;
import com.gamebuzz.model.Game;
import com.gamebuzz.model.GameApiResponse;
import com.gamebuzz.model.Result;
import com.gamebuzz.util.ErrorMessagesUtil;
import com.gamebuzz.util.ServiceLocator;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment implements GameResponseCallback {

    private static final String TAG = HomeFragment.class.getSimpleName();
    private List<Game> gameList;

    private GameViewModel gameViewModel;
    private IGameRepository iGameRepository;
    private GameRecyclerViewAdapter gameRecyclerViewAdapter;

    public HomeFragment() {
        // Required empty public constructor
    }

    public static HomeFragment newInstance() {
        HomeFragment fragment = new HomeFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // sharedPreferencesUtil = new SharedPreferencesUtil(requireActivity().getApplication());
        //iGameRepository = new GameMockRepository(requireActivity().getApplication(), this, JSONParserUtil.JsonParserType.JSON_OBJECT_ARRAY);

        IGamesRepositoryWithLiveData gamesRepositoryWithLiveData = ServiceLocator.getInstance().getGameRepository(requireActivity().getApplication(), false);

        if(gamesRepositoryWithLiveData != null) {
            gameViewModel = new ViewModelProvider(
                    requireActivity(),
                    new GameViewModelFactory(gamesRepositoryWithLiveData)).get(GameViewModel.class);
        } else {
            Snackbar.make(requireActivity().findViewById(android.R.id.content), "Unexpected error", Snackbar.LENGTH_SHORT).show();
        }

        gameList = new ArrayList<>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        RecyclerView recyclerViewGame = view.findViewById(R.id.recyclerview_game);
        GridLayoutManager layoutManager = new GridLayoutManager(requireContext(), 2, RecyclerView.VERTICAL, false);

        gameRecyclerViewAdapter = new GameRecyclerViewAdapter(gameList, requireActivity().getApplication(), new GameRecyclerViewAdapter.OnItemClickListener() {
            @Override
            public void onGameItemClick(Game game) {
                HomeFragmentDirections.ActionHomeFragmentToGameDetailFragment action = HomeFragmentDirections.actionHomeFragmentToGameDetailFragment(game);
                Navigation.findNavController(view).navigate(action);
            }
        });

        recyclerViewGame.setLayoutManager(layoutManager);
        recyclerViewGame.setAdapter(gameRecyclerViewAdapter);

        ProgressBar progressBar = view.findViewById(R.id.progress_bar);
        progressBar.setVisibility(View.VISIBLE);

        gameViewModel.getGames().observe(getViewLifecycleOwner(), result -> {

            if (result instanceof Result.GameResponseSuccess) {
                GameApiResponse gameResponse = ((Result.GameResponseSuccess) result).getData();
                List<Game> fetchedGames = gameResponse.getGamesList();

                if(!gameViewModel.isLoading()) {
                    if(gameViewModel.isFirstLoading()) {
                        gameViewModel.setFirstLoading(false);
                        this.gameList.addAll(fetchedGames);
                        gameRecyclerViewAdapter.notifyItemRangeInserted(0, this.gameList.size());
                    } else {
                        gameList.clear();
                        gameList.addAll(fetchedGames);
                        gameRecyclerViewAdapter.notifyItemChanged(0, fetchedGames.size());
                    }
                    progressBar.setVisibility(View.GONE);
                } else {
                    gameViewModel.setLoading(false);

                }
            } else {
                ErrorMessagesUtil errorMessagesUtil = new ErrorMessagesUtil(requireActivity().getApplication());
                Snackbar.make(view, errorMessagesUtil.getErrorMessage(((Result.Error) result).getMessage()), Snackbar.LENGTH_SHORT);
                progressBar.setVisibility(View.GONE);
            }

        });

    }

    public void onSuccess(List<Game> gameList) {
        if(gameList != null) {
            this.gameList.clear();
            this.gameList.addAll(gameList);
        }
    }

    public void onFailure(String errorMessage) {
        // TODO: do this
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        gameViewModel.setFirstLoading(true);
        gameViewModel.setLoading(false);
    }

}