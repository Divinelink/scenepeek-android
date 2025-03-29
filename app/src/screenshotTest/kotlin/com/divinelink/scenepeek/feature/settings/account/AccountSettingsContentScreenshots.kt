package com.divinelink.scenepeek.feature.settings.account

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.PreviewParameter
import com.divinelink.core.ui.Previews
import com.divinelink.feature.settings.app.account.AccountSettingsContentPreview
import com.divinelink.feature.settings.app.account.AccountSettingsViewState
import com.divinelink.feature.settings.provider.AccountDetailsParameterProvider

@Previews
@Composable
fun AccountSettingsContentScreenshots(
  @PreviewParameter(AccountDetailsParameterProvider::class) uiState: AccountSettingsViewState,
) {
  AccountSettingsContentPreview(uiState = uiState)
}
