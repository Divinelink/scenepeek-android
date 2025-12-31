package com.divinelink.feature.settings.provider

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.divinelink.core.commons.ExcludeFromKoverReport
import com.divinelink.core.fixtures.model.account.AccountDetailsFactory
import com.divinelink.core.fixtures.model.jellyseerr.JellyseerrProfileFactory
import com.divinelink.core.model.account.TMDBAccount
import com.divinelink.feature.settings.app.account.AccountSettingsViewState

@ExcludeFromKoverReport
class AccountDetailsParameterProvider : PreviewParameterProvider<AccountSettingsViewState?> {
  override val values: Sequence<AccountSettingsViewState?> = sequenceOf(
    AccountSettingsViewState(
      tmdbAccount = TMDBAccount.LoggedIn(AccountDetailsFactory.Pinkman()),
      alertDialogUiState = null,
      jellyseerrProfile = JellyseerrProfileFactory.jellyseerr(),
    ),
    AccountSettingsViewState(
      tmdbAccount = TMDBAccount.LoggedIn(
        AccountDetailsFactory.Pinkman().copy(tmdbAvatarPath = null),
      ),
      alertDialogUiState = null,
      jellyseerrProfile = JellyseerrProfileFactory.jellyseerr(),
    ),
    AccountSettingsViewState(
      tmdbAccount = TMDBAccount.Anonymous,
      alertDialogUiState = null,
      jellyseerrProfile = null,
    ),
    AccountSettingsViewState(
      tmdbAccount = TMDBAccount.Anonymous,
      alertDialogUiState = null,
      jellyseerrProfile = null,
    ),
    AccountSettingsViewState(
      tmdbAccount = TMDBAccount.Anonymous,
      alertDialogUiState = null,
      jellyseerrProfile = JellyseerrProfileFactory.jellyfin(),
    ),
  )
}
