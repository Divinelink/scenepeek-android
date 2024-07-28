package com.divinelink.feature.details.person.ui

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.divinelink.core.domain.details.person.FetchPersonDetailsUseCase
import com.divinelink.feature.details.navigation.person.PersonNavArguments
import com.divinelink.feature.details.screens.destinations.PersonScreenDestination
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class PersonViewModel @Inject constructor(
  fetchPersonDetailsUseCase: FetchPersonDetailsUseCase,
  savedStateHandle: SavedStateHandle,
) : ViewModel() {

  private val args: PersonNavArguments = PersonScreenDestination.argsFrom(savedStateHandle)

  private val _uiState: MutableStateFlow<PersonUiState> = MutableStateFlow(
    PersonUiState.initial(args.id),
  )
  val uiState: StateFlow<PersonUiState> = _uiState.asStateFlow()

  init {
    fetchPersonDetailsUseCase(args.id)
      .onEach { result ->
        result.fold(
          onSuccess = { personDetails ->
//            _uiState.value = PersonUiState.success(personDetails)
          },
          onFailure = {
            // TODO: Handle error
          },
        )
      }
      .launchIn(viewModelScope)
  }
}
