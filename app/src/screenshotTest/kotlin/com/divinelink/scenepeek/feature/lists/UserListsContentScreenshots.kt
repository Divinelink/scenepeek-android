package com.divinelink.scenepeek.feature.lists

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.PreviewParameter
import com.android.tools.screenshot.PreviewTest
import com.divinelink.core.ui.Previews
import com.divinelink.feature.lists.user.ListsUiState
import com.divinelink.feature.lists.user.ui.ListsContentGridPreview
import com.divinelink.feature.lists.user.ui.ListsContentListPreview
import com.divinelink.feature.lists.user.ui.provider.ListsUiStateParameterProvider

@PreviewTest
@Previews
@Composable
fun UserListsContentScreenshots(
  @PreviewParameter(ListsUiStateParameterProvider::class) state: ListsUiState,
) {
  ListsContentListPreview(state)
}

@PreviewTest
@Previews
@Composable
fun UserListsGridContentScreenshots() {
  ListsContentGridPreview()
}
