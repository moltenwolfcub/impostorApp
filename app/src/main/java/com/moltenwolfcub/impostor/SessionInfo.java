package com.moltenwolfcub.impostor;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

public class SessionInfo implements Parcelable {
    public List<NameItem> currentPlayers;
    public List<Category> categories;

    public SessionInfo() {
        currentPlayers = new ArrayList<>();
        currentPlayers.add(new NameItem("Player 1"));
        currentPlayers.add(new NameItem("Player 2"));
        currentPlayers.add(new NameItem("Player 3"));


        categories = new ArrayList<>();

        Category food = new Category("Food");
        food.addWord("Apple");
        food.addWord("Roast Chicken");
        food.addWord("Shortbread");
        Category houseItems = new Category("Household Items");
        houseItems.addWord("Sofa");
        houseItems.addWord("Blender");

        categories.add(food);
        categories.add(houseItems);
    }

    protected SessionInfo(Parcel in) {
        currentPlayers = in.createTypedArrayList(NameItem.CREATOR);
        categories = in.createTypedArrayList(Category.CREATOR);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeTypedList(currentPlayers);
        dest.writeTypedList(categories);
    }

    public static final Creator<SessionInfo> CREATOR = new Creator<SessionInfo>() {
        @Override
        public SessionInfo createFromParcel(Parcel in) {
            return new SessionInfo(in);
        }

        @Override
        public SessionInfo[] newArray(int size) {
            return new SessionInfo[size];
        }
    };
}
