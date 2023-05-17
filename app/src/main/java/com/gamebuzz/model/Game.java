package com.gamebuzz.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.ColumnInfo;
import androidx.room.Embedded;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import com.gamebuzz.util.Converters;
import com.google.firebase.database.Exclude;
import com.google.gson.annotations.SerializedName;

import java.util.List;
import java.util.Objects;

@Entity
public class Game implements Parcelable {

    @PrimaryKey(autoGenerate = true)
    private long id;

    private int gameId;

    @SerializedName("name")
    private String title;

    @Embedded(prefix = "cover_")
    @SerializedName("cover")
    private GameCover cover;

    @TypeConverters(Converters.class)
    @SerializedName("genres")
    private List<GameGenre> gameGenre;

    @TypeConverters(Converters.class)
    @SerializedName("platforms")
    private List<GamePlatform> gamePlatform;

    @TypeConverters(Converters.class)
    @SerializedName("release_dates")
    private List<GameDate> releaseDates;

    @TypeConverters(Converters.class)
    @SerializedName("screenshots")
    private List<GameScreenshot> screenshotList;

    @TypeConverters(Converters.class)
    @SerializedName("themes")
    private List<GameTheme> gameThemeList;

    private String summary;

    @ColumnInfo(name="is_favorite")
    private boolean isFavorite;

    @ColumnInfo(name = "is_synchronized")
    private boolean isSynchronized;

    public Game() {}

    public Game(int gameId, String title, GameCover cover, List<GameGenre> gameGenre,
                List<GamePlatform> gamePlatform, List<GameDate> gameDates, List<GameScreenshot> screenshotList,
                List<GameTheme> gameThemeList, String summary, boolean isFavorite, boolean isSynchronized) {
        this.gameId = gameId;
        this.title = title;
        this.cover = cover;
        this.gameGenre = gameGenre;
        this.gamePlatform = gamePlatform;
        this.releaseDates = gameDates;
        this.screenshotList = screenshotList;
        this.gameThemeList = gameThemeList;
        this.summary = summary;
        this.isFavorite = isFavorite;
        this.isSynchronized = isSynchronized;
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
    public void setGamePlatform(List<GamePlatform> gamePlatform) { this.gamePlatform = gamePlatform; }
    public void setReleaseDates(List<GameDate> gameDateList) {this.releaseDates = gameDateList; }
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
    public List<GamePlatform> getGamePlatform() { return gamePlatform; }
    public List<GameDate> getReleaseDates() { return releaseDates; }
    public List<GameScreenshot> getScreenshotList() { return screenshotList; }
    public List<GameTheme> getGameThemeList() { return gameThemeList; }
    public boolean getFavorite() { return isFavorite; }

    @Exclude
    public boolean isSynchronized() { return isSynchronized; }
    public void setSynchronized(boolean aSynchronized) { isSynchronized = aSynchronized; }

    @Override
    public String toString() {
        return "Game{" +
                "id=" + id +
                ", gameId='" + gameId + '\'' +
                ", title='" + title + '\'' +
                ", cover='" + cover + '\'' +
                ", gameGenre='" + gameGenre + '\'' +
                ", gamePlatform='" + gamePlatform + '\'' +
                ", date='" + releaseDates + '\'' +
                ", screenshot='" + screenshotList + '\'' +
                ", themes='" + gameThemeList + '\'' +
                ", summary='" + summary + '\'' +
                ", isFavorite=" + isFavorite +
                ", isSynchronized=" + isSynchronized +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Game game = (Game) o;
        return Objects.equals(gameId, game.gameId) && Objects.equals(title, game.title) && Objects.equals(cover, game.cover) &&
                Objects.equals(gameGenre, game.gameGenre) && Objects.equals(gamePlatform, game.gamePlatform) &&
                 Objects.equals(releaseDates, game.releaseDates) && Objects.equals(screenshotList, game.screenshotList)
                && Objects.equals(gameThemeList, game.gameThemeList) && Objects.equals(summary, game.summary);
    }

    @Override
    public int hashCode() {
        return Objects.hash(gameId, title, cover, gameGenre, gamePlatform, releaseDates, screenshotList, gameThemeList, summary);
    }

    public int describeContents() { return 0; }

    public void writeToParcel(Parcel dest, int flags) {
        // TODO: this

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
