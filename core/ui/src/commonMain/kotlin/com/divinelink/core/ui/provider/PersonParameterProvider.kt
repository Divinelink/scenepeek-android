package com.divinelink.core.ui.provider

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.divinelink.core.commons.ExcludeFromKoverReport
import com.divinelink.core.model.credits.PersonRole
import com.divinelink.core.model.details.Person

@ExcludeFromKoverReport
class PersonParameterProvider : PreviewParameterProvider<Person> {
  override val values: Sequence<Person> = sequenceOf(
    Person(
      id = 94622,
      name = "Brian Baumgartner",
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
      knownForDepartment = "Acting",
      profilePath = "/1O7ECkD4mOKAgMAbQADBpTKBzOP.jpg",
    ),
    Person(
      id = 94622,
      name = "Brian Baumgartner",
      role = listOf(
        PersonRole.MovieActor(
          "Kevin Malone",
        ),
      ),
      knownForDepartment = "Acting",
      profilePath = "/1O7ECkD4mOKAgMAbQADBpTKBzOP.jpg",
    ),
  )
}
