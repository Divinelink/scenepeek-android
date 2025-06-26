package com.divinelink.feature.profile.ui.provider

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.divinelink.core.fixtures.model.account.AccountDetailsFactory
import com.divinelink.core.model.account.TMDBAccount
import com.divinelink.feature.profile.ProfileUiState

class ProfileUiStateParameterProvider : PreviewParameterProvider<ProfileUiState> {
  override val values: Sequence<ProfileUiState> = sequenceOf(
    ProfileUiState(
      tmdbAccount = TMDBAccount.NotLoggedIn,
    ),
    ProfileUiState(
      tmdbAccount = TMDBAccount.LoggedIn(
        AccountDetailsFactory.Pinkman(),
      ),
    ),
  )
}
