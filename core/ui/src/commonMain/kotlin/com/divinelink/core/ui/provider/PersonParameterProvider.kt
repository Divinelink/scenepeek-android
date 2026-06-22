package com.divinelink.core.ui.provider

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.divinelink.core.commons.ExcludeFromKoverReport
import com.divinelink.core.fixtures.details.person.PersonFactory
import com.divinelink.core.model.credits.PersonRole
import com.divinelink.core.model.media.MediaItem

@ExcludeFromKoverReport
class PersonParameterProvider : PreviewParameterProvider<MediaItem.Person> {
  override val values: Sequence<MediaItem.Person> = sequenceOf(
    PersonFactory.SeriesActor.brianBaumgartner.copy(
      role = listOf(
        PersonRole.SeriesActor(
          character = "Kevin Malone",
          totalEpisodes = 217,
          creditId = "123456789",
        ),
        PersonRole.SeriesActor(
          character = "Self",
          totalEpisodes = 10,
          creditId = "987654321",
        ),
      ),
    ),
    PersonFactory.SeriesActor.brianBaumgartner,
    PersonFactory.SeriesActor.angelaKinsey,
  )
}
