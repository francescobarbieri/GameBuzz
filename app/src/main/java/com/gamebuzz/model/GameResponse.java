package com.gamebuzz.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

public class GameResponse implements Parcelable {

    private boolean isLoading;

    // TODO: @SerializedName("articles") se serve
    private List<Game> gameList;

    public GameResponse() {}

    public GameResponse(List<Game> gameList) { this.gameList = gameList; }

    public List<Game> getGameList() { return gameList; }

    public void setGameList(List<Game> gameList) { this.gameList = gameList; }

    public boolean isLoading() { return isLoading; }

    public void setLoading(boolean loading) { this.isLoading = loading; }

    @Override
    public String toString() {
        return "GameResponse{" +
                "gameList=" + gameList +
                "}";
    }

    @Override
    public int describeContents() { return 0; }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeByte(this.isLoading ? (byte) 1 : (byte) 0);
        dest.writeTypedList(this.gameList);
    }

    public void readFromParcel(Parcel source) {
        this.isLoading = source.readByte() != 0;
        this.gameList = source.createTypedArrayList(Game.CREATOR);
    }

    protected GameResponse(Parcel in) {
        this.isLoading = in.readByte() != 0;
        this.gameList = in.createTypedArrayList(Game.CREATOR);
    }

    public static final Parcelable.Creator<GameResponse> CREATOR = new Parcelable.Creator<GameResponse>() {
        @Override
        public GameResponse createFromParcel(Parcel source) { return new GameResponse(source); }

        @Override
        public GameResponse[] newArray(int size) {return new GameResponse[size]; }
    };

}
