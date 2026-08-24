package com.divinelink.core.model.home

import com.divinelink.core.model.media.MediaType
import kotlinx.serialization.Serializable

@Serializable
sealed interface MediaListSection {
  @Serializable
  sealed interface TopBar : MediaListSection

  @Serializable
  data object Saved : TopBar

  @Serializable
  data object TopRated : TopBar

  @Serializable
  sealed interface Home : MediaListSection {
    val key: String
  }

  @Serializable
  data object TrendingAll : Home {
    override val key = "trending_all"
  }

  @Serializable
  data class Popular(val mediaType: MediaType) : Home {
    override val key: String = "popular_${mediaType.value}"
  }

  @Serializable
  data class Upcoming(val mediaType: MediaType) : Home {
    override val key: String = "upcoming_${mediaType.value}"
  }

  @Serializable
  data object RecentRequests : Home {
    override val key: String = "seerr_recent_requests"
  }

  companion object {
    val homeEntries = listOf(
      RecentRequests,
      TrendingAll,
      Popular(MediaType.MOVIE),
      Popular(MediaType.TV),
      Upcoming(MediaType.MOVIE),
      Upcoming(MediaType.TV),
    )

    fun from(key: String?): Home? = homeEntries.find { it.key == key }
  }
}
