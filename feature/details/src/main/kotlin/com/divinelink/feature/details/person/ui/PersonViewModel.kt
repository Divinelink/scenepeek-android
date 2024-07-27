package com.divinelink.feature.details.person.ui

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.divinelink.feature.details.navigation.person.PersonNavArguments
import com.divinelink.feature.details.screens.destinations.PersonScreenDestination
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class PersonViewModel @Inject constructor(savedStateHandle: SavedStateHandle) : ViewModel() {

  private val args: PersonNavArguments = PersonScreenDestination.argsFrom(savedStateHandle)

  private val _uiState: MutableStateFlow<PersonUiState> = MutableStateFlow(
    PersonUiState.initial(args.id),
  )
  val uiState: StateFlow<PersonUiState> = _uiState.asStateFlow()
}
