package com.gamebuzz.data.source.user;

import com.gamebuzz.data.repository.user.UserResponseCallback;
import com.gamebuzz.model.User;

public abstract class BaseUserDataRemoteDataSource {

    protected UserResponseCallback userResponseCallback;

    public void setUserResponseCallback(UserResponseCallback userResponseCallback) {
        this.userResponseCallback = userResponseCallback;
    }

    public abstract void saveUserData(User user);

    public abstract void getUserFavoriteGames(String idToken);

}
