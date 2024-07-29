package com.divinelink.feature.details.person.ui.provider

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.tooling.preview.datasource.LoremIpsum
import com.divinelink.core.model.credits.PersonRole
import com.divinelink.core.model.details.Person
import com.divinelink.core.model.details.person.PersonDetails
import com.divinelink.core.model.person.Gender
import com.divinelink.feature.details.person.ui.PersonUiState

class PersonUiStatePreviewParameterProvider : PreviewParameterProvider<PersonUiState.Success> {
  override val values: Sequence<PersonUiState.Success> = sequenceOf(
    PersonUiState.Success(
      personDetails = PersonUiStateData.steveCarell,
    ),
    // Sorry for making you dead Steve Carell, it's only for testing purposes!
    PersonUiState.Success(
      personDetails = PersonUiStateData.steveCarell.copy(deathday = "2022-05-16"),
    ),
    PersonUiState.Success(
      personDetails = PersonUiStateData.steveCarell.copy(biography = null),
    ),
  )
}

private object PersonUiStateData {
  val steveCarell = PersonDetails(
    person = Person(
      id = 4495,
      name = "Steve carell",
      profilePath = "/dzJtsLspH5Bf8Tvw7OQC47ETNfJ.jpg",
      gender = Gender.MALE,
      role = PersonRole.Unknown,
    ),
    biography = LoremIpsum(50).values.joinToString(),
    birthday = "1962-08-16",
    deathday = null,
    placeOfBirth = "Concord, Massachusetts, USA",
    homepage = null,
    alsoKnownAs = emptyList(),
    imdbId = "nm0136797",
    popularity = 77.108,
    knownForDepartment = "Acting",
  )
}
