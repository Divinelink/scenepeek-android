package com.divinelink.feature.details.person.ui

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.divinelink.core.data.person.details.model.PersonDetailsResult
import com.divinelink.core.domain.change.FetchChangesUseCase
import com.divinelink.core.domain.details.person.FetchPersonDetailsUseCase
import com.divinelink.core.domain.details.person.PersonDetailsParams
import com.divinelink.core.model.details.person.GroupedPersonCredits
import com.divinelink.core.model.tab.PersonTab
import com.divinelink.core.navigation.route.Navigation.PersonRoute
import com.divinelink.core.navigation.route.map
import com.divinelink.feature.details.person.ui.filter.CreditFilter
import io.github.aakira.napier.Napier
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class PersonViewModel(
  fetchPersonDetailsUseCase: FetchPersonDetailsUseCase,
  fetchChangesUseCase: FetchChangesUseCase,
  savedStateHandle: SavedStateHandle,
) : ViewModel() {

  private val route: PersonRoute = PersonRoute(
    id = savedStateHandle.get<Long>("id") ?: -1,
    knownForDepartment = savedStateHandle["knownForDepartment"],
    name = savedStateHandle["name"],
    profilePath = savedStateHandle["profilePath"],
    gender = savedStateHandle["gender"],
  )

  private val _uiState: MutableStateFlow<PersonUiState> = MutableStateFlow(
    if (route.name == null) {
      PersonUiState(
        selectedTabIndex = 0,
        isLoading = true,
        tabs = PersonTab.entries,
      )
    } else {
      PersonUiState(
        selectedTabIndex = 0,
        forms = mapOf(
          PersonTab.About.order to PersonForm.About(
            PersonDetailsUiState.Data.Prefetch(route.map()),
          ),
          PersonTab.Movies.order to PersonForm.Movies(emptyMap()),
          PersonTab.TVShows.order to PersonForm.TvShows(emptyMap()),
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
          id = route.id,
          knownForDepartment = route.knownForDepartment,
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
                        PersonTab.About.order -> PersonForm.About(
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
                        PersonTab.Movies.order -> PersonForm.Movies(credits = result.movies)
                        PersonTab.TVShows.order -> PersonForm.TvShows(credits = result.tvShows)
                        else -> value
                      }
                    },
                    filteredCredits = mapOf(
                      PersonTab.Movies.order to result.movies,
                      PersonTab.TVShows.order to result.tvShows,
                    ),
                  )
                }
                is PersonDetailsResult.DetailsFailure -> _uiState.update { uiState ->
                  uiState.copy(isError = true)
                }
              }
            },
            onFailure = {
              Napier.d(it.message.toString())
              _uiState.update { uiState ->
                uiState.copy(isError = true)
              }
            },
          )
        }
    }

    viewModelScope.launch {
      fetchChangesUseCase(route.id)
    }
  }

  fun onTabSelected(tab: Int) {
    _uiState.update { uiState ->
      uiState.copy(selectedTabIndex = tab)
    }
  }

  fun onApplyFilter(filter: CreditFilter) {
    val selectedTab = _uiState.value.selectedTabIndex

    _uiState.update { oldState ->
      val newFilters = if (oldState.filters[selectedTab]?.contains(filter) == true) {
        oldState.filters.mapValues { (key, value) ->
          if (key == selectedTab) value.filter { it != filter } else value
        }
      } else {
        oldState.filters.mapValues { (key, value) ->
          if (key == selectedTab) listOf(filter) else value
        }
      }

      val newFilteredCredits = oldState.forms
        .filterKeys { it != PersonTab.About.order }
        .mapValues { (key, form) ->
          when (key) {
            PersonTab.Movies.order -> if (key == selectedTab) {
              applyFilters(
                credits = (form as PersonForm.Movies).credits,
                filters = newFilters[selectedTab] ?: emptyList(),
              )
            } else {
              oldState.filteredCredits[key] ?: emptyMap()
            }
            PersonTab.TVShows.order -> if (key == selectedTab) {
              applyFilters(
                credits = (form as PersonForm.TvShows).credits,
                filters = newFilters[selectedTab] ?: emptyList(),
              )
            } else {
              oldState.filteredCredits[key] ?: emptyMap()
            }
            else -> emptyMap()
          }
        }

      oldState.copy(
        filters = newFilters,
        filteredCredits = newFilteredCredits,
      )
    }
  }

  private fun applyFilters(
    credits: GroupedPersonCredits,
    filters: List<CreditFilter>,
  ): GroupedPersonCredits = credits.filterKeys { department ->
    filters.all { filter ->
      when (filter) {
        is CreditFilter.Department -> department == filter.department
        CreditFilter.SortReleaseDate -> true
      }
    }
  }
}
