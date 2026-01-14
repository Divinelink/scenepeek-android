package com.divinelink.core.model.home

import com.divinelink.core.model.media.MediaType
import kotlinx.serialization.Serializable

@Serializable
sealed interface MediaListSection {
  @Serializable
  sealed interface TopBar : MediaListSection

  @Serializable
  sealed interface Home : MediaListSection

  @Serializable
  data object Favorites : TopBar

  @Serializable
  data object TopRated : TopBar

  @Serializable
  data object TrendingAll : Home

  @Serializable
  data class Popular(
    val mediaType: MediaType,
  ) : Home

  @Serializable
  data class Upcoming(
    val mediaType: MediaType,
    val minDate: String,
  ) : Home
}
