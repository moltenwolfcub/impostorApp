package com.moltenwolfcub.impostor;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

public class Word implements Parcelable {
    public String contents;
    public String categoryName;

    public Word(String contents, String categoryName) {
        this.contents = contents;
        this.categoryName = categoryName;
    }

    protected Word(Parcel in) {
        this.contents = in.readString();
        this.categoryName = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel parcel, int flags) {
        parcel.writeString(contents);
        parcel.writeString(categoryName);
    }

    public static final Creator<Word> CREATOR = new Creator<Word>() {
        @Override
        public Word createFromParcel(Parcel in) {
            return new Word(in);
        }

        @Override
        public Word[] newArray(int size) {
            return new Word[size];
        }
    };
}
