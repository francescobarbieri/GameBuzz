package com.gamebuzz.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Objects;

public class GameGenre implements Parcelable {

    private String name;

    public GameGenre() {}

    public GameGenre(String name) { this.name = name; }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() { return name; }

    public int describeContents() { return 0; }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.name);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GameGenre that = (GameGenre) o;
        return Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    public static final Parcelable.Creator<GameGenre> CREATOR = new Parcelable.Creator<GameGenre>() {
        @Override
        public GameGenre createFromParcel(Parcel source) {
            return new GameGenre();
        }

        @Override
        public GameGenre[] newArray(int size) {
            return new GameGenre[size];
        }
    };
}
