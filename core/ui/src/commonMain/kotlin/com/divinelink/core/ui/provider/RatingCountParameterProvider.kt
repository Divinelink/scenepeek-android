package com.divinelink.core.ui.provider

import com.divinelink.core.commons.ExcludeFromKoverReport
import com.divinelink.core.fixtures.model.details.rating.RatingCountFactory
import com.divinelink.core.model.details.rating.RatingCount
import org.jetbrains.compose.ui.tooling.preview.PreviewParameterProvider

@ExcludeFromKoverReport
class RatingCountParameterProvider : PreviewParameterProvider<RatingCount> {
  override val values: Sequence<RatingCount> = sequenceOf(
    RatingCountFactory.tmdb(),
    RatingCountFactory.imdb(),
    RatingCountFactory.trakt(),
    RatingCountFactory.full(),
  )
}
