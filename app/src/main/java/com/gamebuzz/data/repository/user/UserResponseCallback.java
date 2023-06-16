package com.gamebuzz.data.repository.user;

import com.gamebuzz.model.Game;
import com.gamebuzz.model.User;

import java.util.List;

public interface UserResponseCallback {
    void onSuccessFromAuthentication(User user);
    void onFailureFromAuthentication(String message);
    void onSuccessLogout();
    void onSuccessFromRemoteDatabase(User user);
    void onSuccessFromRemoteDatabase(List<Game> gameList);
    void onFailureFromRemoteDatabase(String message);
}
