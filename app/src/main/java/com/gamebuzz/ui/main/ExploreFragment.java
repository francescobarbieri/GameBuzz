package com.gamebuzz.ui.main;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
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

import com.gamebuzz.R;
import com.gamebuzz.adapter.SearchResultsAdapter;
import com.gamebuzz.data.repository.game.IGamesRepositoryWithLiveData;
import com.gamebuzz.model.Game;
import com.gamebuzz.model.GameApiResponse;
import com.gamebuzz.model.Result;
import com.gamebuzz.util.ErrorMessagesUtil;
import com.gamebuzz.util.ServiceLocator;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;
import java.util.List;

public class ExploreFragment extends Fragment {

    private static final String TAG = ExploreFragment.class.getSimpleName();

    private TextInputLayout inputText;
    private Button submitButton;
    private RecyclerView recyclerView;
    private SearchResultsAdapter searchResultsAdapter;
    private List<Game> gameList;
    private GameViewModel gameViewModel;

    public ExploreFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        IGamesRepositoryWithLiveData gamesRepositoryWithLiveData = ServiceLocator.getInstance().getGameRepository(requireActivity().getApplication(), false);

        if(gamesRepositoryWithLiveData != null) {
            gameViewModel = new ViewModelProvider(
                    this,
                    new GameViewModelFactory(gamesRepositoryWithLiveData)).get(GameViewModel.class);
        } else {
            Snackbar.make(requireActivity().findViewById(android.R.id.content), "Unexpected error", Snackbar.LENGTH_SHORT).show();
        }

        gameList = new ArrayList<>();
    }

    @Override
    @Nullable
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_explore, container, false);
        return view;
    }

    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        inputText = view.findViewById(R.id.search_games_texInput);
        submitButton = view.findViewById(R.id.searchSubmmitButton);
        recyclerView = view.findViewById(R.id.recyclerview_games_results);

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        searchResultsAdapter = new SearchResultsAdapter(gameList, requireActivity().getApplication(), new SearchResultsAdapter.OnItemClickListener(){

            @Override
            public void OnItemClick(Game game){
                ExploreFragmentDirections.ActionExploreFragmentToGameDetailFragment action = ExploreFragmentDirections.actionExploreFragmentToGameDetailFragment(game);
                Navigation.findNavController(view).navigate(action);
            }
        });
        recyclerView.setAdapter(searchResultsAdapter);

        submitButton.setOnClickListener( v -> {
            performSearch(inputText.getEditText().getText().toString().trim(), view);
        });
    }

    private void performSearch(String query, View view) {
        gameViewModel.getGames(query).observe(getViewLifecycleOwner(), result -> {

            if(result instanceof Result.GameResponseSuccess){

                GameApiResponse gameApiResponse = ((Result.GameResponseSuccess)result).getData();
                List<Game> fetchedGames = gameApiResponse.getGamesList();

                if(!gameViewModel.isLoading()) {
                    if(gameViewModel.isFirstLoading()) {
                        gameViewModel.setFirstLoading(false);
                        this.gameList.addAll(fetchedGames);
                        searchResultsAdapter.notifyItemRangeInserted(0, this.gameList.size());
                    } else {
                        gameList.clear();
                        gameList.addAll(fetchedGames);
                        searchResultsAdapter.notifyItemChanged(0, fetchedGames.size());
                    }
                } else {
                    gameViewModel.setLoading(false);
                }
            } else {
                ErrorMessagesUtil errorMessagesUtil = new ErrorMessagesUtil(requireActivity().getApplication());
                Snackbar.make(view, errorMessagesUtil.getErrorMessage(((Result.Error) result).getMessage()), Snackbar.LENGTH_SHORT);
                //    progressBar.setVisibility(View.GONE);
            }
        });
    }

}