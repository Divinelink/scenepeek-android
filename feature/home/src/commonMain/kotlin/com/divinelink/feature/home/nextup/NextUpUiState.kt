package com.divinelink.feature.home.nextup

import com.divinelink.core.model.DataState
import com.divinelink.core.model.track.NextUpEpisode

data class NextUpUiState(
  val data: DataState<NextUpEpisode>,
) {
  companion object {
    val initial = NextUpUiState(
      data = DataState.Initial,
    )
  }
}
