package com.divinelink.feature.details.person.ui.provider

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.tooling.preview.datasource.LoremIpsum
import com.divinelink.core.model.credits.PersonRole
import com.divinelink.core.model.details.Person
import com.divinelink.core.model.details.person.PersonDetails
import com.divinelink.core.model.person.Gender
import com.divinelink.feature.details.person.ui.PersonDetailsUiState
import com.divinelink.feature.details.person.ui.PersonUiState

class PersonUiStatePreviewParameterProvider : PreviewParameterProvider<PersonUiState> {
  override val values: Sequence<PersonUiState> = sequenceOf(
    PersonUiState(
      personDetails = PersonDetailsUiState.Data.Visible(PersonUiStateData.steveCarell),
    ),
    // Sorry for making you dead Steve Carell, it's only for testing purposes!
    PersonUiState(
      personDetails = PersonDetailsUiState.Data.Visible(
        PersonUiStateData.steveCarell.copy(deathday = "2022-05-16"),
      ),
    ),
    PersonUiState(
      personDetails = PersonDetailsUiState.Data.Visible(
        PersonUiStateData.steveCarell.copy(biography = null),
      ),
    ),
  )

  private object PersonUiStateData {
    val steveCarell = PersonDetails(
      person = Person(
        id = 4495,
        name = "Steve carell",
        profilePath = "/dzJtsLspH5Bf8Tvw7OQC47ETNfJ.jpg",
        gender = Gender.MALE,
        role = listOf(PersonRole.Unknown),
        knownForDepartment = "Acting",
      ),
      biography = LoremIpsum(50).values.joinToString(),
      birthday = "1962-08-16",
      deathday = null,
      placeOfBirth = "Concord, Massachusetts, USA",
      homepage = null,
      alsoKnownAs = emptyList(),
      imdbId = "nm0136797",
      popularity = 77.108,
      insertedAt = "1642185600",
    )
  }
}
