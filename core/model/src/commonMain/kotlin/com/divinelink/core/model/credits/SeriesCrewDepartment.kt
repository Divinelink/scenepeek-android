package com.divinelink.core.model.credits

import com.divinelink.core.model.media.MediaItem
import kotlinx.serialization.Serializable

@Serializable
data class SeriesCrewDepartment(
  val department: String,
  val crewList: List<MediaItem.Person>,
) {
  val uniqueCrewList: List<MediaItem.Person>
    get() = crewList.distinctBy { it.id }
}
