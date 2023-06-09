package com.gamebuzz.util;

public class Constants
{
    // Constants for managing errors
    public static final String RETROFIT_ERROR = "retrofit_error";
    public static final String API_KEY_ERROR = "api_key_error";
    public static final String UNEXPECTED_ERROR = "unexpected_error";
    public static final String INVALID_USER_ERROR = "invalidUserError";
    public static final String INVALID_CREDENTIALS_ERROR = "invalidCredentials";
    public static final String USER_COLLISION_ERROR = "userCollisionError";
    public static final String WEAK_PASSWORD_ERROR = "passwordIsWeak";

    public static final int MINIMUM_PASSWORD_LENGTH = 6;

    // Constants for Room database
    public static final String GAME_DATABASE_NAME = "game_db";
    public static final int DATABASE_VERSION = 1;

    public static final String SERIALIZE_GENRE_NAME = "genre_name";
    public static final String SERIALIZE_PLATFORM_NAME = "platform_name";
    public static final String SERIALIZE_SCREENSHOT_URL = "screenshot_url";
    public static final String SERIALIZE_THEME_NAME = "theme_name";
    public static final String SERIALIZE_DATE_NAME = "date_name";

    // Constants for Room database

    public static final String GAME_API_BASE_URL = "https://api.igdb.com/v4/";
    public static final String GAME_API_GAME_ENDPOINT = "games";

    // Constants for Firebase Realtime Database

    public static final String FIREBASE_REALTIME_DATABASE = "https://gamebuzz-6c020-default-rtdb.europe-west1.firebasedatabase.app/";
    public static final String FIREBASE_USERS_COLLECTION = "users";
    public static final String FIREBASE_FAVORITE_GAMES_COLLECTION = "favorite_games";

    // Constants for EncryptedSharedPreferences
    public static final String ENCRYPTED_SHARED_PREFERENCES_FILE_NAME = "com.gamebuzz.encrypted_preferences";
    public static final String EMAIL_ADDRESS = "email_address";
    public static final String PASSWORD = "password";
    public static final String ID_TOKEN = "google_token";

    // Constants for SharedPreferences
    public static final String SHARED_PREFERENCES_FILE_NAME = "com.gamebuzz.preferences";
    public static final String SHARED_PREFERENCES_FIRST_LOADING = "first_loading";

}
