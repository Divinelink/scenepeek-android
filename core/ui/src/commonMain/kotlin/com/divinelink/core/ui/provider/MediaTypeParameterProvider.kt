package com.divinelink.core.ui.provider

import com.divinelink.core.commons.ExcludeFromKoverReport
import com.divinelink.core.model.media.MediaType
import org.jetbrains.compose.ui.tooling.preview.PreviewParameterProvider

@ExcludeFromKoverReport
class MediaTypeParameterProvider : PreviewParameterProvider<MediaType> {
  override val values: Sequence<MediaType> = sequenceOf(
    MediaType.MOVIE,
    MediaType.TV,
    MediaType.PERSON,
    MediaType.UNKNOWN,
  )
}
