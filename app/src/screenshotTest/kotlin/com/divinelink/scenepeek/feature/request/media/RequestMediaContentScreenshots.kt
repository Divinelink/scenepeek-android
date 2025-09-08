package com.divinelink.scenepeek.feature.request.media

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.PreviewParameter
import com.divinelink.core.ui.Previews
import com.divinelink.feature.request.media.RequestMediaContentPreview
import com.divinelink.feature.request.media.RequestMediaUiState
import com.divinelink.feature.request.media.provider.RequestMediaUiStateProvider

@Composable
@Previews
fun RequestSeasonsContentScreenshots(
  @PreviewParameter(RequestMediaUiStateProvider::class) uiState: RequestMediaUiState,
) {
  RequestMediaContentPreview(uiState = uiState)
}
