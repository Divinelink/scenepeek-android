package com.divinelink.feature.details.person.ui

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.divinelink.core.data.person.details.model.PersonDetailsResult
import com.divinelink.core.domain.change.FetchChangesUseCase
import com.divinelink.core.domain.details.person.FetchPersonDetailsUseCase
import com.divinelink.core.domain.details.person.PersonDetailsParams
import com.divinelink.core.model.LayoutStyle
import com.divinelink.core.navigation.arguments.PersonNavArguments
import com.divinelink.core.navigation.arguments.map
import com.divinelink.feature.details.person.ui.filter.CreditFilter
import com.divinelink.feature.details.person.ui.tab.PersonTab
import com.divinelink.feature.details.screens.destinations.PersonScreenDestination
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
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
        tabs = PersonTab.entries,
      )
    } else {
      PersonUiState(
        selectedTabIndex = 0,
        forms = mapOf(
          PersonTab.ABOUT.order to PersonForm.About(PersonDetailsUiState.Data.Prefetch(args.map())),
          PersonTab.MOVIES.order to PersonForm.Movies(emptyMap()),
          PersonTab.TV_SHOWS.order to PersonForm.TvShows(emptyMap()),
        ),
        tabs = PersonTab.entries,
      )
    },
  )
  val uiState: StateFlow<PersonUiState> = _uiState

  init {
    viewModelScope.launch {
      fetchPersonDetailsUseCase(
        PersonDetailsParams(
          id = args.id,
          knownForDepartment = args.knownForDepartment,
        ),
      )
        .distinctUntilChanged()
        .collect { personDetailsResult ->
          personDetailsResult.fold(
            onSuccess = { result ->
              when (result) {
                is PersonDetailsResult.DetailsSuccess -> _uiState.update { uiState ->
                  uiState.copy(
                    forms = uiState.forms.mapValues { (key, value) ->
                      when (key) {
                        PersonTab.ABOUT.order -> PersonForm.About(
                          personDetails = PersonDetailsUiState.Data.Visible(
                            result.personDetails,
                          ),
                        )
                        else -> value
                      }
                    },
                    isLoading = false,
                  )
                }

                is PersonDetailsResult.CreditsSuccess -> _uiState.update { uiState ->
                  uiState.copy(
                    knownForCredits = result.knownForCredits,
                    forms = uiState.forms.mapValues { (key, value) ->
                      when (key) {
                        1 -> PersonForm.Movies(credits = result.movies)
                        2 -> PersonForm.TvShows(credits = result.tvShows)
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

  // TODO Add tests for this function
  fun onUpdateLayoutStyle() {
    val layoutStyle = when (_uiState.value.layoutStyle) {
      LayoutStyle.GRID -> LayoutStyle.LIST
      LayoutStyle.LIST -> LayoutStyle.GRID
    }

    _uiState.update { uiState ->
      uiState.copy(layoutStyle = layoutStyle)
    }
  }

  // TODO Add tests for this function
  fun onApplyFilter(filter: CreditFilter) {
    val selectedTab = _uiState.value.selectedTabIndex
    _uiState.update { uiState ->
      uiState.copy(
        filters = if (uiState.filters[selectedTab]?.contains(filter) == true) {
          uiState.filters.mapValues { (key, value) ->
            if (key == selectedTab) {
              value.filter { it != filter }
            } else {
              value
            }
          }
        } else {
          uiState.filters.mapValues { (key, value) ->
            if (key == selectedTab) {
              listOf(filter)
            } else {
              value
            }
          }
        },
      )
    }
  }
}
