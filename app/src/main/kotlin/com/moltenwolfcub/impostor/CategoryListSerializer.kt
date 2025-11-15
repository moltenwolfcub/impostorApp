package com.moltenwolfcub.impostor

import androidx.datastore.core.Serializer
import com.google.protobuf.InvalidProtocolBufferException
import com.moltenwolfcub.impostor.protos.CategoryList
import java.io.InputStream
import java.io.OutputStream

object CategoryListSerializer : Serializer<CategoryList> {
    override val defaultValue: CategoryList
        get() = CategoryList.getDefaultInstance()

    override suspend fun readFrom(input: InputStream): CategoryList {
        return try {
            CategoryList.parseFrom(input)
        } catch (_: InvalidProtocolBufferException) {
            defaultValue
        }
    }

    override suspend fun writeTo(t: CategoryList, output: OutputStream) {
        t.writeTo(output)
    }
}