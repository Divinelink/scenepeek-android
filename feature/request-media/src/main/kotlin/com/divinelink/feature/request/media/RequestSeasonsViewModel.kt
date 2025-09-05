package com.divinelink.feature.request.media

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.divinelink.core.data.jellyseerr.repository.JellyseerrRepository
import com.divinelink.core.model.details.Season
import com.divinelink.core.model.jellyseerr.server.InstanceProfile
import com.divinelink.core.model.jellyseerr.server.InstanceRootFolder
import com.divinelink.core.model.jellyseerr.server.sonarr.SonarrInstance
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class RequestSeasonsViewModel(private val repository: JellyseerrRepository) : ViewModel() {

  private val _uiState = MutableStateFlow(
    RequestSeasonsUiState.initial(
      seasons = emptyList(),
    ),
  )
  val uiState = _uiState.asStateFlow()

  init {
    viewModelScope.launch {
      repository.getSonarrInstances().fold(
        onSuccess = { instances ->
          val default = instances.find { it.isDefault && it.is4k == uiState.value.is4k }
          _uiState.update { uiState ->
            uiState.copy(
              instances = instances.filter { it.is4k == uiState.is4k },
            )
          }
          if (default == null) {
            _uiState.update { uiState ->
              uiState.copy(selectedInstance = LCEState.Error)
            }
          } else {
            selectInstance(default)
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
      uiState.copy(selectedInstance = LCEState.Content(instance))
    }

    viewModelScope.launch {
      repository.getSonarrInstanceDetails(instance.id).fold(
        onSuccess = { result ->
          _uiState.update { uiState ->
            val defaultProfile = result.profiles.find { it.id == instance.activeProfileId }
            val defaultRootFolder = result.rootFolders.find { it.path == instance.activeDirectory }
            uiState.copy(
              profiles = result.profiles,
              rootFolders = result.rootFolders,
              selectedProfile = if (defaultProfile == null) {
                LCEState.Error
              } else {
                LCEState.Content(defaultProfile)
              },
              selectedRootFolder = if (defaultRootFolder == null) {
                LCEState.Error
              } else {
                LCEState.Content(defaultRootFolder)
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

  fun selectQualityProfile(quality: InstanceProfile) {
    _uiState.update { uiState ->
      uiState.copy(selectedProfile = LCEState.Content(quality))
    }
  }

  fun selectRootFolder(folder: InstanceRootFolder) {
    _uiState.update { uiState ->
      uiState.copy(selectedRootFolder = LCEState.Content(folder))
    }
  }
}
