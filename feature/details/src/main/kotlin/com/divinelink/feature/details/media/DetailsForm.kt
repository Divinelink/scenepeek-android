package com.divinelink.feature.details.media

import com.divinelink.core.model.details.Person
import com.divinelink.core.model.details.rating.RatingCount
import com.divinelink.core.model.details.rating.RatingSource

sealed interface DetailsData {
  data class TVAbout(
    val overview: String,
    val tagline: String,
    val genres: List<String>,
    val ratingCount: RatingCount,
    val ratingSource: RatingSource,
  ) : DetailsData

  data class About(
    val overview: String?,
    val tagline: String?,
    val genres: List<String>?,
    val director: Person?,
    val ratingCount: RatingCount,
    val ratingSource: RatingSource,
  ) : DetailsData
}

sealed interface DetailsForm<T : DetailsData> {
  data object Loading : DetailsForm<Nothing>
  data object Error : DetailsForm<Nothing>
  data class Content<T : DetailsData>(val data: T) : DetailsForm<T>
}
