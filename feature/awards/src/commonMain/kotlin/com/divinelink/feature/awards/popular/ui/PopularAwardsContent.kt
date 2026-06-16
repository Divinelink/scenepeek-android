package com.divinelink.feature.awards.popular.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.PreviewParameter
import com.divinelink.core.ui.Previews
import com.divinelink.core.ui.composition.PreviewLocalProvider
import com.divinelink.feature.awards.popular.PopularAwardsAction
import com.divinelink.feature.awards.popular.PopularAwardsUiState
import com.divinelink.feature.awards.popular.ui.provider.AwardCategoriesUiStateParameterProvider

@Composable
fun AwardCategoriesContent(
  uiState: PopularAwardsUiState,
  action: (PopularAwardsAction) -> Unit,
) {
}

@Composable
@Previews
fun AwardCategoriesContentPreview(
  @PreviewParameter(AwardCategoriesUiStateParameterProvider::class) state: PopularAwardsUiState,
) {
  PreviewLocalProvider {
    AwardCategoriesContent(
      uiState = state,
      action = { },
    )
  }
}



