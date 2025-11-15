package com.moltenwolfcub.impostor;

import android.content.Context;

import androidx.datastore.core.DataStore;
import androidx.datastore.core.DataStoreFactory;

import com.moltenwolfcub.impostor.protos.CategoryList;

import java.io.File;

public class CategoryStore {
    private static final String FILE_NAME = "categories.pb";
    private static volatile DataStore<CategoryList> STORE;
    public static DataStore<CategoryList> get(Context context) {
        if (STORE == null) {
            synchronized (CategoryStore.class) {
                if (STORE == null) {
                    STORE = DataStoreFactory.INSTANCE.create(
                            CategoryListSerializer.INSTANCE,
                            () -> new File(context.getFilesDir(), FILE_NAME)
                    );
                }
            }
        }
        return STORE;
    }
}