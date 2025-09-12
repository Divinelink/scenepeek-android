package com.divinelink.scenepeek.feature.requests

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.PreviewParameter
import com.divinelink.core.ui.Previews
import com.divinelink.feature.requests.RequestsUiState
import com.divinelink.feature.requests.ui.RequestsContentPreview
import com.divinelink.feature.requests.ui.provider.RequestsUiStateParameterProvider

@Previews
@Composable
fun RequestsContentScreenshots(
  @PreviewParameter(RequestsUiStateParameterProvider::class) uiState: RequestsUiState,
) {
  RequestsContentPreview(uiState)
}
