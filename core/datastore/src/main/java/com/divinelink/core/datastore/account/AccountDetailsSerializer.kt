package com.divinelink.core.datastore.account

import androidx.datastore.core.CorruptionException
import androidx.datastore.core.Serializer
import com.divinelink.core.datastore.AccountDetailsProto
import com.google.protobuf.InvalidProtocolBufferException
import java.io.InputStream
import java.io.OutputStream

object AccountDetailsSerializer : Serializer<AccountDetailsProto> {
  override val defaultValue: AccountDetailsProto = AccountDetailsProto.getDefaultInstance()

  override suspend fun readFrom(input: InputStream): AccountDetailsProto {
    try {
      return AccountDetailsProto.parseFrom(input)
    } catch (exception: InvalidProtocolBufferException) {
      throw CorruptionException("Cannot read proto.", exception)
    }
  }

  override suspend fun writeTo(
    t: AccountDetailsProto,
    output: OutputStream,
  ) {
    t.writeTo(output)
  }
}
