package com.divinelink.feature.awards.categories

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class AwardCategoriesViewModel : ViewModel() {

  private val _uiState: MutableStateFlow<AwardCategoriesUiState> =
    MutableStateFlow(AwardCategoriesUiState.initial)
  val uiState: StateFlow<AwardCategoriesUiState> = _uiState
}

