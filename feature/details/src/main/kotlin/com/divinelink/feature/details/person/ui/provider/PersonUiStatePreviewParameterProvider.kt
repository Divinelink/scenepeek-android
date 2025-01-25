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
        0 to PersonForm.About(
          PersonDetailsUiState.Data.Visible(PersonDetailsFactory.steveCarell()),
        ),
        1 to PersonForm.Movies(emptyMap()),
        2 to PersonForm.TvShows(emptyMap()),
      ),
      filters = mapOf(
        1 to emptyList(),
        2 to emptyList(),
      ),
    ),
    // Sorry for making you dead Steve Carell, it's only for testing purposes!
    PersonUiState(
      selectedTabIndex = 0,
      tabs = PersonTab.entries,
      filters = mapOf(
        1 to emptyList(),
        2 to emptyList(),
      ),
      forms = mapOf(
        0 to PersonForm.About(
          PersonDetailsUiState.Data.Visible(
            PersonDetailsFactory.steveCarell().copy(deathday = "2022-05-16"),
          ),
        ),
        1 to PersonForm.Movies(emptyMap()),
        2 to PersonForm.TvShows(emptyMap()),
      ),
    ),
    PersonUiState(
      selectedTabIndex = 0,
      tabs = PersonTab.entries,
      forms = mapOf(
        0 to PersonForm.About(
          PersonDetailsUiState.Data.Visible(
            PersonDetailsFactory.steveCarell().copy(biography = null),
          ),
        ),
        1 to PersonForm.Movies(emptyMap()),
        2 to PersonForm.TvShows(emptyMap()),
      ),
      filters = mapOf(
        1 to emptyList(),
        2 to emptyList(),
      ),
    ),
    PersonUiState(
      selectedTabIndex = 0,
      forms = mapOf(
        0 to PersonForm.About(
          PersonDetailsUiState.Data.Visible(PersonDetailsFactory.steveCarell()),
        ),
        1 to PersonForm.Movies(emptyMap()),
        2 to PersonForm.TvShows(emptyMap()),
      ),
      filters = mapOf(
        1 to emptyList(),
        2 to emptyList(),
      ),
      tabs = PersonTab.entries,
      knownForCredits = PersonCastCreditFactory.all(),
    ),
    PersonUiState(
      selectedTabIndex = 1,
      forms = mapOf(
        0 to PersonForm.About(
          PersonDetailsUiState.Data.Visible(PersonDetailsFactory.steveCarell()),
        ),
        1 to PersonForm.Movies(emptyMap()), // TODO add credits
        2 to PersonForm.TvShows(emptyMap()),
      ),
      filters = mapOf(
        1 to emptyList(),
        2 to emptyList(),
      ),
      tabs = PersonTab.entries,
      knownForCredits = PersonCastCreditFactory.all(),
    ),
  )
}
