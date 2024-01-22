package com.esaudev.expensetracker.data.proto

import android.content.Context
import androidx.datastore.core.CorruptionException
import androidx.datastore.core.DataStore
import androidx.datastore.core.Serializer
import androidx.datastore.dataStore
import com.esaudev.expensetracker.proto.UserModel
import com.google.protobuf.InvalidProtocolBufferException
import java.io.InputStream
import java.io.OutputStream

object UserModelSerializer : Serializer<UserModel> {
    override val defaultValue: UserModel
        get() = UserModel.getDefaultInstance()

    override suspend fun readFrom(input: InputStream): UserModel {
        try {
            return UserModel.parseFrom(input)
        } catch (e: InvalidProtocolBufferException) {
            throw CorruptionException("Cannot read proto.", e)
        }
    }

    override suspend fun writeTo(t: UserModel, output: OutputStream) {
        t.writeTo(output)
    }
}

val Context.userModelDataStore: DataStore<UserModel> by dataStore(
    fileName = "user_model.pb",
    serializer = UserModelSerializer
)
