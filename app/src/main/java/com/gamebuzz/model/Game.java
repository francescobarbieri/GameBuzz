package com.gamebuzz.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.ColumnInfo;
import androidx.room.Embedded;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import com.gamebuzz.util.Converters;

import java.util.List;
import java.util.Objects;

@Entity
public class Game implements Parcelable {

    @PrimaryKey(autoGenerate = true)
    private long id;

    private int gameId;
    private String title;
    @Embedded(prefix = "cover_")
    private GameCover cover;
    @TypeConverters(Converters.class)
    private List<GameGenre> gameGenre;
    @Embedded(prefix = "gameCompany_")
    private GameCompany gameCompany;
    @TypeConverters(Converters.class)
    private List<GamePlatform> gamePlatform;
    @Embedded(prefix = "gameDate_")
    private GameDate releaseDate;
    @TypeConverters(Converters.class)
    private List<GameScreenshot> screenshotList;
    @TypeConverters(Converters.class)
    private List<GameTheme> gameThemeList;
    private String summary;

    @ColumnInfo(name="is_favorite")
    private boolean isFavorite;

    public Game() {}

    public Game(int gameId, String title, GameCover cover, List<GameGenre> gameGenre, GameCompany gameCompany,
                List<GamePlatform> gamePlatform, GameDate gameDate, List<GameScreenshot> screenshotList,
                List<GameTheme> gameThemeList, String summary, boolean isFavorite) {
        this.gameId = gameId;
        this.title = title;
        this.cover = cover;
        this.gameGenre = gameGenre;
        this.gameCompany = gameCompany;
        this.gamePlatform = gamePlatform;
        this.releaseDate = gameDate;
        this.screenshotList = screenshotList;
        this.gameThemeList = gameThemeList;
        this.summary = summary;
        this.isFavorite = isFavorite;
    }

    public Game(String title) {
        this.title = title;
    }

    // Setters
    public void setId(long id) { this.id = id; }
    public void setGameId(int gameId) {
        this.gameId = gameId;
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
    public void setFavorite(boolean favorite) { isFavorite = favorite; }

    // Getters
    public long getId() { return id; }
    public int getGameId() { return gameId; }
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
    public boolean getFavorite() { return isFavorite; }


    @Override
    public String toString() {
        return "Game{" +
                "id=" + id +
                ", gameId='" + gameId + '\'' +
                ", title='" + title + '\'' +
                ", cover='" + cover + '\'' +
                ", gameGenre='" + gameGenre + '\'' +
                ", gamePlatform='" + gamePlatform + '\'' +
                ", gameCompany=" + gameCompany +
                ", date='" + releaseDate + '\'' +
                ", screenshot='" + screenshotList + '\'' +
                ", themes='" + gameThemeList + '\'' +
                ", summary='" + summary + '\'' +
                ", isFavorite=" + isFavorite +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Game game = (Game) o;
        return Objects.equals(gameId, game.gameId) && Objects.equals(title, game.title) && Objects.equals(cover, game.cover) &&
                Objects.equals(gameGenre, game.gameGenre) && Objects.equals(gamePlatform, game.gamePlatform) &&
                Objects.equals(gameCompany, game.gameCompany)&& Objects.equals(releaseDate, game.releaseDate) && Objects.equals(screenshotList, game.screenshotList)
                && Objects.equals(gameThemeList, game.gameThemeList) && Objects.equals(summary, game.summary);
    }

    @Override
    public int hashCode() {
        return Objects.hash(gameId, title, cover, gameGenre, gamePlatform,gameCompany, releaseDate, screenshotList, gameThemeList, summary);
    }

    public int describeContents() { return 0; }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.id);
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
