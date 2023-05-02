package com.gamebuzz.model;

import android.os.Parcel;
import android.os.Parcelable;

public class GameCompany implements Parcelable {

    private String company;

    public GameCompany(){}

    public GameCompany(String company){ this.company = company; }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getCompany() {
        return company;
    }

    public int describeContents() { return 0; }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.company);
    }

    public static final Parcelable.Creator<GameCompany> CREATOR = new Parcelable.Creator<GameCompany>() {
        @Override
        public GameCompany createFromParcel(Parcel source) {
            return new GameCompany();
        }

        @Override
        public GameCompany[] newArray(int size) {
            return new GameCompany[size];
        }
    };
}
