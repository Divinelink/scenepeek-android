package com.divinelink.core.model.home

import com.divinelink.core.model.media.MediaType

sealed interface HomeSection {
  data object TrendingAll : HomeSection

  data class Popular(
    val mediaType: MediaType,
  ) : HomeSection

  data class Upcoming(
    val mediaType: MediaType,
    val minDate: String,
  ) : HomeSection
}
