package com.gamebuzz.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Objects;

public class GameScreenshot implements Parcelable {

    private String url;

    public GameScreenshot() {}

    public GameScreenshot(String url) { this.url = url; }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }

    public int describeContents() { return 0; }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.url);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GameScreenshot that = (GameScreenshot) o;
        return Objects.equals(url, that.url);
    }

    @Override
    public int hashCode() {
        return Objects.hash(url);
    }

    public static final Parcelable.Creator<GameScreenshot> CREATOR = new Parcelable.Creator<GameScreenshot>() {
        @Override
        public GameScreenshot createFromParcel(Parcel source) {
            return new GameScreenshot();
        }

        @Override
        public GameScreenshot[] newArray(int size) {
            return new GameScreenshot[size];
        }
    };
}
