package com.gamebuzz.data.database;

import static com.gamebuzz.util.Constants.DATABASE_VERSION;
import static com.gamebuzz.util.Constants.GAME_DATABASE_NAME;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.gamebuzz.model.Game;
import com.gamebuzz.util.Converters;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {Game.class}, version = DATABASE_VERSION)
@TypeConverters({Converters.class})
public abstract class GameRoomDatabase extends RoomDatabase {

    public abstract GameDao gameDao();

    public static volatile GameRoomDatabase INSTANCE;
    private static final int NUMBER_OF_THREADS = Runtime.getRuntime().availableProcessors();

    public static final ExecutorService databaseWriteExecutor = Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    public static GameRoomDatabase getDatabase(final Context context) {
        if(INSTANCE == null) {
            synchronized (GameRoomDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(), GameRoomDatabase.class, GAME_DATABASE_NAME).build();
                }
            }
        }
        return INSTANCE;
    }
}
