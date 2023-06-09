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

    public static final class GameResponseSuccess extends Result {
        private final GameApiResponse gameResponse;

        public GameResponseSuccess(GameApiResponse gameResponse) { this.gameResponse = gameResponse; }
        public GameApiResponse getData() { return gameResponse; }
    }


    public static final class Error extends Result {
        private final String message;
        public Error(String message) {
            this.message = message;
        }
        public String getMessage() {
            return message;
        }
    }
}
