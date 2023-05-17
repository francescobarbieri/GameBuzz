package com.gamebuzz.util;

import static com.gamebuzz.util.Constants.API_KEY_ERROR;
import static com.gamebuzz.util.Constants.RETROFIT_ERROR;

import android.app.Application;

public class ErrorMessagesUtil {

    private Application application;

    public ErrorMessagesUtil(Application application) { this.application = application; }

    public String getErrorMessage(String errorType) {
        switch (errorType) {
            case RETROFIT_ERROR:
                return "Error in retrieving the news";
            case API_KEY_ERROR:
                return "API not valid";
            default:
                return "Unexpected error";
        }
    }
}
