package com.divinelink.core.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.PreviewParameter
import com.divinelink.core.model.media.MediaItem
import com.divinelink.core.ui.components.MediaItemPreview
import com.divinelink.core.ui.components.MediaItemWithSubtitlePreview
import com.divinelink.core.ui.provider.MediaItemParameterProvider

@Composable
@Previews
fun MediaItemScreenshots(
  @PreviewParameter(MediaItemParameterProvider::class) mediaItem: MediaItem.Media,
) {
  MediaItemPreview(mediaItem)
}

@Composable
@Previews
fun MediaItemWithSubtitleScreenshots(
  @PreviewParameter(MediaItemParameterProvider::class) mediaItem: MediaItem.Media,
) {
  MediaItemWithSubtitlePreview(mediaItem)
}
