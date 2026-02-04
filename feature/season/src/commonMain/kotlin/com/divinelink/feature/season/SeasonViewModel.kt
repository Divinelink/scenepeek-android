package com.divinelink.feature.season

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.divinelink.core.data.media.repository.MediaRepository
import com.divinelink.core.model.details.SeasonDetails
import com.divinelink.core.model.details.season.SeasonData
import com.divinelink.core.model.details.season.SeasonForm
import com.divinelink.core.model.details.season.SeasonInformation
import com.divinelink.core.model.tab.SeasonTab
import com.divinelink.core.navigation.route.Navigation
import com.divinelink.core.network.Resource
import io.github.aakira.napier.Napier
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
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
            _uiState.update { state ->
              val aboutTab = SeasonTab.About

              val updatedForms = state.forms.toMutableMap().apply {
                this[aboutTab] = SeasonForm.Content(
                  SeasonData.About(
                    overview = it.overview,
                    information = SeasonInformation(
                      totalEpisodes = it.episodeCount,
                      totalRuntime = null,
                      airedEpisodes = null,
                      lastAirDate = null,
                      firstAirDate = it.airDate,
                    ),
                  ),
                )
              }

              state.copy(
                season = it,
                forms = updatedForms,
              )
            }
          },
          onFailure = {
            Napier.d(it.message.toString())
          },
        )
      }
      .launchIn(viewModelScope)

    repository.fetchSeasonDetails(
      showId = route.showId,
      season = route.seasonNumber,
    )
      .distinctUntilChanged()
      .catch {
        Napier.d(it.message.toString())
      }
      .onEach { result ->
        when (result) {
          is Resource.Error -> Napier.d(result.error.message.toString())
          is Resource.Loading -> result.data?.let { data -> handleSuccessResponse(data) }
          is Resource.Success -> result.data?.let { data -> handleSuccessResponse(data) }
        }
      }
      .launchIn(viewModelScope)
  }

  fun onAction(action: SeasonAction) {
    when (action) {
      is SeasonAction.OnSelectTab -> handleSelectTab(action)
    }
  }

  private fun handleSelectTab(action: SeasonAction.OnSelectTab) {
    _uiState.update { state ->
      state.copy(
        selectedTab = action.index,
      )
    }
  }

  private fun handleSuccessResponse(data: SeasonDetails) {
    _uiState.update { state ->
      val episodeTab = SeasonTab.Episodes
      val aboutTab = SeasonTab.About
      val castTab = SeasonTab.GuestStars

      val updatedForms = state.forms.toMutableMap().apply {
        this[episodeTab] = SeasonForm.Content(
          SeasonData.Episodes(data.episodes),
        )
        this[aboutTab] = SeasonForm.Content(
          SeasonData.About(
            overview = data.overview,
            information = SeasonInformation(
              totalEpisodes = data.episodeCount,
              totalRuntime = data.totalRuntime,
              airedEpisodes = data.episodes.count { it.runtime != null },
              lastAirDate = data.episodes.findLast { it.runtime != null }?.airDate,
              firstAirDate = data.airDate,
            ),
          ),
        )
        this[castTab] = SeasonForm.Content(
          SeasonData.GuestStars(cast = data.guestStars),
        )
      }

      state.copy(
        forms = updatedForms,
      )
    }
  }
}
