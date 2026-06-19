package com.divinelink.core.model.details.media

import com.divinelink.core.model.Genre
import com.divinelink.core.model.details.Collection
import com.divinelink.core.model.details.Keyword
import com.divinelink.core.model.details.Season
import com.divinelink.core.model.details.review.Review
import com.divinelink.core.model.media.MediaItem
import com.divinelink.core.model.media.MediaType

sealed interface DetailsData {
  data class About(
    val mediaType: MediaType,
    val overview: String?,
    val tagline: String?,
    val genres: List<Genre>?,
    val creators: List<MediaItem.Person>?,
    val information: MediaDetailsInformation?,
    val collection: Collection?,
    val keywords: List<Keyword>?,
  ) : DetailsData

  data class Cast(
    val isTv: Boolean,
    val items: List<MediaItem.Person>,
  ) : DetailsData

  data class Recommendations(val items: List<MediaItem.Media>) : DetailsData

  data class Reviews(val items: List<Review>) : DetailsData

  data class Seasons(val items: List<Season>) : DetailsData
}
