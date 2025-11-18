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
    public List<Word> words;
    public transient boolean enabled;

    public Category(String name) {
        this(name, UUID.randomUUID());
    }

    public Category(String name, UUID id) {
        this.id = id;
        this.name = name;
        this.words = new ArrayList<>();
        this.enabled = false;
    }

    protected Category(Parcel in) {
        id = ((ParcelUuid)in.readParcelable(ParcelUuid.class.getClassLoader())).getUuid();
        name = in.readString();
        enabled = in.readBoolean();
        words = in.createTypedArrayList(Word.CREATOR);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void addWord(String word) {
        this.words.add(new Word(word, name));
    }

    @Override
    public void writeToParcel(@NonNull Parcel parcel, int flags) {
        parcel.writeParcelable(new ParcelUuid(id), flags);
        parcel.writeString(name);
        parcel.writeBoolean(enabled);
        parcel.writeTypedList(words, flags);
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
