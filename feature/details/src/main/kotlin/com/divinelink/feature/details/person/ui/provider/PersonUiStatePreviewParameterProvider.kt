package com.divinelink.feature.details.person.ui.provider

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.divinelink.core.fixtures.details.person.PersonDetailsFactory
import com.divinelink.core.fixtures.model.person.credit.PersonCastCreditFactory
import com.divinelink.feature.details.person.ui.PersonDetailsUiState
import com.divinelink.feature.details.person.ui.PersonUiState

class PersonUiStatePreviewParameterProvider : PreviewParameterProvider<PersonUiState> {
  override val values: Sequence<PersonUiState> = sequenceOf(
    PersonUiState(
      personDetails = PersonDetailsUiState.Data.Visible(PersonDetailsFactory.steveCarell()),
    ),
    // Sorry for making you dead Steve Carell, it's only for testing purposes!
    PersonUiState(
      personDetails = PersonDetailsUiState.Data.Visible(
        PersonDetailsFactory.steveCarell().copy(deathday = "2022-05-16"),
      ),
    ),
    PersonUiState(
      personDetails = PersonDetailsUiState.Data.Visible(
        PersonDetailsFactory.steveCarell().copy(biography = null),
      ),
    ),
    PersonUiState(
      personDetails = PersonDetailsUiState.Data.Visible(PersonDetailsFactory.steveCarell()),
      credits = PersonCastCreditFactory.all(),
    ),
  )
}
