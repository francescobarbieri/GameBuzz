package com.gamebuzz.data.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.gamebuzz.model.Game;

import java.util.List;

@Dao
public interface GameDao {

    @Query("SELECT * FROM game")
    List<Game> getAll();

    @Query("SELECT * FROM game WHERE id = :id")
    Game getGame(long id);

    @Query("SELECT * FROM game WHERE is_favorite = 1")
    List<Game> getFavoriteGame();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    List<Long> insertGameList(List<Game> gameList);

    @Insert
    void insertAll(Game... game);

    @Delete
    void delete(Game game);

    @Delete
    void deleteAllWithoutQuery(Game... game);

    @Query("DELETE FROM game")
    int deleteAll();

    @Query("DELETE FROM game WHERE is_favorite = 0")
    void deleteNotFavoriteGames();

    @Update
    int updateSingleFavoriteGame(Game game);

}
