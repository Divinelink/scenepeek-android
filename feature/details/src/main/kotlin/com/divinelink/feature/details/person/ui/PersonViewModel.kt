package com.divinelink.feature.details.person.ui

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.divinelink.core.data.person.details.model.PersonDetailsResult
import com.divinelink.core.domain.FetchChangesUseCase
import com.divinelink.core.domain.details.person.FetchPersonDetailsUseCase
import com.divinelink.core.domain.details.person.PersonDetailsParams
import com.divinelink.core.navigation.arguments.PersonNavArguments
import com.divinelink.core.navigation.arguments.map
import com.divinelink.feature.details.person.ui.credits.PersonCreditsUiState
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
      PersonUiState(isLoading = true)
    } else {
      PersonUiState(personDetails = PersonDetailsUiState.Data.Prefetch(args.map()))
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
                  )
                }

                is PersonDetailsResult.CreditsSuccess -> _uiState.update { uiState ->
                  uiState.copy(
                    credits = PersonCreditsUiState.Visible(
                      knownFor = detailsResult.knownForCredits,
                    ),
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
}
