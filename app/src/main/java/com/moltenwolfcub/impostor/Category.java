package com.moltenwolfcub.impostor;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

public class Category implements Parcelable {
    private String name;

    public Category(String name) {
        this.name = name;
    }

    protected Category(Parcel in) {
        name = in.readString();
    }

    public String getName() {
        return name;
    }

    public void SetName(String name) {
        this.name = name;
    }

    @Override
    public void writeToParcel(@NonNull Parcel parcel, int i) {
        parcel.writeString(name);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Category> CREATOR = new Creator<Category>() {
        @Override
        public Category createFromParcel(Parcel in) {
            return new Category(in);
        }

        @Override
        public Category[] newArray(int size) {
            return new Category[size];
        }
    };
}
