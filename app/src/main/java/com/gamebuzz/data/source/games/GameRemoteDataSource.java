package com.gamebuzz.data.source.games;

import android.util.Log;

import androidx.annotation.NonNull;

import com.gamebuzz.model.Game;
import com.gamebuzz.model.GameApiResponse;
import com.gamebuzz.service.GameApiService;
import com.gamebuzz.service.SearchGameApiService;
import com.gamebuzz.util.ServiceLocator;

import java.util.List;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GameRemoteDataSource extends BaseGamesRemoteDataSource {

    private final static String TAG = GameRemoteDataSource.class.getSimpleName();

    private final GameApiService gameApiService;

    private final SearchGameApiService searchGameApiService;

    public GameRemoteDataSource() {
        this.gameApiService = ServiceLocator.getInstance().getGameApiService();
        this.searchGameApiService = ServiceLocator.getInstance().getSearchGameApiService();
    }

    @Override

    public void getGames() {

        String text = "fields name, summary, genres.name, cover.url, release_dates.human, screenshots.url, involved_companies.company.name, themes.name, platforms.name; where summary != null & genres.name != null & cover.url != null & involved_companies != null & themes != null & platforms != null;  limit 20;";
        RequestBody body = RequestBody.create(MediaType.parse("text/plain"), text);

        Call<List<Game>> gameResponseCall = gameApiService.getGames(body);

        gameResponseCall.enqueue(new Callback<List<Game>>() {
            @Override
            public void onResponse(@NonNull Call<List<Game>> call,
                                   @NonNull Response<List<Game>> response) {
                if(response.body() != null && response.isSuccessful()) {
                    GameApiResponse gameApiResponse = new GameApiResponse(response.body());
                    gamesCallback.onSuccessFromRemote(gameApiResponse);
                } else {
                    gamesCallback.onFailureFromRemote(new Exception("API KEY ERROR"));
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<Game>> call, @NonNull Throwable t) {
                gamesCallback.onFailureFromRemote(new Exception("RETROFIT ERROR"));

            }
        });
    }

    @Override
    public void searchGames(String query) {
        String text = "fields name, summary, genres.name, cover.url, release_dates.human, screenshots.url, involved_companies.company.name, themes.name, platforms.name; where summary != null & genres.name != null & cover.url != null & involved_companies != null & themes != null & platforms != null;  limit 10; search \"" + query + "\"";
        RequestBody body = RequestBody.create(MediaType.parse("text/plain"), text);

        Call<List<Game>> gameResponseCall = searchGameApiService.getGames(body);

        gameResponseCall.enqueue(new Callback<List<Game>>() {
            @Override
            public void onResponse(@NonNull Call<List<Game>> call,
                                   @NonNull Response<List<Game>> response) {
                if(response.body() != null && response.isSuccessful()) {
                    GameApiResponse gameApiResponse = new GameApiResponse(response.body());
                    gamesCallback.onSuccessFromRemote(gameApiResponse);
                } else {
                    gamesCallback.onFailureFromRemote(new Exception("API KEY ERROR"));
                }
            }
            @Override
            public void onFailure(@NonNull Call<List<Game>> call, @NonNull Throwable t) {
                gamesCallback.onFailureFromRemote(new Exception("RETROFIT ERROR"));

            }
        });
    }

}
