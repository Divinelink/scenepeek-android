package com.divinelink.core.ui.provider

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.divinelink.core.fixtures.model.media.MediaItemFactory
import com.divinelink.core.model.media.MediaItem

class MediaItemParameterProvider : PreviewParameterProvider<MediaItem.Media> {
  override val values: Sequence<MediaItem.Media> = sequenceOf(
    MediaItemFactory.theOffice(),
    MediaItemFactory.FightClub(),
  )
}
