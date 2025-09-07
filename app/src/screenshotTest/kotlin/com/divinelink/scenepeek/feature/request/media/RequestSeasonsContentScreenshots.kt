package com.divinelink.scenepeek.feature.request.media

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.PreviewParameter
import com.divinelink.core.ui.Previews
import com.divinelink.feature.request.media.RequestSeasonsContentPreview
import com.divinelink.feature.request.media.RequestSeasonsUiState
import com.divinelink.feature.request.media.provider.RequestSeasonUiStateProvider

@Composable
@Previews
fun RequestSeasonsContentScreenshots(
  @PreviewParameter(RequestSeasonUiStateProvider::class) uiState: RequestSeasonsUiState,
) {
  RequestSeasonsContentPreview(uiState = uiState)
}
