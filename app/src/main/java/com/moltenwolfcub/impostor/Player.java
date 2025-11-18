package com.moltenwolfcub.impostor;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

public class Player implements Parcelable {
    public NameItem name;
    public boolean isImposter;
    public boolean hasBeenViewed = false;

    public Player(NameItem name) {
        this.name = name;
    }

    protected Player(Parcel in) {
        name = in.readParcelable(NameItem.class.getClassLoader());
        isImposter = in.readBoolean();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeParcelable(name, flags);
        dest.writeBoolean(isImposter);
    }

    public static final Creator<Player> CREATOR = new Creator<Player>() {
        @Override
        public Player createFromParcel(Parcel in) {
            return new Player(in);
        }

        @Override
        public Player[] newArray(int size) {
            return new Player[size];
        }
    };
}
