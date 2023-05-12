package com.gamebuzz.service;

import com.gamebuzz.model.GameApiResponse;

import retrofit2.Call;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface GameApiService {
    @Headers({
            "Accept: application/json",
            "Client-ID: 9xiakk40c8cslp3k9o5mp0ep6ma7tr",
            "Authorization: Bearer v39811a9a3tmr3cdrrkkygix7kpube"
    })
    @POST("https://api.igdb.com/v4/games")
    Call<GameApiResponse> getGames(

    );
}