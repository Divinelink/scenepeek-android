package com.divinelink.core.navigation.utilities

import android.os.Bundle
import androidx.navigation.NavType
import com.divinelink.core.model.media.MediaItem
import kotlinx.serialization.json.Json

val MediaItemParameterType = object : NavType<MediaItem>(
  isNullableAllowed = false,
) {
  override fun get(
    bundle: Bundle,
    key: String,
  ): MediaItem? = bundle.getString(key)?.let { parseValue(it) }

  override fun put(
    bundle: Bundle,
    key: String,
    value: MediaItem,
  ) {
    bundle.putString(key, serializeAsValue(value))
  }

  override fun parseValue(value: String): MediaItem = Json.decodeFromString(value)

  override fun serializeAsValue(value: MediaItem): String = Json.encodeToString(value)
}
