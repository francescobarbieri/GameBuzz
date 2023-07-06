package com.gamebuzz.service;

import com.gamebuzz.model.Game;
import com.gamebuzz.model.GameApiResponse;
import com.gamebuzz.util.Constants;

import java.util.List;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface GameApiService {



    @Headers({
            "Accept: application/json",
            "Client-ID: 9xiakk40c8cslp3k9o5mp0ep6ma7tr",
            "Authorization: Bearer uzym96kuyv19d46h7nrhxhvulp48xj"
    })
    @POST(Constants.GAME_API_GAME_ENDPOINT)
    Call<List<Game>> getGames(@Body RequestBody body);
}