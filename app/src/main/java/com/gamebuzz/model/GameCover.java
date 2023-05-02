package com.gamebuzz.model;

import android.os.Parcel;
import android.os.Parcelable;

public class GameCover implements Parcelable {

    private int id;
    private String url;

    public GameCover() {}

    public GameCover(int id, String url) {
        this.id = id;
        this.url = url;
    }

    public int getId() {
        return id;
    }

    public String getUrl() {
        return url;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int describeContents() { return 0; }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.url);
    }

    public static final Parcelable.Creator<GameCover> CREATOR = new Parcelable.Creator<GameCover>() {
        @Override
        public GameCover createFromParcel(Parcel source) {
            return new GameCover();
        }

        @Override
        public GameCover[] newArray(int size) {
            return new GameCover[size];
        }
    };
}
