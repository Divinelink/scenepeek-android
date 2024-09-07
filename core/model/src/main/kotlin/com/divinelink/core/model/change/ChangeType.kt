@file:Suppress("ktlint:standard:trailing-comma-on-declaration-site")

package com.divinelink.core.model.change

enum class ChangeType(val key: String) {
  ALSO_KNOWN_AS("also_known_as"),
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
  UNKNOWN("unknown");

  companion object {
    fun from(key: String?): ChangeType = entries.find { it.key == key } ?: UNKNOWN
  }
}
