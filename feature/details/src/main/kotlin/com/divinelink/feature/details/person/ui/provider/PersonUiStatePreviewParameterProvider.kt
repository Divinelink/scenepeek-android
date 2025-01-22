package com.divinelink.feature.details.person.ui.provider

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.divinelink.core.fixtures.details.person.PersonDetailsFactory
import com.divinelink.core.fixtures.model.person.credit.PersonCastCreditFactory
import com.divinelink.feature.details.person.ui.PersonDetailsUiState
import com.divinelink.feature.details.person.ui.PersonForm
import com.divinelink.feature.details.person.ui.PersonUiState
import com.divinelink.feature.details.person.ui.tab.PersonTab

class PersonUiStatePreviewParameterProvider : PreviewParameterProvider<PersonUiState> {
  override val values: Sequence<PersonUiState> = sequenceOf(
    PersonUiState(
      selectedTabIndex = 0,
      tabs = PersonTab.entries,
      forms = mapOf(
        0 to PersonForm.About(PersonDetailsUiState.Loading),
        1 to PersonForm.Movies(emptyList()),
        2 to PersonForm.TvShows(emptyList()),
      ),
      personDetails = PersonDetailsUiState.Data.Visible(PersonDetailsFactory.steveCarell()),
    ),
    // Sorry for making you dead Steve Carell, it's only for testing purposes!
    PersonUiState(
      selectedTabIndex = 0,
      tabs = PersonTab.entries,
      forms = mapOf(
        0 to PersonForm.About(PersonDetailsUiState.Loading),
        1 to PersonForm.Movies(emptyList()),
        2 to PersonForm.TvShows(emptyList()),
      ),
      personDetails = PersonDetailsUiState.Data.Visible(
        PersonDetailsFactory.steveCarell().copy(deathday = "2022-05-16"),
      ),
    ),
    PersonUiState(
      selectedTabIndex = 0,
      tabs = PersonTab.entries,
      forms = mapOf(
        0 to PersonForm.About(PersonDetailsUiState.Loading),
        1 to PersonForm.Movies(emptyList()),
        2 to PersonForm.TvShows(emptyList()),
      ),
      personDetails = PersonDetailsUiState.Data.Visible(
        PersonDetailsFactory.steveCarell().copy(biography = null),
      ),
    ),
    PersonUiState(
      selectedTabIndex = 0,
      forms = mapOf(
        0 to PersonForm.About(PersonDetailsUiState.Loading),
        1 to PersonForm.Movies(emptyList()),
        2 to PersonForm.TvShows(emptyList()),
      ),
      tabs = PersonTab.entries,
      personDetails = PersonDetailsUiState.Data.Visible(PersonDetailsFactory.steveCarell()),
      knownForCredits = PersonCastCreditFactory.all(),
    ),
  )
}
