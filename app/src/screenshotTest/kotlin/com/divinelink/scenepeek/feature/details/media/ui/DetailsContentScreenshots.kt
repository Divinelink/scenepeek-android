package com.divinelink.scenepeek.feature.details.media.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.PreviewParameter
import com.divinelink.core.ui.Previews
import com.divinelink.feature.details.media.ui.DetailsContentPreview
import com.divinelink.feature.details.media.ui.DetailsViewState
import com.divinelink.feature.details.media.ui.provider.DetailsViewStateProvider

@Composable
@Previews
fun DetailsContentScreenshots(
  @PreviewParameter(DetailsViewStateProvider::class) viewState: DetailsViewState,
) {
  DetailsContentPreview(viewState)
}
