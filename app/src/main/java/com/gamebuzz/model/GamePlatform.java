package com.gamebuzz.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Objects;

public class GamePlatform implements Parcelable {

    private String name;

    public GamePlatform(){}

    public GamePlatform(String name) { this.name = name; }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int describeContents() { return 0; }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.name);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GamePlatform that = (GamePlatform) o;
        return Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    public static final Parcelable.Creator<GamePlatform> CREATOR = new Parcelable.Creator<GamePlatform>() {
        @Override
        public GamePlatform createFromParcel(Parcel source) {
            return new GamePlatform();
        }

        @Override
        public GamePlatform[] newArray(int size) {
            return new GamePlatform[size];
        }
    };
}
