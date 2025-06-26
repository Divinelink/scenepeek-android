package com.divinelink.feature.profile

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class ProfileViewModel : ViewModel() {

  private val _uiState: MutableStateFlow<ProfileUiState> = MutableStateFlow(ProfileUiState)
  val uiState: StateFlow<ProfileUiState> = _uiState
}
