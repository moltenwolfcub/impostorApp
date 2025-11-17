package com.moltenwolfcub.impostor;

import android.content.Context;

import com.moltenwolfcub.impostor.protos.CategoryList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class CategoryDefaults {

    public static List<Category> loadDefaults(Context context) {
        byte[] buffer;
        try (InputStream stream = context.getAssets().open("defaultCategories.json")) {
            buffer = new byte[stream.available()];
            int read = stream.read(buffer);
            if (read != stream.available()) {
                throw new IOException("Failed to read the whole json file");
            }
            stream.close();

            String jsonString = new String(buffer, StandardCharsets.UTF_8);
            JSONArray array = new JSONArray(jsonString);

            List<Category> categoryList = new ArrayList<>();
            for (int i = 0; i < array.length(); i++) {
                JSONObject obj = array.getJSONObject(i);

                String id = obj.getString("id");
                String name = obj.getString("name") + " [D]";

                Category category = new Category(name, UUID.fromString(id));

                JSONArray jsonWords = obj.getJSONArray("words");
                for (int wordIndex = 0; wordIndex < jsonWords.length(); wordIndex++) {
                    category.addWord(jsonWords.getString(wordIndex));
                }

                categoryList.add(category);
            }
            return categoryList;

        } catch (IOException | JSONException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    public static void restoreDefaults(Context context) {
        CategoryList storedCategories = CategoryStoreHelper.readCategories(context);
        List<Category> currentList = ProtoConversions.fromProto(storedCategories);
        List<Category> defaultList = loadDefaults(context);

        for (Category d : defaultList) {
            Category existing = null;
            for (Category c : currentList) {
                if (c.id.equals(d.id)) {
                    existing = c;
                }
            }

            if (existing == null) {
                currentList.add(d);
            } else {
                for (String word : d.words) {
                    if (!existing.words.contains(word)) {
                        existing.words.add(word);
                    }
                }
            }
        }
        CategoryStoreHelper.writeCategories(context, ProtoConversions.toProto(currentList));
    }
}
