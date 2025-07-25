package com.divinelink.scenepeek.feature.lists

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.PreviewParameter
import com.divinelink.core.ui.Previews
import com.divinelink.feature.lists.create.CreateListUiState
import com.divinelink.feature.lists.create.ui.CreateListScaffoldPreview
import com.divinelink.feature.lists.create.ui.provider.CreateListUiStateParameterProvider

@Previews
@Composable
fun CreateListScaffoldScreenshots(
  @PreviewParameter(CreateListUiStateParameterProvider::class) uiState: CreateListUiState,
) {
  CreateListScaffoldPreview(uiState)
}
