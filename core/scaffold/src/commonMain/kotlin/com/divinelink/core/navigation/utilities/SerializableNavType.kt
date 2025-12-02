package com.divinelink.core.navigation.utilities

import androidx.navigation.NavType
import androidx.savedstate.SavedState
import androidx.savedstate.read
import androidx.savedstate.write
import kotlinx.serialization.KSerializer
import kotlinx.serialization.json.Json
import kotlinx.serialization.serializer

inline fun <reified T : Any> serializableNavType(): NavType<T> = SerializableNavType(
  serializer = serializer<T>(),
  isNullableAllowed = false,
)

class SerializableNavType<T : Any>(
  private val serializer: KSerializer<T>,
  isNullableAllowed: Boolean = false,
) : NavType<T>(isNullableAllowed = isNullableAllowed) {

  override fun get(
    bundle: SavedState,
    key: String,
  ): T? = bundle.read { parseValue(getString(key)) }

  override fun put(
    bundle: SavedState,
    key: String,
    value: T,
  ) {
    bundle.write { putString(key, Json.encodeToString(serializer, value)) }
  }

  override fun parseValue(value: String): T = Json.decodeFromString(serializer, value)

  override fun serializeAsValue(value: T): String = Json.encodeToString(serializer, value)
}
