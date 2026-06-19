package com.divinelink.feature.awards.category.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.PreviewParameter
import com.divinelink.core.navigation.route.Navigation
import com.divinelink.core.ui.Previews
import com.divinelink.core.ui.blankslate.BlankSlate
import com.divinelink.core.ui.components.LoadingContent
import com.divinelink.core.ui.composition.PreviewLocalProvider
import com.divinelink.feature.awards.category.AwardCategoryAction
import com.divinelink.feature.awards.category.AwardCategoryUiState
import com.divinelink.feature.awards.category.ui.provider.AwardCategoryUiStateParameterProvider

@Composable
fun AwardCategoryContent(
  uiState: AwardCategoryUiState,
  action: (AwardCategoryAction) -> Unit,
  onNavigate: (Navigation) -> Unit,
) {
  when {
    uiState.loading -> LoadingContent()
    uiState.error != null -> BlankSlate(
      uiState = uiState.error,
      onRetry = { action(AwardCategoryAction.OnRetry) },
    )
    else -> AwardCategoriesListContent(
      uiState = uiState,
      action = action,
      onNavigate = onNavigate,
    )
  }
}

@Composable
@Previews
fun AwardCategoryContentPreview(
  @PreviewParameter(AwardCategoryUiStateParameterProvider::class) state: AwardCategoryUiState,
) {
  PreviewLocalProvider {
    AwardCategoryContent(
      uiState = state,
      action = {},
      onNavigate = {},
    )
  }
}
