package com.divinelink.core.model.track.backup

import kotlinx.serialization.Serializable

@Serializable
data class LibraryExport(
  val version: Int,
  val exportedAt: String,
  val shows: List<ShowExport>,
  val seasons: List<SeasonExport>,
  val episodes: List<EpisodeExport>,
  val media: List<MediaExport>? = null,
  val people: List<PersonExport>? = null,
  val saved: SavedExport? = null,
)
