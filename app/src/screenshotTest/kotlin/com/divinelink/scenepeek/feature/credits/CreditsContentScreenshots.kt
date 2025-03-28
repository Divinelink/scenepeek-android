package com.divinelink.scenepeek.feature.credits

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.PreviewParameter
import com.divinelink.core.ui.Previews
import com.divinelink.feature.credits.provider.CreditsUiStateParameterProvider
import com.divinelink.feature.credits.ui.CreditsContentPreview
import com.divinelink.feature.credits.ui.CreditsUiState

@Previews
@Composable
fun CreditsContentScreenshots(
  @PreviewParameter(CreditsUiStateParameterProvider::class) uiState: CreditsUiState,
) {
  CreditsContentPreview(uiState)
}
