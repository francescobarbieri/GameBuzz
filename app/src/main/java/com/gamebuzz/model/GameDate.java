package com.gamebuzz.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.Objects;

public class GameDate implements Parcelable {

    @SerializedName("human")
    public String human;

    public GameDate() {}

    public GameDate(String human) { this.human = human; }

    public void setDate(String human) {
        this.human = human;
    }

    public String getDate() {
        return human;
    }

    public int describeContents() { return 0; }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.human);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GameDate that = (GameDate) o;
        return Objects.equals(human, that.human);
    }

    @Override
    public int hashCode() {
        return Objects.hash(human);
    }

    public static final Parcelable.Creator<GameDate> CREATOR = new Parcelable.Creator<GameDate>() {
        @Override
        public GameDate createFromParcel(Parcel source) {
            return new GameDate();
        }

        @Override
        public GameDate[] newArray(int size) {
            return new GameDate[size];
        }
    };
}
