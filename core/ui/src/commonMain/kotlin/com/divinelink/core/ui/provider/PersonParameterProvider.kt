package com.divinelink.core.ui.provider

import com.divinelink.core.commons.ExcludeFromKoverReport
import com.divinelink.core.model.credits.PersonRole
import com.divinelink.core.model.details.Person
import org.jetbrains.compose.ui.tooling.preview.PreviewParameterProvider

@ExcludeFromKoverReport
class PersonParameterProvider : PreviewParameterProvider<Person> {
  override val values: Sequence<Person> = sequenceOf(
    Person(
      id = 94622,
      name = "Brian Baumgartner",
      role = listOf(
        PersonRole.SeriesActor(
          "Kevin Malone",
          totalEpisodes = 217,
        ),
        PersonRole.SeriesActor(
          "Self",
          totalEpisodes = 10,
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
