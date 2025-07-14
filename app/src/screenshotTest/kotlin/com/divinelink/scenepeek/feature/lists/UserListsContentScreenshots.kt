package com.divinelink.scenepeek.feature.lists

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.PreviewParameter
import com.divinelink.core.ui.Previews
import com.divinelink.feature.lists.user.ListsUiState
import com.divinelink.feature.lists.user.ui.ListsContentPreview
import com.divinelink.feature.lists.user.ui.provider.ListsUiStateParameterProvider

@Composable
@Previews
fun UserListsContentScreenshots(
  @PreviewParameter(ListsUiStateParameterProvider::class) state: ListsUiState,
) {
  ListsContentPreview(state)
}
