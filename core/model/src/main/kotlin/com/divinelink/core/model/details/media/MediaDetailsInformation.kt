package com.divinelink.core.model.details.media

import com.divinelink.core.model.details.TvStatus
import com.divinelink.core.model.locale.Country
import com.divinelink.core.model.locale.Language

sealed interface MediaDetailsInformation {

  data class Movie(
    val originalTitle: String,
    val status: String,
    val runtime: String?,
    val originalLanguage: Language?,
    val budget: String,
    val revenue: String,
    val companies: List<String>,
    val countries: List<Country>,
  ) : MediaDetailsInformation

  data class TV(
    val originalTitle: String,
    val status: TvStatus,
    val firstAirDate: String,
    val lastAirDate: String,
    val nextEpisodeAirDate: String?,
    val seasons: Int,
    val episodes: Int,
    val originalLanguage: Language?,
    val companies: List<String>,
    val countries: List<Country>,
  ) : MediaDetailsInformation
}
