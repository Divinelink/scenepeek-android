package com.divinelink.ui.provider

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.tooling.preview.datasource.LoremIpsum
import com.divinelink.core.model.media.MediaItem
import com.divinelink.ui.provider.MediaItemParameterData.movie

class MediaItemPreviewParameterProvider : PreviewParameterProvider<MediaItem.Media> {
  override val values: Sequence<MediaItem.Media> = sequenceOf(
    movie
  )
}

private object MediaItemParameterData {

  val movie = MediaItem.Media.Movie(
    id = 0,
    posterPath = "",
    releaseDate = "2020-07-02",
    name = "Flight Club",
    rating = "9.4",
    overview = LoremIpsum(50).values.joinToString(),
    isFavorite = false,
  )
}
