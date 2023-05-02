package com.gamebuzz.model;

import android.os.Parcel;
import android.os.Parcelable;

public class GameDate implements Parcelable {

    public String date;

    public GameDate() {}

    public GameDate(String date) { this.date = date; }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDate() {
        return date;
    }

    public int describeContents() { return 0; }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.date);
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
