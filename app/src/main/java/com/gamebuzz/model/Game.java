package com.gamebuzz.model;

import android.os.Parcel;
import android.os.Parcelable;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

import java.util.List;

@Entity
public class Game implements Parcelable {

    private int id;
    private String title;
    private GameCover cover;
    private List<GameGenre> gameGenre;
    private GameCompany gameCompany;
    private List<GamePlatform> gamePlatform;
    private GameDate releaseDate;
    private List<GameScreenshot> screenshotList;
    private List<GameTheme> gameThemeList;
    private String summary;

    public Game() {}

    public Game(String name, int id) {
        this.title = name;
        this.id  = id;
    }

    // Setters
    public void setId(int id) {
        this.id = id;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public void setCover(GameCover cover) { this.cover = cover; }
    public void setGameGenre(List<GameGenre> gameGenre) { this.gameGenre = gameGenre; }
    public void setGameCompany(GameCompany gameCompany) { this.gameCompany = gameCompany; }
    public void setGamePlatform(List<GamePlatform> gamePlatform) { this.gamePlatform = gamePlatform; }
    public void setReleaseDate(GameDate date) { this.releaseDate = date; }
    public void setScreenshotList(List<GameScreenshot> screenshotList) { this.screenshotList = screenshotList; }
    public void setGameThemeList(List<GameTheme> gameThemeList) { this.gameThemeList = gameThemeList; }
    public void setSummary(String summary) { this.summary = summary; }

    // Getters
    public int getId() { return id; }
    public String getSummary() { return summary; }
    public String getTitle() {
        return title;
    }
    public GameCover getCover() { return cover; }
    public List<GameGenre> getGameGenre() { return gameGenre; }
    public GameCompany getGameCompany() { return gameCompany; }
    public List<GamePlatform> getGamePlatform() { return gamePlatform; }
    public GameDate getReleaseDate() { return releaseDate; }
    public List<GameScreenshot> getScreenshotList() { return screenshotList; }
    public List<GameTheme> getGameThemeList() { return gameThemeList; }
    public int describeContents() { return 0; }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.title);
    }

    public static final Parcelable.Creator<Game> CREATOR = new Parcelable.Creator<Game>() {
        @Override
        public Game createFromParcel(Parcel source) {
            return new Game();
        }

        @Override
        public Game[] newArray(int size) {
            return new Game[size];
        }
    };
}
