package com.gamebuzz.util;


import android.app.Application;

import com.gamebuzz.data.repository.source.user.BaseUserAuthenticationRemoteDataSource;
import com.gamebuzz.data.repository.source.user.UserAuthenticationRemoteDataSource;
import com.gamebuzz.data.repository.user.IUserRepository;
import com.gamebuzz.data.repository.user.UserRepository;

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

    public IUserRepository getUserRepository(Application application) {

        BaseUserAuthenticationRemoteDataSource userRemoteAuthenticationDataSource = new UserAuthenticationRemoteDataSource();

        return new UserRepository(userRemoteAuthenticationDataSource);
    }
}
