package com.divinelink.feature.profile.ui

import com.divinelink.core.model.account.TMDBAccount

sealed interface TMDBAccountUiState {
  data class LoggedIn(val account: TMDBAccount) : TMDBAccountUiState
  data object NotLoggedIn : TMDBAccountUiState
  data object Initial : TMDBAccountUiState
  data object Error : TMDBAccountUiState
}
