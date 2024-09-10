@file:Suppress("ktlint:standard:trailing-comma-on-declaration-site")

package com.divinelink.core.model.change

enum class ChangeType(val key: String) {
  ALSO_KNOWN_AS("also_known_as"),
  BIOGRAPHY("biography"),
  NAME("name"),
  TIKTOD_ID("tiktok_id"),
  YOUTUBE_ID("youtube_id"),
  FACEBOOK_ID("facebook_id"),
  IMDB_ID("imdb_id"),
  INSTAGRAM_ID("instagram_id"),
  TWITTER_ID("twitter_id"),
  BIRTHDAY("birthday"),
  DAY_OF_DEATH("day_of_death"),
  PLACE_OF_BIRTH("place_of_birth"),
  HOMEPAGE("homepage"),
  GENDER("gender"),
  TRANSLATIONS("translations"),
  IMAGES("images"),
  VIDEOS("videos"),
  CAST("cast"),
  PLOT_KEYWORDS("plot_keywords"),
  RELEASE_DATES("release_dates"),
  ALTERNATIVE_TITLES("alternative_titles"),
  TAGLINE("tagline"),
  PRIMARY("primary"),
  UNKNOWN("unknown");

  companion object {
    fun from(key: String?): ChangeType = entries.find { it.key == key } ?: UNKNOWN
  }
}
