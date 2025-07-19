package com.divinelink.feature.details.person.ui.provider

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.divinelink.core.commons.ExcludeFromKoverReport
import com.divinelink.core.fixtures.details.person.PersonDetailsFactory
import com.divinelink.core.fixtures.model.person.credit.GroupedPersonCreditsSample
import com.divinelink.core.fixtures.model.person.credit.PersonCastCreditFactory
import com.divinelink.core.model.tab.PersonTab
import com.divinelink.feature.details.person.ui.PersonDetailsUiState
import com.divinelink.feature.details.person.ui.PersonForm
import com.divinelink.feature.details.person.ui.PersonUiState
import com.divinelink.feature.details.person.ui.filter.CreditFilter

@ExcludeFromKoverReport
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
        1 to PersonForm.Movies(GroupedPersonCreditsSample.movies()),
        2 to PersonForm.TvShows(emptyMap()),
      ),
      filteredCredits = mapOf(
        1 to GroupedPersonCreditsSample.movies(),
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
        1 to PersonForm.Movies(GroupedPersonCreditsSample.movies()),
        2 to PersonForm.TvShows(emptyMap()),
      ),
      filteredCredits = mapOf(
        1 to GroupedPersonCreditsSample.movies(),
      ),
      filters = mapOf(
        1 to emptyList(),
        2 to emptyList(),
      ),
      tabs = PersonTab.entries,
      knownForCredits = PersonCastCreditFactory.all(),
    ),
    // Filtered Movies
    PersonUiState(
      selectedTabIndex = 1,
      forms = mapOf(
        0 to PersonForm.About(
          PersonDetailsUiState.Data.Visible(PersonDetailsFactory.steveCarell()),
        ),
        1 to PersonForm.Movies(GroupedPersonCreditsSample.movies()),
        2 to PersonForm.TvShows(emptyMap()),
      ),
      filteredCredits = mapOf(
        1 to GroupedPersonCreditsSample.movies().filterKeys { it == "Acting" },
      ),
      filters = mapOf(
        1 to listOf(
          CreditFilter.Department(
            department = "Acting",
            size = 3,
          ),
        ),
        2 to emptyList(),
      ),
      tabs = PersonTab.entries,
      knownForCredits = PersonCastCreditFactory.all(),
    ),
    // Non filtered TV Shows
    PersonUiState(
      selectedTabIndex = 2,
      forms = mapOf(
        0 to PersonForm.About(
          PersonDetailsUiState.Data.Visible(PersonDetailsFactory.steveCarell()),
        ),
        1 to PersonForm.Movies(emptyMap()),
        2 to PersonForm.TvShows(GroupedPersonCreditsSample.tvShows()),
      ),
      filteredCredits = mapOf(
        2 to GroupedPersonCreditsSample.tvShows(),
      ),
      filters = mapOf(
        1 to emptyList(),
        2 to emptyList(),
      ),
      tabs = PersonTab.entries,
      knownForCredits = PersonCastCreditFactory.all(),
    ),
    // Grid TV Shows
    PersonUiState(
      selectedTabIndex = 2,
      forms = mapOf(
        0 to PersonForm.About(
          PersonDetailsUiState.Data.Visible(PersonDetailsFactory.steveCarell()),
        ),
        1 to PersonForm.Movies(emptyMap()),
        2 to PersonForm.TvShows(GroupedPersonCreditsSample.tvShows()),
      ),
      filteredCredits = mapOf(
        2 to GroupedPersonCreditsSample.tvShows(),
      ),
      filters = mapOf(
        1 to emptyList(),
        2 to emptyList(),
      ),
      tabs = PersonTab.entries,
      knownForCredits = PersonCastCreditFactory.all(),
    ),
    // Filtered TV Shows
    PersonUiState(
      selectedTabIndex = 2,
      forms = mapOf(
        0 to PersonForm.About(
          PersonDetailsUiState.Data.Visible(PersonDetailsFactory.steveCarell()),
        ),
        1 to PersonForm.Movies(emptyMap()),
        2 to PersonForm.TvShows(GroupedPersonCreditsSample.tvShows()),
      ),
      filteredCredits = mapOf(
        2 to GroupedPersonCreditsSample.tvShows().filterKeys { it == "Acting" },
      ),
      filters = mapOf(
        1 to emptyList(),
        2 to listOf(
          CreditFilter.Department(
            department = "Acting",
            size = 1,
          ),
        ),
      ),
      tabs = PersonTab.entries,
      knownForCredits = PersonCastCreditFactory.all(),
    ),
    // Empty Movies
    PersonUiState(
      selectedTabIndex = 1,
      forms = mapOf(
        0 to PersonForm.About(
          PersonDetailsUiState.Data.Visible(PersonDetailsFactory.steveCarell()),
        ),
        1 to PersonForm.Movies(emptyMap()),
        2 to PersonForm.TvShows(emptyMap()),
      ),
      filters = mapOf(
        1 to emptyList(),
        2 to listOf(
          CreditFilter.Department(
            department = "Acting",
            size = 10,
          ),
        ),
      ),
      tabs = PersonTab.entries,
      knownForCredits = PersonCastCreditFactory.all(),
    ),
    // Empty TV Shows
    PersonUiState(
      selectedTabIndex = 2,
      forms = mapOf(
        0 to PersonForm.About(
          PersonDetailsUiState.Data.Visible(PersonDetailsFactory.steveCarell()),
        ),
        1 to PersonForm.Movies(emptyMap()),
        2 to PersonForm.TvShows(emptyMap()),
      ),
      filters = mapOf(
        1 to emptyList(),
        2 to listOf(
          CreditFilter.Department(
            department = "Acting",
            size = 10,
          ),
        ),
      ),
      tabs = PersonTab.entries,
      knownForCredits = PersonCastCreditFactory.all(),
    ),
  )
}
