package com.divinelink.core.model.details.media

import com.divinelink.core.model.media.MediaReference
import com.divinelink.core.model.media.MediaType

sealed class DetailSection(
  val media: MediaReference,
) {

  data class MovieRecommendations(val id: Long) : DetailSection(MediaReference(id, MediaType.MOVIE))
  data class MovieReviews(val id: Long) : DetailSection(MediaReference(id, MediaType.MOVIE))

  data class TvRecommendations(val id: Long) : DetailSection(MediaReference(id, MediaType.TV))
  data class TvReviews(val id: Long) : DetailSection(MediaReference(id, MediaType.TV))
}
