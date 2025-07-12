package com.divinelink.scenepeek.feature.add.to.account.list.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.PreviewParameter
import com.divinelink.core.ui.Previews
import com.divinelink.feature.add.to.account.list.AddToListUiState
import com.divinelink.feature.add.to.account.list.ui.AddToListContentPreview
import com.divinelink.feature.add.to.account.list.ui.provider.AddToListUiStateParameterProvider

@Composable
@Previews
fun AddToListContentScreenshots(
  @PreviewParameter(AddToListUiStateParameterProvider::class) state: AddToListUiState,
) {
  AddToListContentPreview(state)
}
