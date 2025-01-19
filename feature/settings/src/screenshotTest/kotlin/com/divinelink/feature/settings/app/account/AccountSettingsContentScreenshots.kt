package com.divinelink.feature.settings.app.account

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.PreviewParameter
import com.divinelink.core.ui.Previews
import com.divinelink.feature.settings.provider.AccountDetailsParameterProvider

@Previews
@Composable
fun AccountSettingsContentScreenshots(
  @PreviewParameter(AccountDetailsParameterProvider::class) uiState: AccountSettingsViewState,
) {
  AccountSettingsContentPreview(uiState = uiState)
}
