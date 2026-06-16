package com.divinelink.scenepeek.feature.awards

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.PreviewParameter
import com.android.tools.screenshot.PreviewTest
import com.divinelink.core.ui.Previews
import com.divinelink.feature.awards.categories.AwardCategoriesUiState
import com.divinelink.feature.awards.categories.ui.provider.AwardCategoriesUiStateParameterProvider

@Previews
@Composable
fun AwardsContentScreenshots(
  @PreviewParameter(AwardsUiStateParameterProvider::class) uiState: AwardsUiState,
) {
  AwardsContentPreview(uiState)
}
