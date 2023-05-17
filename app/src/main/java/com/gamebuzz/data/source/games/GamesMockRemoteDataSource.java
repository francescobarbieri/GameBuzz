package com.gamebuzz.data.source.games;

import com.gamebuzz.model.GameApiResponse;
import com.gamebuzz.util.JSONParserUtil;

import java.io.IOException;

public class GamesMockRemoteDataSource extends BaseGamesRemoteDataSource {

    private final JSONParserUtil jsonParserUtil;

    public GamesMockRemoteDataSource(JSONParserUtil jsonParserUtil) {
        this.jsonParserUtil = jsonParserUtil;
    }


    @Override
    public void getGames() {
        GameApiResponse gameApiResponse = null;

        try {
            gameApiResponse = jsonParserUtil.parseJSONFileWithGSon("game-list.json");
        } catch (IOException e) {
            e.printStackTrace();
        }

        if(gameApiResponse != null) {
            gamesCallback.onSuccessFromRemote(gameApiResponse);
        } else {
            gamesCallback.onFailureFromRemote(new Exception("Error"));
        }
    }
}
