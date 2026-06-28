package com.divinelink.feature.awards.category.screenshots

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.PreviewParameter
import com.divinelink.core.ui.Previews
import com.divinelink.feature.awards.category.AwardCategoryUiState
import com.divinelink.feature.awards.category.ui.AwardCategoryContentPreview
import com.divinelink.feature.awards.category.ui.provider.AwardCategoryUiStateParameterProvider

@Previews
@Composable
fun AwardCategoryContentScreenshots(
  @PreviewParameter(AwardCategoryUiStateParameterProvider::class) uiState: AwardCategoryUiState,
) {
  AwardCategoryContentPreview(uiState)
}
