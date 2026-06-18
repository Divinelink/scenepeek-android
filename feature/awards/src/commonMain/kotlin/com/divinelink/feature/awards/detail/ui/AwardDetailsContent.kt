package com.divinelink.feature.awards.detail.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.PreviewParameter
import com.divinelink.core.ui.Previews
import com.divinelink.core.ui.blankslate.BlankSlate
import com.divinelink.core.ui.components.LoadingContent
import com.divinelink.core.ui.composition.PreviewLocalProvider
import com.divinelink.feature.awards.detail.AwardDetailsAction
import com.divinelink.feature.awards.detail.AwardDetailsUiState
import com.divinelink.feature.awards.detail.ui.provider.AwardDetailsUiStateParameterProvider

@Composable
fun AwardDetailsContent(
  uiState: AwardDetailsUiState,
  action: (AwardDetailsAction) -> Unit,
) {
  when {
    uiState.loading -> LoadingContent()
    uiState.error != null -> BlankSlate(
      uiState = uiState.error,
      onRetry = { action(AwardDetailsAction.OnRetry) },
    )
    else -> AwardDetailsListContent(
      uiState = uiState,
      action = action,
    )
  }
}

@Composable
@Previews
fun AwardDetailsContentPreview(
  @PreviewParameter(AwardDetailsUiStateParameterProvider::class) state: AwardDetailsUiState,
) {
  PreviewLocalProvider {
    AwardDetailsContent(
      uiState = state,
      action = { },
    )
  }
}
