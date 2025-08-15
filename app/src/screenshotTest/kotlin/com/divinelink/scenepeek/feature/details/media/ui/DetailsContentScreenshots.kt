package com.divinelink.scenepeek.feature.details.media.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.PreviewParameter
import com.android.tools.screenshot.PreviewTest
import com.divinelink.core.ui.Previews
import com.divinelink.feature.details.media.ui.DetailsContentPreview
import com.divinelink.feature.details.media.ui.DetailsViewState
import com.divinelink.feature.details.media.ui.provider.DetailsViewStateProvider

@Composable
@PreviewTest
@Previews
fun DetailsContentScreenshots(
  @PreviewParameter(DetailsViewStateProvider::class) viewState: DetailsViewState,
) {
  DetailsContentPreview(viewState)
}
