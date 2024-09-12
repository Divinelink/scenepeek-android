package com.divinelink.core.network.changes.model.api

import com.divinelink.core.network.changes.model.serializer.ChangeApiSerializer
import kotlinx.serialization.Serializable

@Serializable(with = ChangeApiSerializer::class)
data class ChangeApi(
  val key: String,
  val items: List<ChangeItemApi>,
) {
  companion object {
    object JsonKeys {
      const val KEY = "key"
      const val ITEMS = "items"
    }
  }
}
