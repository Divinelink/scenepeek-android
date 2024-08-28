package com.divinelink.core.network.changes.model.serializer

enum class ImageType(val key: String) {
  PROFILE("profile"),
  BACKDROP("backdrop"),
  POSTER("poster"),
  TITLE_LOGO("title_logo"),
  ;

  companion object {
    fun fromKey(key: String?): ImageType? = entries.find { it.key == key }
  }
}
