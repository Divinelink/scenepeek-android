package com.divinelink.scenepeek.feature.media.lists

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.PreviewParameter
import com.divinelink.core.ui.Previews
import com.divinelink.feature.search.MediaListsUiState
import com.divinelink.feature.search.ui.MediaListsContentPreview
import com.divinelink.feature.search.ui.provider.MediaListsUiStateParameterProvider

@Previews
@Composable
fun MediaListsContentScreenshots(
  @PreviewParameter(MediaListsUiStateParameterProvider::class) uiState: MediaListsUiState,
) {
  MediaListsContentPreview(uiState)
}
