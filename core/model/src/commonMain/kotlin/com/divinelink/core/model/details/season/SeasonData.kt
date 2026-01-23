package com.divinelink.core.model.details.season

import com.divinelink.core.model.details.Episode
import com.divinelink.core.model.details.Person

data class SeasonInformation(
  val totalEpisodes: Int,
  val totalRuntime: String?,
  val firstAirDate: String?,
  val lastAirDate: String?,
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
    val cast: List<Person>,
  ) : SeasonData
}
