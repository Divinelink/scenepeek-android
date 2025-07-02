package com.divinelink.scenepeek.feature.lists

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.PreviewParameter
import com.divinelink.core.ui.Previews
import com.divinelink.feature.lists.ListsUiState
import com.divinelink.feature.lists.ui.ListsContentPreview
import com.divinelink.feature.lists.ui.provider.ListsUiStateParameterProvider

@Composable
@Previews
fun UserListsContentScreenshots(
  @PreviewParameter(ListsUiStateParameterProvider::class) state: ListsUiState,
) {
  ListsContentPreview(state)
}
