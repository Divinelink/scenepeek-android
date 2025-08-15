package com.divinelink.scenepeek.core.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.PreviewParameter
import com.android.tools.screenshot.PreviewTest
import com.divinelink.core.model.media.MediaItem
import com.divinelink.core.ui.DetailedMediaItemPreview
import com.divinelink.core.ui.Previews
import com.divinelink.core.ui.provider.MediaItemParameterProvider

@Previews
@PreviewTest
@Composable
fun DetailedMediaItemScreenshots(
  @PreviewParameter(MediaItemParameterProvider::class) mediaItem: MediaItem.Media,
) {
  DetailedMediaItemPreview(mediaItem)
}
