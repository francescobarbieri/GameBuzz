package com.gamebuzz.model;

public class Result {
    private Result() {}

    public boolean isSuccess () {
        if(this instanceof UserResponseSuccess) {
            return true;
        } else {
            return false;
        }
    }

    public static final class UserResponseSuccess extends Result  {
        private final User user;
        public UserResponseSuccess (User user) {
            this.user = user;
        }
        public User getData() {
            return user;
        }
    }
}
