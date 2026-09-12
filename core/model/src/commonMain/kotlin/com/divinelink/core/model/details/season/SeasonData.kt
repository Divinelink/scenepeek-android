package com.divinelink.core.model.details.season

import com.divinelink.core.model.details.Episode
import com.divinelink.core.model.media.MediaItem
import kotlinx.datetime.LocalDate

data class SeasonInformation(
  val totalEpisodes: Int,
  val totalRuntime: String?,
  val firstAirDate: LocalDate?,
  val lastAirDate: LocalDate?,
  val airedEpisodes: Int?,
)

sealed interface SeasonData {
  data class Episodes(
    val episodes: List<Episode>,
  ) : SeasonData

  data class About(
    val overview: String?,
    val information: SeasonInformation,
  ) : SeasonData

  data class Cast(
    val cast: List<MediaItem.Person>,
  ) : SeasonData

  data class GuestStars(
    val cast: List<MediaItem.Person>,
  ) : SeasonData
}
