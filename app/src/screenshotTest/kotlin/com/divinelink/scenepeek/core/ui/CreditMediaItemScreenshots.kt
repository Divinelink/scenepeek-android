package com.divinelink.scenepeek.core.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.PreviewParameter
import com.divinelink.core.model.media.MediaItem
import com.divinelink.core.ui.CreditMediaItemPreview
import com.divinelink.core.ui.Previews
import com.divinelink.core.ui.provider.MediaItemParameterProvider

@Previews
@Composable
fun CreditMediaItemScreenshots(
  @PreviewParameter(MediaItemParameterProvider::class) mediaItem: MediaItem.Media,
) {
  CreditMediaItemPreview(mediaItem)
}
