package com.divinelink.feature.home.nextup

import com.divinelink.core.database.show.ShowSyncError
import com.divinelink.core.model.track.NextUpEpisode

data class NextUpUiState(
  val episodes: List<NextUpEpisode>,
  val errors: List<ShowSyncError>,
  val loading: Boolean,
) {
  companion object {
    val initial = NextUpUiState(
      episodes = emptyList(),
      errors = emptyList(),
      loading = true,
    )
  }
}
