package com.gamebuzz.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

public class GameApiResponse implements Parcelable {

    private List<Game> games;

    public GameApiResponse() { }

    public GameApiResponse( List<Game> games) {
        this.games = games;
    }

    public List<Game> getGamesList() {
        return games;
    }

    public void setGamesList(List<Game> games) {
        this.games = games;
    }

    @Override
    public String toString() {
        return "GameApiResponse{" +
                "games=" + games +
                "}";
    }

    @Override
    public int describeContents() { return 0; }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(games);
    }

    public void readFromParcel(Parcel source) {
        this.games = source.createTypedArrayList(Game.CREATOR);
    }

    protected GameApiResponse(Parcel in) {
        this.games = in.createTypedArrayList(Game.CREATOR);
    }

    public static final Creator<GameApiResponse> CREATOR = new Creator<GameApiResponse>() {
        @Override
        public GameApiResponse createFromParcel(Parcel source) {
            return new GameApiResponse(source);
        }

        @Override
        public GameApiResponse[] newArray(int size) {
            return new GameApiResponse[size];
        }
    };


}
