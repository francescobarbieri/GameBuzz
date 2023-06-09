package com.gamebuzz.util;


import static com.gamebuzz.util.Constants.ENCRYPTED_SHARED_PREFERENCES_FILE_NAME;
import static com.gamebuzz.util.Constants.ID_TOKEN;

import android.app.Application;

import com.gamebuzz.data.database.GameRoomDatabase;
import com.gamebuzz.data.repository.game.GameRepositoryWithLiveData;
import com.gamebuzz.data.repository.game.IGamesRepositoryWithLiveData;
import com.gamebuzz.data.source.games.BaseFavoriteGamesDataSource;
import com.gamebuzz.data.source.games.BaseGamesLocalDataSource;
import com.gamebuzz.data.source.games.BaseGamesRemoteDataSource;
import com.gamebuzz.data.source.games.FavoriteGamesDataSource;
import com.gamebuzz.data.source.games.GameRemoteDataSource;
import com.gamebuzz.data.source.games.GamesLocalDataSource;
import com.gamebuzz.data.source.games.GamesMockRemoteDataSource;
import com.gamebuzz.data.source.user.BaseUserDataRemoteDataSource;
import com.gamebuzz.data.source.user.UserDataRemoteDataSource;
import com.gamebuzz.data.source.user.user.BaseUserAuthenticationRemoteDataSource;
import com.gamebuzz.data.source.user.UserAuthenticationRemoteDataSource;
import com.gamebuzz.data.repository.user.IUserRepository;
import com.gamebuzz.data.repository.user.UserRepository;
import com.gamebuzz.service.GameApiService;
import com.gamebuzz.service.SearchGameApiService;

import java.io.IOException;
import java.security.GeneralSecurityException;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ServiceLocator {
    private static volatile ServiceLocator INSTANCE = null;

    private ServiceLocator() {}

    public static ServiceLocator getInstance() {
        if (INSTANCE == null) {
            synchronized (ServiceLocator.class) {
                if(INSTANCE == null) {
                    INSTANCE = new ServiceLocator();
                }
            }
        }
        return INSTANCE;
    }

    public GameRoomDatabase getGameDao(Application application) {
        return GameRoomDatabase.getDatabase(application);
    }

    public GameApiService getGameApiService () {
        Retrofit retrofit = new Retrofit.Builder().baseUrl(Constants.GAME_API_BASE_URL).addConverterFactory(GsonConverterFactory.create()).build();
        return retrofit.create(GameApiService.class);
    }

    public SearchGameApiService getSearchGameApiService () {
        Retrofit retrofit = new Retrofit.Builder().baseUrl(Constants.GAME_API_BASE_URL).addConverterFactory(GsonConverterFactory.create()).build();
        return retrofit.create(SearchGameApiService.class);
    }

    public IGamesRepositoryWithLiveData getGameRepository(Application application, boolean debugMode) {
        BaseGamesRemoteDataSource gamesRemoteDataSource;
        BaseGamesLocalDataSource gamesLocalDataSource;
        BaseFavoriteGamesDataSource favoriteGamesDataSource;
        // SharedPreferencesUtil sharedPreferencesUtil = new SharedPreferencesUtil(application);
        DataEncryptionUtil dataEncryptionUtil = new DataEncryptionUtil(application);

        if(debugMode) {
            JSONParserUtil jsonParserUtil = new JSONParserUtil(application);
            gamesRemoteDataSource = new GamesMockRemoteDataSource(jsonParserUtil);
        } else {
            gamesRemoteDataSource = new GameRemoteDataSource();
        }

        gamesLocalDataSource = new GamesLocalDataSource(getGameDao(application));

        try {
            favoriteGamesDataSource = new FavoriteGamesDataSource(dataEncryptionUtil.readSecretDataWithEncryptedSharePreferences(ENCRYPTED_SHARED_PREFERENCES_FILE_NAME, ID_TOKEN));
        } catch (GeneralSecurityException | IOException e) {
            return null;
        }

        return new GameRepositoryWithLiveData(gamesRemoteDataSource, gamesLocalDataSource, favoriteGamesDataSource);
    }

    public IUserRepository getUserRepository(Application application) {

        BaseUserAuthenticationRemoteDataSource userRemoteAuthenticationDataSource = new UserAuthenticationRemoteDataSource();

        BaseUserDataRemoteDataSource userDataRemoteDataSource = new UserDataRemoteDataSource();

        BaseGamesLocalDataSource gamesLocalDataSource = new GamesLocalDataSource(getGameDao(application));

        return new UserRepository(userRemoteAuthenticationDataSource, gamesLocalDataSource, userDataRemoteDataSource);
    }
}
