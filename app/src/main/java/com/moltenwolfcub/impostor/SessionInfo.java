package com.moltenwolfcub.impostor;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import com.moltenwolfcub.impostor.protos.CategoryList;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class SessionInfo implements Parcelable {
    public List<NameItem> currentPlayers;
    public List<Category> categories;

    public SessionInfo(Context context) {
        currentPlayers = new ArrayList<>();
        currentPlayers.add(new NameItem("Player 1"));
        currentPlayers.add(new NameItem("Player 2"));
        currentPlayers.add(new NameItem("Player 3"));


        categories = new ArrayList<>();

        CategoryList categoryList = CategoryStoreHelper.readCategories(context);

        for (com.moltenwolfcub.impostor.protos.Category categoryProto : categoryList.getCategoriesList()) {
            Category category = new Category(categoryProto.getName(), UUID.fromString(categoryProto.getId()));
            category.words.addAll(categoryProto.getWordsList());
            categories.add(category);
        }
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
