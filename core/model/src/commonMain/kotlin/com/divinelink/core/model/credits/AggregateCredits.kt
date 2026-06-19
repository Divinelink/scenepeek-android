package com.divinelink.core.model.credits

import com.divinelink.core.model.media.MediaItem
import kotlinx.serialization.Serializable

@Serializable
data class AggregateCredits(
  val cast: List<MediaItem.Person>,
  val crewDepartments: List<SeriesCrewDepartment>,
  val id: Long,
)
