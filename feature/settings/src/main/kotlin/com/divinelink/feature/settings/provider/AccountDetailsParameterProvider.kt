package com.divinelink.feature.settings.provider

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.divinelink.core.commons.ExcludeFromKoverReport
import com.divinelink.core.fixtures.model.account.AccountDetailsFactory
import com.divinelink.core.fixtures.model.jellyseerr.JellyseerrAccountDetailsFactory
import com.divinelink.feature.settings.app.account.AccountSettingsViewState

@ExcludeFromKoverReport
class AccountDetailsParameterProvider : PreviewParameterProvider<AccountSettingsViewState?> {
  override val values: Sequence<AccountSettingsViewState?> = sequenceOf(
    AccountSettingsViewState(
      accountDetails = AccountDetailsFactory.Pinkman(),
      alertDialogUiState = null,
      jellyseerrAccountDetails = JellyseerrAccountDetailsFactory.jellyseerr(),
    ),
    AccountSettingsViewState(
      accountDetails = AccountDetailsFactory.Pinkman().copy(tmdbAvatarPath = null),
      alertDialogUiState = null,
      jellyseerrAccountDetails = JellyseerrAccountDetailsFactory.jellyseerr(),
    ),
    AccountSettingsViewState(
      accountDetails = null,
      alertDialogUiState = null,
      jellyseerrAccountDetails = null,
    ),
    AccountSettingsViewState(
      accountDetails = null,
      alertDialogUiState = null,
      jellyseerrAccountDetails = null,
    ),
    AccountSettingsViewState(
      accountDetails = null,
      alertDialogUiState = null,
      jellyseerrAccountDetails = JellyseerrAccountDetailsFactory.jellyfin(),
    ),
  )
}
