package com.divinelink.feature.profile.ui.provider

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.divinelink.core.commons.ExcludeFromKoverReport
import com.divinelink.core.fixtures.model.account.AccountDetailsFactory
import com.divinelink.core.model.account.TMDBAccount
import com.divinelink.feature.profile.ProfileUiState
import com.divinelink.feature.profile.ui.TMDBAccountUiState

@ExcludeFromKoverReport
class ProfileUiStateParameterProvider : PreviewParameterProvider<ProfileUiState> {
  override val values: Sequence<ProfileUiState> = sequenceOf(
    ProfileUiState(
      accountUiState = TMDBAccountUiState.Anonymous,
    ),
    ProfileUiState(
      accountUiState = TMDBAccountUiState.Initial,
    ),
    ProfileUiState(
      accountUiState = TMDBAccountUiState.Error,
    ),
    ProfileUiState(
      accountUiState = TMDBAccountUiState.LoggedIn(
        TMDBAccount.LoggedIn(AccountDetailsFactory.Pinkman()),
      ),
    ),
  )
}
