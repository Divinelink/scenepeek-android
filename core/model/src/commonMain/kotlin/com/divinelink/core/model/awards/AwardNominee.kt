package com.divinelink.core.model.awards

import com.divinelink.core.model.locale.Country
import com.divinelink.core.model.media.MediaReference

data class AwardNominee(
  val media: MediaReference,
  val winner: Boolean,
  val winningMedia: MediaReference?,
  val countries: List<Country>?,
)
