package com.gamebuzz.data.source.user.user;

import com.gamebuzz.data.repository.user.UserResponseCallback;
import com.gamebuzz.model.User;

public abstract class BaseUserAuthenticationRemoteDataSource {
    protected UserResponseCallback userResponseCallback;

    public void setUserResponseCallback(UserResponseCallback userResponseCallback) {
        this.userResponseCallback = userResponseCallback;
    }

    public abstract User getLoggedUser();
    public abstract void logout();
    public abstract void signUp(String email, String password);
    public abstract void login(String email, String password);
    public abstract void signInWithGoogle(String idToken);
}
