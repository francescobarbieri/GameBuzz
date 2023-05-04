package com.gamebuzz.util;


import android.app.Application;

import com.gamebuzz.data.database.GameRoomDatabase;
import com.gamebuzz.data.repository.game.IGameRepository;
import com.gamebuzz.data.source.user.user.BaseUserAuthenticationRemoteDataSource;
import com.gamebuzz.data.source.user.user.UserAuthenticationRemoteDataSource;
import com.gamebuzz.data.repository.user.IUserRepository;
import com.gamebuzz.data.repository.user.UserRepository;
import com.gamebuzz.model.Game;

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

    public IUserRepository getUserRepository(Application application) {

        BaseUserAuthenticationRemoteDataSource userRemoteAuthenticationDataSource = new UserAuthenticationRemoteDataSource();

        return new UserRepository(userRemoteAuthenticationDataSource);
    }
}
