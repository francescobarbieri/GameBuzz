package com.gamebuzz.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

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
