package com.divinelink.scenepeek.feature.awards

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.PreviewParameter
import com.divinelink.core.ui.Previews

@Previews
@Composable
fun AwardsContentScreenshots(
  @PreviewParameter(AwardsUiStateParameterProvider::class) uiState: AwardsUiState,
) {
  AwardsContentPreview(uiState)
}
