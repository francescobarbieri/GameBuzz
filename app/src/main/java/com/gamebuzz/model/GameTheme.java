package com.gamebuzz.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Objects;

public class GameTheme implements Parcelable {

    private String name;

    public GameTheme(){}
    public GameTheme(String name){ this.name = name; }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public int describeContents() { return 0; }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.name);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GameTheme that = (GameTheme) o;
        return Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    public static final Parcelable.Creator<GameTheme> CREATOR = new Parcelable.Creator<GameTheme>() {
        @Override
        public GameTheme createFromParcel(Parcel source) {
            return new GameTheme();
        }

        @Override
        public GameTheme[] newArray(int size) {
            return new GameTheme[size];
        }
    };
}
