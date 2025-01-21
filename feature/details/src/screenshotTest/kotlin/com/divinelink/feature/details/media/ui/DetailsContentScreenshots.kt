package com.divinelink.feature.details.media.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.PreviewParameter
import com.divinelink.core.ui.Previews

@Composable
@Previews
fun DetailsContentScreenshots(
  @PreviewParameter(DetailsViewStateProvider::class) viewState: DetailsViewState,
) {
  DetailsContentPreview(viewState)
}
