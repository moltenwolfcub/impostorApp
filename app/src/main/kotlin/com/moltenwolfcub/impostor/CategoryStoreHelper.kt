package com.moltenwolfcub.impostor

import android.content.Context
import com.moltenwolfcub.impostor.protos.CategoryList
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

object CategoryStoreHelper {

    @JvmStatic
    fun readCategories(context: Context): CategoryList = runBlocking {
        CategoryStore.get(context).data.first()
    }

    @JvmStatic
    fun writeCategories(context: Context, categories: CategoryList) {
        CoroutineScope(Dispatchers.IO).launch {
            CategoryStore.get(context).updateData { categories }
        }
    }

    @JvmStatic
    fun toProto(categories: List<Category>): CategoryList {
        val builder = CategoryList.newBuilder()
        categories.forEach { c ->
            val categoryBuilder = com.moltenwolfcub.impostor.protos.Category.newBuilder()
                .setId(c.id.toString())
                .setName(c.name)
                .addAllWords(c.words)
            builder.addCategories(categoryBuilder)
        }
        return builder.build()
    }
}