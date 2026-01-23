package com.divinelink.scenepeek.feature.season

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.PreviewParameter
import com.divinelink.core.ui.Previews
import com.divinelink.feature.season.SeasonUiState
import com.divinelink.feature.season.ui.SeasonContentPreview
import com.divinelink.feature.season.ui.provider.SeasonUiStateParameterProvider

@Previews
@Composable
fun SeasonContentScreenshots(
  @PreviewParameter(SeasonUiStateParameterProvider::class) uiState: SeasonUiState,
) {
  SeasonContentPreview(uiState)
}
