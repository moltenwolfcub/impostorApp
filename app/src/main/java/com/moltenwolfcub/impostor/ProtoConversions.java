package com.moltenwolfcub.impostor;

import com.moltenwolfcub.impostor.protos.CategoryList;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ProtoConversions {

    public static CategoryList toProto(List<Category> categories) {
        CategoryList.Builder builder = CategoryList.newBuilder();
        for (Category c : categories) {
            com.moltenwolfcub.impostor.protos.Category.Builder categoryBuilder = com.moltenwolfcub.impostor.protos.Category.newBuilder()
                    .setId(c.id.toString())
                    .setName(c.getName())
                    .addAllWords(c.words);
            builder.addCategories(categoryBuilder);
        }
        return builder.build();
    }

    public static List<Category> fromProto(CategoryList list) {
        List<Category> categories = new ArrayList<>();
        for (com.moltenwolfcub.impostor.protos.Category c : list.getCategoriesList()) {
            Category newCategory = new Category(c.getName(), UUID.fromString(c.getId()));
            for (String word : c.getWordsList()) {
                newCategory.addWord(word);
            }
            categories.add(newCategory);
        }
        return categories;
    }
}
