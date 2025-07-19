package com.divinelink.feature.lists.create

import androidx.lifecycle.ViewModel
import com.divinelink.core.domain.list.CreateListUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class CreateListViewModel(private val createListUseCase: CreateListUseCase) : ViewModel() {

  private val _uiState: MutableStateFlow<CreateListUiState> =
    MutableStateFlow(CreateListUiState.initial)
  val uiState: StateFlow<CreateListUiState> = _uiState
}
