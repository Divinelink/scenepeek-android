package com.divinelink.core.network.media.model.credits

import kotlinx.serialization.Serializable

@Serializable
data class AggregateCreditsApi(
  val cast: List<SeriesCastApi>,
  val crew: List<SeriesCrewApi>,
  val id: Int,
)
