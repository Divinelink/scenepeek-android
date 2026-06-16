package com.divinelink.feature.awards.popular.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.PreviewParameter
import com.divinelink.core.ui.Previews
import com.divinelink.core.ui.composition.PreviewLocalProvider
import com.divinelink.feature.awards.popular.AwardsAction
import com.divinelink.feature.awards.popular.AwardsUiState
import com.divinelink.feature.awards.popular.ui.provider.AwardsUiStateParameterProvider

@Composable
fun AwardsContent(
  uiState: AwardsUiState,
  action: (AwardsAction) -> Unit,
) {
}

@Composable
@Previews
fun AwardsContentPreview(
  @PreviewParameter(AwardsUiStateParameterProvider::class) state: AwardsUiState,
) {
  PreviewLocalProvider {
    AwardsContent(
      uiState = state,
      action = { },
    )
  }
}



