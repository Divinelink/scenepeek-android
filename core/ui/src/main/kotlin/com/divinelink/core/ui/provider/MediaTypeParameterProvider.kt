package com.divinelink.core.ui.provider

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.divinelink.core.model.media.MediaType

class MediaTypeParameterProvider : PreviewParameterProvider<MediaType> {
  override val values: Sequence<MediaType> = sequenceOf(
    MediaType.MOVIE,
    MediaType.TV,
    MediaType.PERSON,
    MediaType.UNKNOWN,
  )
}
