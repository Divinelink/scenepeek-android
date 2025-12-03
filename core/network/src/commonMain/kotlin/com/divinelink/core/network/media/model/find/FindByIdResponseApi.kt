package com.divinelink.core.network.media.model.find

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class FindByIdResponseApi(
  @SerialName("movie_results") val movieResults: List<MovieResultApi>,
  @SerialName("person_results") val personResults: List<PersonResultApi>,
  @SerialName("tv_results") val tvResults: List<TvResultApi>,
  @SerialName("tv_episode_results") val tvEpisodeResults: List<TvEpisodeResultApi>,
)
