package com.gamebuzz.ui.welcome;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.gamebuzz.data.repository.user.IUserRepository;
import com.gamebuzz.model.Result;
import com.gamebuzz.model.User;

public class UserViewModel extends ViewModel {
    private static final String TAG = UserViewModel.class.getSimpleName();
    private final IUserRepository userRepository;
    private MutableLiveData<Result> userMutableLiveData;

    private MutableLiveData<Result> userFavoriteGamesMutableLiveData;
    private boolean authenticationError;

    public UserViewModel (IUserRepository userRepository) {
        this.userRepository = userRepository;
        authenticationError = false;
    }

    public MutableLiveData<Result> getUserMutableLiveData(
            String email, String password, boolean isUserRegistered) {
        if (userMutableLiveData == null) {
            getUserData(email, password, isUserRegistered);
        }
        return userMutableLiveData;
    }

    public MutableLiveData<Result> getUserFavoriteGamesMutableLiveData(String idToken) {
        if(userFavoriteGamesMutableLiveData == null) {
            getUserFavoriteGames(idToken);
        }

        return  userFavoriteGamesMutableLiveData;
    }


    public User getLoggedUser() {
        return userRepository.getLoggedUser();
    }

    public MutableLiveData<Result> logout() {
        if(userMutableLiveData == null) {
            userMutableLiveData = userRepository.logout();
        } else {
            userRepository.logout();
        }

        return userMutableLiveData;
    }

    public void getUser(String email, String password, boolean isUserRegistered) {
        userRepository.getUser(email, password, isUserRegistered);
    }

    public boolean isAuthenticationError() {
        return authenticationError;
    }

    public void setAuthenticationError(boolean authenticationError) {
        this.authenticationError = authenticationError;
    }

    private void getUserData(String email, String password, boolean isUserRegistered) {
        userMutableLiveData = userRepository.getUser(email, password, isUserRegistered);
    }

    private void getUserData(String token) {
        userMutableLiveData = userRepository.getGoogleUser(token);
    }

    private void getUserFavoriteGames(String idToken) {
        userFavoriteGamesMutableLiveData = userRepository.getUserFavoriteGames(idToken);
    }

}
