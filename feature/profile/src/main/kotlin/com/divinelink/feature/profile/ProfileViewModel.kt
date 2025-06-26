package com.divinelink.feature.profile

import androidx.lifecycle.ViewModel
import com.divinelink.core.model.account.TMDBAccount
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class ProfileViewModel : ViewModel() {

  private val _uiState: MutableStateFlow<ProfileUiState> = MutableStateFlow(
    ProfileUiState(
      tmdbAccount = TMDBAccount.Initial,
    ),
  )
  val uiState: StateFlow<ProfileUiState> = _uiState
}
