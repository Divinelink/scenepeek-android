package com.divinelink.core.ui.provider

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.divinelink.core.commons.ExcludeFromKoverReport
import com.divinelink.core.fixtures.model.media.MediaItemFactory
import com.divinelink.core.model.media.MediaItem

@ExcludeFromKoverReport
class MediaItemParameterProvider : PreviewParameterProvider<MediaItem.Media> {
  override val values: Sequence<MediaItem.Media> = sequenceOf(
    MediaItemFactory.theOffice(),
    MediaItemFactory.FightClub(),
    MediaItemFactory.theOffice().copy(accountRating = 10),
    MediaItemFactory.theOffice().copy(
      name = "The Office with a very long name",
      accountRating = 10,
    ),
    MediaItemFactory.FightClub().copy(accountRating = 8),
  )
}
