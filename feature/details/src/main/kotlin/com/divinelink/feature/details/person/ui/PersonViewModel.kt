package com.divinelink.feature.details.person.ui

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.divinelink.core.data.person.details.model.PersonDetailsResult
import com.divinelink.core.domain.details.person.FetchPersonDetailsUseCase
import com.divinelink.core.navigation.arguments.PersonNavArguments
import com.divinelink.feature.details.screens.destinations.PersonScreenDestination
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class PersonViewModel @Inject constructor(
  fetchPersonDetailsUseCase: FetchPersonDetailsUseCase,
  savedStateHandle: SavedStateHandle,
) : ViewModel() {

  private val args: PersonNavArguments = PersonScreenDestination.argsFrom(savedStateHandle)

  private val _uiState: MutableStateFlow<PersonUiState> = MutableStateFlow(
    PersonUiState.Loading,
  )
  val uiState: StateFlow<PersonUiState> = _uiState.asStateFlow()

  init {
    fetchPersonDetailsUseCase(args.id)
      .onEach { result ->
        result.fold(
          onSuccess = { detailsResult ->
            when (detailsResult) {
              is PersonDetailsResult.DetailsSuccess -> {
                _uiState.update {
                  PersonUiState.Success(personDetails = detailsResult.personDetails)
                }
              }

              is PersonDetailsResult.DetailsFailure -> _uiState.update { PersonUiState.Error }
            }
          },
          onFailure = {
            _uiState.update { PersonUiState.Error }
          },
        )
      }
      .launchIn(viewModelScope)
  }
}
