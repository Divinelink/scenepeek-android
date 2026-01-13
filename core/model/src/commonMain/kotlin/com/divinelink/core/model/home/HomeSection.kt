package com.divinelink.core.model.home

import com.divinelink.core.model.media.MediaType
import kotlinx.serialization.Serializable

@Serializable
sealed interface HomeSection {

  @Serializable
  data object TrendingAll : HomeSection

  @Serializable
  data class Popular(
    val mediaType: MediaType,
  ) : HomeSection

  @Serializable
  data class Upcoming(
    val mediaType: MediaType,
    val minDate: String,
  ) : HomeSection
}
