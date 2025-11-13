package com.moltenwolfcub.impostor;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

public class NameItem implements Parcelable {
    private String name;

    public NameItem(String name) {
        this.name = name;
    }

    protected NameItem(Parcel in) {
        name = in.readString();
    }

    public String getName() {
        return name;
    }

    public void SetName(String name) {
        this.name = name;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeString(name);
    }

    public static final Creator<NameItem> CREATOR = new Creator<NameItem>() {
        @Override
        public NameItem createFromParcel(Parcel in) {
            return new NameItem(in);
        }

        @Override
        public NameItem[] newArray(int size) {
            return new NameItem[size];
        }
    };
}
