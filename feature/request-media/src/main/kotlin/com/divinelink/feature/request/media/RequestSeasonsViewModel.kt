package com.divinelink.feature.request.media

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.divinelink.core.data.jellyseerr.repository.JellyseerrRepository
import com.divinelink.core.model.details.Season
import com.divinelink.core.model.jellyseerr.radarr.SonarrInstance
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class RequestSeasonsViewModel(repository: JellyseerrRepository) : ViewModel() {

  private val _uiState = MutableStateFlow(RequestSeasonsUiState.initial(emptyList()))
  val uiState = _uiState.asStateFlow()

  init {
    viewModelScope.launch {
      repository.getSonarrInstances().fold(
        onSuccess = { instances ->
          _uiState.update { uiState ->
            val default = instances.find { it.isDefault }
            uiState.copy(
              instances = instances,
              selectedInstance = if (default != null) {
                CurrentSonarrInstanceState.Data(default)
              } else {
                CurrentSonarrInstanceState.Error
              },
            )
          }
        },
        onFailure = {
          // Handle error
        },
      )
    }
  }

  fun updateSeasons(seasons: List<Season>) {
    _uiState.update { uiState ->
      uiState.copy(seasons = seasons)
    }
  }

  fun selectInstance(instance: SonarrInstance) {
    _uiState.update { uiState ->
      uiState.copy(selectedInstance = CurrentSonarrInstanceState.Data(instance))
    }
  }
}
