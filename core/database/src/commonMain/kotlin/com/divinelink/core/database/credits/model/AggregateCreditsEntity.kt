package com.divinelink.core.database.credits.model

import com.divinelink.core.model.media.MediaItem

data class AggregateCreditsEntity(
  val id: Long,
  val crew: List<MediaItem.Person>,
  val cast: List<MediaItem.Person>,
)
