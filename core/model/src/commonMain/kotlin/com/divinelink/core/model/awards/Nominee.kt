package com.divinelink.core.model.awards

import com.divinelink.core.model.media.MediaType

data class AwardNominee(
  val id: Int,
  val winner: Boolean,
  val mediaType: MediaType,
)
