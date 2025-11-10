package com.moltenwolfcub.impostor;

import android.os.Parcel;
import android.os.ParcelUuid;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Category implements Parcelable {
    public final UUID id;
    private String name;
    public List<String> words;

    public Category(String name) {
        this.id = UUID.randomUUID();
        this.name = name;
        this.words = new ArrayList<>();
    }

    protected Category(Parcel in) {
        id = ((ParcelUuid)in.readParcelable(ParcelUuid.class.getClassLoader())).getUuid();
        name = in.readString();
        words = in.createStringArrayList();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void addWord(String word) {
        this.words.add(word);
    }

    @Override
    public void writeToParcel(@NonNull Parcel parcel, int flags) {
        parcel.writeParcelable(new ParcelUuid(id), flags);
        parcel.writeString(name);
        parcel.writeStringList(words);
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
