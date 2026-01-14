package com.divinelink.core.model.home

import com.divinelink.core.model.media.MediaType
import kotlinx.serialization.Serializable

@Serializable
sealed interface HomeSection {
  @Serializable
  data object Favorites : HomeSection

  @Serializable
  sealed interface Remote : HomeSection {
    val mediaType: MediaType?
  }

  @Serializable
  data object TrendingAll : Remote {
    override val mediaType: MediaType? = null
  }

  @Serializable
  data class Popular(
    override val mediaType: MediaType,
  ) : Remote

  @Serializable
  data class Upcoming(
    override val mediaType: MediaType,
    val minDate: String,
  ) : Remote

  @Serializable
  data class TopRated(
    override val mediaType: MediaType,
  ) : Remote
}
