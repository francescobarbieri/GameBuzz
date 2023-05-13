package com.gamebuzz.util;


import android.app.Application;

import com.gamebuzz.data.database.GameRoomDatabase;
import com.gamebuzz.data.source.user.user.BaseUserAuthenticationRemoteDataSource;
import com.gamebuzz.data.source.user.user.UserAuthenticationRemoteDataSource;
import com.gamebuzz.data.repository.user.IUserRepository;
import com.gamebuzz.data.repository.user.UserRepository;
import com.gamebuzz.service.GameApiService;

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

    public IUserRepository getUserRepository(Application application) {

        BaseUserAuthenticationRemoteDataSource userRemoteAuthenticationDataSource = new UserAuthenticationRemoteDataSource();

        return new UserRepository(userRemoteAuthenticationDataSource);
    }
}
