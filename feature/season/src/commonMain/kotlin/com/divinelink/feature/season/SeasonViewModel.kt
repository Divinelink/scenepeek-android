package com.divinelink.feature.season

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.divinelink.core.data.media.repository.MediaRepository
import com.divinelink.core.navigation.route.Navigation
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update

class SeasonViewModel(
  val repository: MediaRepository,
  savedStateHandle: SavedStateHandle,
) : ViewModel() {

  private val route: Navigation.SeasonRoute = Navigation.SeasonRoute(
    backdropPath = savedStateHandle.get<String>("backdropPath"),
    title = savedStateHandle.get<String>("title") ?: "",
    showId = savedStateHandle.get<Int>("showId") ?: -1,
    seasonNumber = savedStateHandle.get<Int>("seasonNumber") ?: -1,
  )

  private val _uiState: MutableStateFlow<SeasonUiState> = MutableStateFlow(
    SeasonUiState.initial(route),
  )
  val uiState: StateFlow<SeasonUiState> = _uiState

  init {
    repository.fetchSeason(
      showId = route.showId,
      seasonNumber = route.seasonNumber,
    )
      .distinctUntilChanged()
      .onEach { result ->
        result.fold(
          onSuccess = {
            _uiState.update { uiState ->
              uiState.copy(season = it)
            }
          },
          onFailure = {
          },
        )
      }
      .launchIn(viewModelScope)
  }
}
