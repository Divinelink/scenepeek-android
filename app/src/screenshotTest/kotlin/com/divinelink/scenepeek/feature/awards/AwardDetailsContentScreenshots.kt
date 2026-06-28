package com.divinelink.feature.awards.detail.screenshots

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.PreviewParameter
import com.divinelink.core.ui.Previews
import com.divinelink.feature.awards.detail.AwardDetailsUiState
import com.divinelink.feature.awards.detail.ui.AwardDetailsContentPreview
import com.divinelink.feature.awards.detail.ui.provider.AwardDetailsUiStateParameterProvider

@Previews
@Composable
fun AwardDetailsContentScreenshots(
  @PreviewParameter(AwardDetailsUiStateParameterProvider::class) uiState: AwardDetailsUiState,
) {
  AwardDetailsContentPreview(uiState)
}
