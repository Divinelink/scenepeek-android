package com.divinelink.feature.details.person.ui

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.divinelink.core.data.person.details.model.PersonDetailsResult
import com.divinelink.core.domain.change.FetchChangesUseCase
import com.divinelink.core.domain.details.person.FetchPersonDetailsUseCase
import com.divinelink.core.domain.details.person.PersonDetailsParams
import com.divinelink.core.navigation.arguments.PersonNavArguments
import com.divinelink.core.navigation.arguments.map
import com.divinelink.feature.details.person.ui.tab.PersonTab
import com.divinelink.feature.details.screens.destinations.PersonScreenDestination
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber

class PersonViewModel(
  fetchPersonDetailsUseCase: FetchPersonDetailsUseCase,
  fetchChangesUseCase: FetchChangesUseCase,
  savedStateHandle: SavedStateHandle,
) : ViewModel() {

  private val args: PersonNavArguments = PersonScreenDestination.argsFrom(savedStateHandle)

  private val _uiState: MutableStateFlow<PersonUiState> = MutableStateFlow(
    if (args.name == null) {
      PersonUiState(
        selectedTabIndex = 0,
        isLoading = true,
        forms = mapOf(
          0 to PersonForm.About(PersonDetailsUiState.Loading),
          1 to PersonForm.Movies(emptyList()),
          2 to PersonForm.TvShows(emptyList()),
        ),
        tabs = PersonTab.entries,
      )
    } else {
      PersonUiState(
        selectedTabIndex = 0,
        personDetails = PersonDetailsUiState.Data.Prefetch(args.map()),
        forms = mapOf(
          0 to PersonForm.About(PersonDetailsUiState.Loading),
          1 to PersonForm.Movies(emptyList()),
          2 to PersonForm.TvShows(emptyList()),
        ),
        tabs = PersonTab.entries,
      )
    },
  )
  val uiState: StateFlow<PersonUiState> = _uiState.asStateFlow()

  init {
    viewModelScope.launch {
      fetchPersonDetailsUseCase(
        PersonDetailsParams(
          id = args.id,
          knownForDepartment = args.knownForDepartment,
        ),
      )
        .distinctUntilChanged()
        .collect { result ->
          result.fold(
            onSuccess = { detailsResult ->
              when (detailsResult) {
                is PersonDetailsResult.DetailsSuccess -> _uiState.update { uiState ->
                  uiState.copy(
                    personDetails = PersonDetailsUiState.Data.Visible(
                      detailsResult.personDetails,
                    ),
                    isLoading = false,
                  )
                }

                is PersonDetailsResult.CreditsSuccess -> _uiState.update { uiState ->
                  uiState.copy(
                    knownForCredits = detailsResult.knownForCredits,
                    forms = uiState.forms.mapValues { (key, value) ->
                      when (key) {
                        1 -> PersonForm.Movies(detailsResult.movies)
                        2 -> PersonForm.TvShows(detailsResult.tvShows)
                        else -> value
                      }
                    },

                  )
                }
                is PersonDetailsResult.DetailsFailure -> _uiState.update { uiState ->
                  uiState.copy(isError = true)
                }
              }
            },
            onFailure = {
              Timber.d(it)
              _uiState.update { uiState ->
                uiState.copy(isError = true)
              }
            },
          )
        }
    }

    viewModelScope.launch {
      fetchChangesUseCase(args.id)
    }
  }

  fun onTabSelected(tab: Int) {
    _uiState.update { uiState ->
      uiState.copy(selectedTabIndex = tab)
    }
  }
}
