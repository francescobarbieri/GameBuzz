package com.gamebuzz.ui.main;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gamebuzz.R;
import com.gamebuzz.adapter.GameRecyclerViewAdapter;
import com.gamebuzz.data.repository.game.GameMockRepository;
import com.gamebuzz.data.repository.game.GameResponseCallback;
import com.gamebuzz.data.repository.game.IGameRepository;
import com.gamebuzz.model.Game;
import com.gamebuzz.model.GameApiResponse;
import com.gamebuzz.model.GameCompany;
import com.gamebuzz.model.GameCover;
import com.gamebuzz.model.GameDate;
import com.gamebuzz.model.GameGenre;
import com.gamebuzz.model.GamePlatform;
import com.gamebuzz.model.GameScreenshot;
import com.gamebuzz.model.GameTheme;
import com.gamebuzz.util.JSONParserUtil;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.apache.commons.io.IOUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment implements GameResponseCallback {

    private static final String TAG = HomeFragment.class.getSimpleName();
    private List<Game> gameList;
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

        iGameRepository = new GameMockRepository(requireActivity().getApplication(), this, JSONParserUtil.JsonParserType.JSON_OBJECT_ARRAY);

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

        gameRecyclerViewAdapter = new GameRecyclerViewAdapter(gameList, new GameRecyclerViewAdapter.OnItemClickListener() {
            @Override
            public void onGameItemClick(Game game) {
                Snackbar.make(view, game.getTitle(), Snackbar.LENGTH_SHORT).show();
            }
        });

        recyclerViewGame.setLayoutManager(layoutManager);
        recyclerViewGame.setAdapter(gameRecyclerViewAdapter);

        iGameRepository.fetchGames();

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

}