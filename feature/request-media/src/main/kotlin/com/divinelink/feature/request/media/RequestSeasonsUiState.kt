package com.divinelink.feature.request.media

import com.divinelink.core.model.details.Season
import com.divinelink.core.model.jellyseerr.radarr.SonarrInstance

data class RequestSeasonsUiState(
  val seasons: List<Season>,
  val isLoading: Boolean,
  val instances: List<SonarrInstance>,
  val selectedInstance: CurrentSonarrInstanceState,
) {
  companion object {
    fun initial(seasons: List<Season>) = RequestSeasonsUiState(
      seasons = seasons,
      isLoading = true,
      instances = emptyList(),
      selectedInstance = CurrentSonarrInstanceState.Loading,
    )
  }
}

sealed interface CurrentSonarrInstanceState {
  data object Error : CurrentSonarrInstanceState
  data object Loading : CurrentSonarrInstanceState
  data class Data(val instance: SonarrInstance) : CurrentSonarrInstanceState
}
