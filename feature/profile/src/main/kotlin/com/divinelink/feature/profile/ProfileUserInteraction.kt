package com.divinelink.feature.profile

sealed interface ProfileUserInteraction {
  data object Login : ProfileUserInteraction
  data object NavigateToWatchlist : ProfileUserInteraction
}
