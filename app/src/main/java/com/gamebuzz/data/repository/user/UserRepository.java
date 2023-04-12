package com.gamebuzz.data.repository.user;

import androidx.lifecycle.MutableLiveData;

import com.gamebuzz.data.repository.source.user.BaseUserAuthenticationRemoteDataSource;
import com.gamebuzz.model.Result;
import com.gamebuzz.model.User;

public class UserRepository implements IUserRepository, UserResponseCallback {

    private static final String TAG = UserRepository.class.getSimpleName();

    private final BaseUserAuthenticationRemoteDataSource userRemoteDataSource;
    private final MutableLiveData<Result> userMutableLiveData;

    public UserRepository (BaseUserAuthenticationRemoteDataSource userRemoteDataSource) {
        this.userRemoteDataSource = userRemoteDataSource;
        this.userRemoteDataSource.setUserResponseCallback(this);
        this.userMutableLiveData = new MutableLiveData<>();
    }

    @Override
    public MutableLiveData<Result> getUser (String email, String password, boolean isUserRegistered) {
        if(isUserRegistered) {
            login(email, password);
        } else {
            signUp(email, password);
        }

        return userMutableLiveData;
    }

    @Override
    public MutableLiveData<Result> getGoogleUser(String idToken) {
        signInWithGoogle(idToken);
        return userMutableLiveData;
    }

    @Override
    public MutableLiveData<Result> logout() {
        userRemoteDataSource.logout();
        return userMutableLiveData;
    }

    @Override
    public User getLoggedUser() {
        return userRemoteDataSource.getLoggedUser();
    }

    @Override
    public void signUp(String email, String password) {
        userRemoteDataSource.signUp(email, password);
    }

    @Override
    public void login(String email, String password) {
        userRemoteDataSource.login(email, password);
    }

    @Override
    public void signInWithGoogle(String token) {
        userRemoteDataSource.signInWithGoogle(token);
    }

    @Override
    public void onSuccessFromAuthentication (User user) {
        // TODO: do this
    }

    @Override
    public void onFailureFromAuthentication(String message) {
        Result.Error result = new Result.Error(message);
        userMutableLiveData.postValue(result);
    }

    @Override
    public void onSuccessLogout() {
        // TODO: do this
    }

}