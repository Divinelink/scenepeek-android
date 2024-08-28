package com.divinelink.core.network.changes.model.serializer

enum class ChangeKeyType(val key: String) {
  BIOGRAPHY("biography"),
  TRANSLATIONS("translations"),
  IMAGES("images"),
  VIDEOS("videos"),
  CAST("cast"),
  NAME("name"),
  PLOT_KEYWORDS("plot_keywords"),
  RELEASE_DATES("release_dates"),
  ALTERNATIVE_TITLES("alternative_titles"),
  TAGLINE("tagline"),
  ;

  companion object {
    fun from(key: String?): ChangeKeyType? = entries.find { it.key == key }
  }
}
