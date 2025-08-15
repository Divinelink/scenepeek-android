package com.divinelink.scenepeek.feature.lists

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.PreviewParameter
import com.android.tools.screenshot.PreviewTest
import com.divinelink.core.ui.Previews
import com.divinelink.feature.lists.details.ListDetailsUiState
import com.divinelink.feature.lists.details.ui.ListDetailsContentPreview
import com.divinelink.feature.lists.details.ui.provider.ListDetailsUiStateParameterProvider

@Composable
@PreviewTest
@Previews
fun ListDetailsContentScreenshots(
  @PreviewParameter(ListDetailsUiStateParameterProvider::class) state: ListDetailsUiState,
) {
  ListDetailsContentPreview(state)
}
