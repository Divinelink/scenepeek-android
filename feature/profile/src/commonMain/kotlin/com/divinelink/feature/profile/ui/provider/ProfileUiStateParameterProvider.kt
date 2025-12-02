package com.divinelink.feature.profile.ui.provider

import com.divinelink.core.commons.ExcludeFromKoverReport
import com.divinelink.core.fixtures.model.account.AccountDetailsFactory
import com.divinelink.core.model.account.TMDBAccount
import com.divinelink.feature.profile.ProfileUiState
import com.divinelink.feature.profile.ui.TMDBAccountUiState
import org.jetbrains.compose.ui.tooling.preview.PreviewParameterProvider

@ExcludeFromKoverReport
class ProfileUiStateParameterProvider : PreviewParameterProvider<ProfileUiState> {
  override val values: Sequence<ProfileUiState> = sequenceOf(
    ProfileUiState(
      accountUiState = TMDBAccountUiState.Anonymous,
      isJellyseerrEnabled = false,
    ),
    ProfileUiState(
      accountUiState = TMDBAccountUiState.Initial,
      isJellyseerrEnabled = false,
    ),
    ProfileUiState(
      accountUiState = TMDBAccountUiState.Error,
      isJellyseerrEnabled = false,
    ),
    ProfileUiState(
      accountUiState = TMDBAccountUiState.LoggedIn(
        TMDBAccount.LoggedIn(AccountDetailsFactory.Pinkman()),
      ),
      isJellyseerrEnabled = false,
    ),
    ProfileUiState(
      accountUiState = TMDBAccountUiState.LoggedIn(
        TMDBAccount.LoggedIn(AccountDetailsFactory.Pinkman()),
      ),
      isJellyseerrEnabled = true,
    ),
  )
}
