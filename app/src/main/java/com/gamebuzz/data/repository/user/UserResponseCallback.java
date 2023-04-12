package com.gamebuzz.data.repository.user;

import com.gamebuzz.model.User;

public interface UserResponseCallback {
    void onSuccessFromAuthentication(User user);
    void onFailureFromAuthentication(String message);
    void onSuccessLogout();
}
