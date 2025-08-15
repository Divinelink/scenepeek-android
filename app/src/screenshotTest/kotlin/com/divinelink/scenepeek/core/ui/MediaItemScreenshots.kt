package com.divinelink.scenepeek.core.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.PreviewParameter
import com.android.tools.screenshot.PreviewTest
import com.divinelink.core.model.media.MediaItem
import com.divinelink.core.ui.Previews
import com.divinelink.core.ui.components.MediaItemFullPreview
import com.divinelink.core.ui.components.MediaItemPreview
import com.divinelink.core.ui.components.MediaItemWithDatePreview
import com.divinelink.core.ui.components.MediaItemWithSubtitlePreview
import com.divinelink.core.ui.provider.MediaItemParameterProvider

@Composable
@PreviewTest
@Previews
fun MediaItemScreenshots(
  @PreviewParameter(MediaItemParameterProvider::class) mediaItem: MediaItem.Media,
) {
  MediaItemPreview(mediaItem)
}

@Composable
@PreviewTest
@Previews
fun MediaItemWithSubtitleScreenshots(
  @PreviewParameter(MediaItemParameterProvider::class) mediaItem: MediaItem.Media,
) {
  MediaItemWithSubtitlePreview(mediaItem)
}

@Composable
@PreviewTest
@Previews
fun MediaItemWithDateScreenshots() {
  MediaItemWithDatePreview()
}

@Composable
@PreviewTest
@Previews
fun MediaItemFullScreenshots() {
  MediaItemFullPreview()
}
