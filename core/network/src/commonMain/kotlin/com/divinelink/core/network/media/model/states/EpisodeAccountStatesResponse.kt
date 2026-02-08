package com.divinelink.core.network.media.model.states

import kotlinx.serialization.Serializable

@Serializable
data class EpisodeAccountStatesResponse(
  val results: List<EpisodeRatingResponse>,
)
