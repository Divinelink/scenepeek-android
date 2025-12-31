package com.divinelink.core.ui.provider

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.divinelink.core.commons.ExcludeFromKoverReport
import com.divinelink.core.fixtures.model.details.rating.RatingCountFactory
import com.divinelink.core.model.details.rating.RatingCount

@ExcludeFromKoverReport
class RatingCountParameterProvider : PreviewParameterProvider<RatingCount> {
  override val values: Sequence<RatingCount> = sequenceOf(
    RatingCountFactory.tmdb(),
    RatingCountFactory.imdb(),
    RatingCountFactory.trakt(),
    RatingCountFactory.full(),
  )
}
