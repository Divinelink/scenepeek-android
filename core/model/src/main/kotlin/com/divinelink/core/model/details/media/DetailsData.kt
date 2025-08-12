package com.divinelink.core.model.details.media

import com.divinelink.core.model.details.Person
import com.divinelink.core.model.details.Season
import com.divinelink.core.model.details.review.Review
import com.divinelink.core.model.media.MediaItem

sealed interface DetailsData {
  data class About(
    val overview: String?,
    val tagline: String?,
    val genres: List<String>?,
    val creators: List<Person>?,
    val information: MediaDetailsInformation?,
  ) : DetailsData

  data class Cast(
    val isTv: Boolean,
    val items: List<Person>,
  ) : DetailsData

  data class Recommendations(val items: List<MediaItem.Media>) : DetailsData

  data class Reviews(val items: List<Review>) : DetailsData

  data class Seasons(val items: List<Season>) : DetailsData
}
