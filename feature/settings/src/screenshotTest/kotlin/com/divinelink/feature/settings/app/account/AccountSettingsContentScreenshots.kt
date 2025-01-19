package com.divinelink.feature.settings.app.account

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.PreviewParameter
import com.divinelink.core.ui.Previews

@Previews
@Composable
fun AccountSettingsContentScreenshots(
  @PreviewParameter(AccountDetailsParameterProvider::class) uiState: AccountSettingsViewState,
) {
  AccountSettingsContentPreview(uiState = uiState)
}
