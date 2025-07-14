package com.divinelink.feature.lists.details

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.divinelink.core.navigation.route.ListDetailsRoute
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class ListDetailsViewModel(savedStateHandle: SavedStateHandle) : ViewModel() {

  private val route: ListDetailsRoute = ListDetailsRoute(
    id = savedStateHandle.get<Int>("id") ?: -1,
    name = savedStateHandle.get<String>("name") ?: "",
  )

  private val _uiState: MutableStateFlow<ListDetailsUiState> = MutableStateFlow(
    ListDetailsUiState.initial(
      id = route.id,
      name = route.name,
    ),
  )
  val uiState: StateFlow<ListDetailsUiState> = _uiState
}
