package com.divinelink.feature.details.person.ui

import com.divinelink.core.data.person.details.model.PersonDetailsResult
import com.divinelink.core.fixtures.details.person.PersonDetailsFactory
import com.divinelink.core.fixtures.model.person.credit.GroupedPersonCreditsSample
import com.divinelink.core.fixtures.model.person.credit.PersonCastCreditFactory
import com.divinelink.core.model.LayoutStyle
import com.divinelink.core.model.details.DetailsMenuOptions
import com.divinelink.core.model.details.person.GroupedPersonCredits
import com.divinelink.core.model.person.KnownForDepartment
import com.divinelink.core.model.person.credits.PersonCredit
import com.divinelink.core.navigation.arguments.PersonNavArguments
import com.divinelink.core.navigation.arguments.map
import com.divinelink.core.testing.MainDispatcherRule
import com.divinelink.core.testing.assertUiState
import com.divinelink.core.testing.expectUiStates
import com.divinelink.feature.details.person.ui.filter.CreditFilter
import com.divinelink.feature.details.person.ui.tab.PersonTab
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import kotlin.test.Test

class PersonViewModelTest {

  private var robot: PersonViewModelTestRobot = PersonViewModelTestRobot()

  @get:Rule
  val mainDispatcherRule = MainDispatcherRule()

  @Test
  fun `test initialise viewModel with error`() = runTest {
    robot
      .withNavArgs(
        PersonNavArguments(
          id = PersonDetailsFactory.steveCarell().person.id,
          knownForDepartment = null,
          name = null,
          profilePath = null,
          gender = null,
        ),
      )
      .buildViewModel()
      .assertUiState(
        createState(
          isLoading = true,
          isError = true,
        ),
      )
  }

  @Test
  fun `test initialise viewModel with success person details`() = runTest {
    robot
      .withNavArgs(PersonDetailsFactory.steveCarell().person.map())
      .mockFetchPersonDetailsUseCaseSuccess(
        PersonDetailsResult.DetailsSuccess(PersonDetailsFactory.steveCarell()),
      )
      .buildViewModel()
      .assertUiState(
        createState(
          personDetails = PersonDetailsUiState.Data.Visible(PersonDetailsFactory.steveCarell()),
        ),
      )
  }

  @Test
  fun `test initialise viewModel with error on details`() = runTest {
    robot
      .withNavArgs(PersonDetailsFactory.steveCarell().person.map())
      .mockFetchPersonDetailsUseCaseSuccess(PersonDetailsResult.DetailsFailure)
      .buildViewModel()
      .assertUiState(
        createState(
          isError = true,
          personDetails = PersonDetailsUiState.Data.Prefetch(
            PersonDetailsFactory.steveCarell().person,
          ),
        ),
      )
  }

  @Test
  fun `test updatePersonDetails when uiState is already success`() = runTest {
    val channel = Channel<Result<PersonDetailsResult>>()

    robot
      .withNavArgs(PersonDetailsFactory.steveCarell().person.map())
      .setupChannelForUseCase(channel)
      .buildViewModel()
      .expectUiStates(
        action = {
          launch {
            // Send the first emission
            channel.send(
              Result.success(
                PersonDetailsResult.DetailsSuccess(PersonDetailsFactory.steveCarell()),
              ),
            )

            // Send the second emission
            channel.send(
              Result.success(
                PersonDetailsResult.DetailsSuccess(
                  PersonDetailsFactory.steveCarell().copy(
                    person = PersonDetailsFactory.steveCarell().person.copy(name = "Michael Scarn"),
                  ),
                ),
              ),
            )
          }
        },
        uiStates = listOf(
          createState(
            personDetails = PersonDetailsUiState.Data.Prefetch(
              PersonDetailsFactory.steveCarell().person,
            ),
          ),
          createState(
            personDetails = PersonDetailsUiState.Data.Visible(PersonDetailsFactory.steveCarell()),
          ),
          createState(
            personDetails = PersonDetailsUiState.Data.Visible(
              PersonDetailsFactory.steveCarell().copy(
                person = PersonDetailsFactory.steveCarell().person.copy(name = "Michael Scarn"),
              ),
            ),
          ),
        ),
      )
  }

  @Test
  fun `test updateCredits when uiState is not yet success`() = runTest {
    robot
      .withNavArgs(PersonDetailsFactory.steveCarell().person.map())
      .mockFetchPersonDetailsUseCaseSuccess(
        PersonDetailsResult.CreditsSuccess(
          knownForCredits = PersonCastCreditFactory.knownFor(),
          knownForDepartment = KnownForDepartment.Acting.value,
          movies = GroupedPersonCreditsSample.movies(),
          tvShows = GroupedPersonCreditsSample.tvShows(),
        ),
      )
      .buildViewModel()
      .assertUiState(
        createState(
          personDetails = PersonDetailsUiState.Data.Prefetch(
            PersonDetailsFactory.steveCarell().person,
          ),
          credits = PersonCastCreditFactory.knownFor(),
          movies = GroupedPersonCreditsSample.movies(),
          tvShows = GroupedPersonCreditsSample.tvShows(),
          filteredCredits = mapOf(
            PersonTab.MOVIES.order to GroupedPersonCreditsSample.movies(),
            PersonTab.TV_SHOWS.order to GroupedPersonCreditsSample.tvShows(),
          ),
        ),
      )
  }

  @Test
  fun `test updateCredits when uiState is already success`() = runTest {
    val channel = Channel<Result<PersonDetailsResult>>()

    robot
      .withNavArgs(PersonDetailsFactory.steveCarell().person.map())
      .setupChannelForUseCase(channel)
      .buildViewModel()
      .expectUiStates(
        action = {
          launch {
            // Send the first emission
            channel.send(
              Result.success(
                PersonDetailsResult.DetailsSuccess(PersonDetailsFactory.steveCarell()),
              ),
            )

            // Send the second emission
            channel.send(
              Result.success(
                PersonDetailsResult.CreditsSuccess(
                  knownForCredits = PersonCastCreditFactory.knownFor(),
                  knownForDepartment = KnownForDepartment.Acting.value,
                  movies = emptyMap(),
                  tvShows = emptyMap(),
                ),
              ),
            )
          }
        },
        uiStates = listOf(
          createState(
            personDetails = PersonDetailsUiState.Data.Prefetch(
              PersonDetailsFactory.steveCarell().person,
            ),
          ),
          createState(
            personDetails = PersonDetailsUiState.Data.Visible(PersonDetailsFactory.steveCarell()),
          ),
          createState(
            credits = PersonCastCreditFactory.knownFor(),
            personDetails = PersonDetailsUiState.Data.Visible(PersonDetailsFactory.steveCarell()),
          ),
        ),
      )
  }

  @Test
  fun `test onTabSelected updates current tab`() = runTest {
    robot
      .withNavArgs(PersonDetailsFactory.steveCarell().person.map())
      .mockFetchPersonDetailsUseCaseSuccess(
        PersonDetailsResult.CreditsSuccess(
          knownForCredits = PersonCastCreditFactory.knownFor(),
          knownForDepartment = KnownForDepartment.Acting.value,
          movies = GroupedPersonCreditsSample.movies(),
          tvShows = GroupedPersonCreditsSample.tvShows(),
        ),
      )
      .buildViewModel()
      .assertUiState(
        createState(
          personDetails = PersonDetailsUiState.Data.Prefetch(
            PersonDetailsFactory.steveCarell().person,
          ),
          movies = GroupedPersonCreditsSample.movies(),
          tvShows = GroupedPersonCreditsSample.tvShows(),
          filteredCredits = mapOf(
            PersonTab.MOVIES.order to GroupedPersonCreditsSample.movies(),
            PersonTab.TV_SHOWS.order to GroupedPersonCreditsSample.tvShows(),
          ),
          credits = PersonCastCreditFactory.knownFor(),
        ),
      )
      .onTabSelected(PersonTab.TV_SHOWS)
      .assertUiState(
        createState(
          selectedTabIndex = PersonTab.TV_SHOWS.order,
          personDetails = PersonDetailsUiState.Data.Prefetch(
            PersonDetailsFactory.steveCarell().person,
          ),
          credits = PersonCastCreditFactory.knownFor(),
          movies = GroupedPersonCreditsSample.movies(),
          tvShows = GroupedPersonCreditsSample.tvShows(),
          filteredCredits = mapOf(
            PersonTab.MOVIES.order to GroupedPersonCreditsSample.movies(),
            PersonTab.TV_SHOWS.order to GroupedPersonCreditsSample.tvShows(),
          ),
        ),
      )
  }

  @Test
  fun `test onUpdateLayoutStyle switches between List and Grid`() = runTest {
    robot
      .withNavArgs(PersonDetailsFactory.steveCarell().person.map())
      .mockFetchPersonDetailsUseCaseSuccess(
        PersonDetailsResult.CreditsSuccess(
          knownForCredits = PersonCastCreditFactory.knownFor(),
          knownForDepartment = KnownForDepartment.Acting.value,
          movies = GroupedPersonCreditsSample.movies(),
          tvShows = GroupedPersonCreditsSample.tvShows(),
        ),
      )
      .buildViewModel()
      .assertUiState(
        createState(
          personDetails = PersonDetailsUiState.Data.Prefetch(
            PersonDetailsFactory.steveCarell().person,
          ),
          movies = GroupedPersonCreditsSample.movies(),
          tvShows = GroupedPersonCreditsSample.tvShows(),
          filteredCredits = mapOf(
            PersonTab.MOVIES.order to GroupedPersonCreditsSample.movies(),
            PersonTab.TV_SHOWS.order to GroupedPersonCreditsSample.tvShows(),
          ),
          credits = PersonCastCreditFactory.knownFor(),
          layoutStyle = LayoutStyle.LIST,
        ),
      )
      .onTabSelected(PersonTab.TV_SHOWS)
      .onUpdateLayoutStyle()
      .assertUiState(
        createState(
          selectedTabIndex = PersonTab.TV_SHOWS.order,
          personDetails = PersonDetailsUiState.Data.Prefetch(
            PersonDetailsFactory.steveCarell().person,
          ),
          credits = PersonCastCreditFactory.knownFor(),
          movies = GroupedPersonCreditsSample.movies(),
          tvShows = GroupedPersonCreditsSample.tvShows(),
          filteredCredits = mapOf(
            PersonTab.MOVIES.order to GroupedPersonCreditsSample.movies(),
            PersonTab.TV_SHOWS.order to GroupedPersonCreditsSample.tvShows(),
          ),
          layoutStyle = LayoutStyle.GRID,
        ),
      )
      .onUpdateLayoutStyle()
      .assertUiState(
        createState(
          selectedTabIndex = PersonTab.TV_SHOWS.order,
          personDetails = PersonDetailsUiState.Data.Prefetch(
            PersonDetailsFactory.steveCarell().person,
          ),
          credits = PersonCastCreditFactory.knownFor(),
          movies = GroupedPersonCreditsSample.movies(),
          tvShows = GroupedPersonCreditsSample.tvShows(),
          filteredCredits = mapOf(
            PersonTab.MOVIES.order to GroupedPersonCreditsSample.movies(),
            PersonTab.TV_SHOWS.order to GroupedPersonCreditsSample.tvShows(),
          ),
          layoutStyle = LayoutStyle.LIST,
        ),
      )
  }

  @Test
  fun `test onApplyFilter with Directing Department shows only data with Directing`() {
    robot
      .withNavArgs(PersonDetailsFactory.steveCarell().person.map())
      .mockFetchPersonDetailsUseCaseSuccess(
        PersonDetailsResult.CreditsSuccess(
          knownForCredits = PersonCastCreditFactory.knownFor(),
          knownForDepartment = KnownForDepartment.Acting.value,
          movies = GroupedPersonCreditsSample.movies(),
          tvShows = GroupedPersonCreditsSample.tvShows(),
        ),
      )
      .buildViewModel()
      .assertUiState(
        createState(
          personDetails = PersonDetailsUiState.Data.Prefetch(
            PersonDetailsFactory.steveCarell().person,
          ),
          movies = GroupedPersonCreditsSample.movies(),
          tvShows = GroupedPersonCreditsSample.tvShows(),
          filteredCredits = mapOf(
            PersonTab.MOVIES.order to GroupedPersonCreditsSample.movies(),
            PersonTab.TV_SHOWS.order to GroupedPersonCreditsSample.tvShows(),
          ),
          credits = PersonCastCreditFactory.knownFor(),
        ),
      )
      .onTabSelected(PersonTab.TV_SHOWS)
      .onApplyFilter(CreditFilter.Department("Directing", 4))
      .assertUiState(
        createState(
          selectedTabIndex = PersonTab.TV_SHOWS.order,
          personDetails = PersonDetailsUiState.Data.Prefetch(
            PersonDetailsFactory.steveCarell().person,
          ),
          credits = PersonCastCreditFactory.knownFor(),
          movies = GroupedPersonCreditsSample.movies(),
          tvShows = GroupedPersonCreditsSample.tvShows(),
          filteredCredits = mapOf(
            PersonTab.MOVIES.order to GroupedPersonCreditsSample.movies(),
            PersonTab.TV_SHOWS.order to GroupedPersonCreditsSample.tvShows()
              .filterKeys { it == "Directing" },
          ),
          filters = mapOf(
            PersonTab.MOVIES.order to emptyList(),
            PersonTab.TV_SHOWS.order to listOf(CreditFilter.Department("Directing", 4)),
          ),
        ),
      )
  }

  @Test
  fun `test onApplyFilter clears filter when toggling the same filter`() {
    robot
      .withNavArgs(PersonDetailsFactory.steveCarell().person.map())
      .mockFetchPersonDetailsUseCaseSuccess(
        PersonDetailsResult.CreditsSuccess(
          knownForCredits = PersonCastCreditFactory.knownFor(),
          knownForDepartment = KnownForDepartment.Acting.value,
          movies = GroupedPersonCreditsSample.movies(),
          tvShows = GroupedPersonCreditsSample.tvShows(),
        ),
      )
      .buildViewModel()
      .onTabSelected(PersonTab.TV_SHOWS)
      .onApplyFilter(CreditFilter.Department("Directing", 4))
      .assertUiState(
        createState(
          selectedTabIndex = PersonTab.TV_SHOWS.order,
          personDetails = PersonDetailsUiState.Data.Prefetch(
            PersonDetailsFactory.steveCarell().person,
          ),
          credits = PersonCastCreditFactory.knownFor(),
          movies = GroupedPersonCreditsSample.movies(),
          tvShows = GroupedPersonCreditsSample.tvShows(),
          filteredCredits = mapOf(
            PersonTab.MOVIES.order to GroupedPersonCreditsSample.movies(),
            PersonTab.TV_SHOWS.order to GroupedPersonCreditsSample.tvShows()
              .filterKeys { it == "Directing" },
          ),
          filters = mapOf(
            PersonTab.MOVIES.order to emptyList(),
            PersonTab.TV_SHOWS.order to listOf(CreditFilter.Department("Directing", 4)),
          ),
        ),
      )
      .onApplyFilter(CreditFilter.Department("Directing", 4))
      .assertUiState(
        createState(
          selectedTabIndex = PersonTab.TV_SHOWS.order,
          personDetails = PersonDetailsUiState.Data.Prefetch(
            PersonDetailsFactory.steveCarell().person,
          ),
          credits = PersonCastCreditFactory.knownFor(),
          movies = GroupedPersonCreditsSample.movies(),
          tvShows = GroupedPersonCreditsSample.tvShows(),
          filteredCredits = mapOf(
            PersonTab.MOVIES.order to GroupedPersonCreditsSample.movies(),
            PersonTab.TV_SHOWS.order to GroupedPersonCreditsSample.tvShows(),
          ),
          filters = mapOf(
            PersonTab.MOVIES.order to emptyList(),
            PersonTab.TV_SHOWS.order to emptyList(),
          ),
        ),
      )
  }

  @Test
  fun `test onApplyFilter for movies`() {
    robot
      .withNavArgs(PersonDetailsFactory.steveCarell().person.map())
      .mockFetchPersonDetailsUseCaseSuccess(
        PersonDetailsResult.CreditsSuccess(
          knownForCredits = PersonCastCreditFactory.knownFor(),
          knownForDepartment = KnownForDepartment.Acting.value,
          movies = GroupedPersonCreditsSample.movies(),
          tvShows = GroupedPersonCreditsSample.tvShows(),
        ),
      )
      .buildViewModel()
      .onTabSelected(PersonTab.MOVIES)
      .onApplyFilter(CreditFilter.Department("Directing", 4))
      .assertUiState(
        createState(
          selectedTabIndex = PersonTab.MOVIES.order,
          personDetails = PersonDetailsUiState.Data.Prefetch(
            PersonDetailsFactory.steveCarell().person,
          ),
          credits = PersonCastCreditFactory.knownFor(),
          movies = GroupedPersonCreditsSample.movies(),
          tvShows = GroupedPersonCreditsSample.tvShows(),
          filteredCredits = mapOf(
            PersonTab.MOVIES.order to GroupedPersonCreditsSample.movies()
              .filterKeys { it == "Directing" },
            PersonTab.TV_SHOWS.order to GroupedPersonCreditsSample.tvShows(),
          ),
          filters = mapOf(
            PersonTab.MOVIES.order to listOf(CreditFilter.Department("Directing", 4)),
            PersonTab.TV_SHOWS.order to emptyList(),
          ),
        ),
      )
      .onApplyFilter(CreditFilter.Department("Directing", 4))
      .assertUiState(
        createState(
          selectedTabIndex = PersonTab.MOVIES.order,
          personDetails = PersonDetailsUiState.Data.Prefetch(
            PersonDetailsFactory.steveCarell().person,
          ),
          credits = PersonCastCreditFactory.knownFor(),
          movies = GroupedPersonCreditsSample.movies(),
          tvShows = GroupedPersonCreditsSample.tvShows(),
          filteredCredits = mapOf(
            PersonTab.MOVIES.order to GroupedPersonCreditsSample.movies(),
            PersonTab.TV_SHOWS.order to GroupedPersonCreditsSample.tvShows(),
          ),
          filters = mapOf(
            PersonTab.MOVIES.order to emptyList(),
            PersonTab.TV_SHOWS.order to emptyList(),
          ),
        ),
      )
  }

  // Not valid case but we should handle it
  @Test
  fun `test onApplyFilter without correct tab`() {
    robot
      .withNavArgs(PersonDetailsFactory.steveCarell().person.map())
      .mockFetchPersonDetailsUseCaseSuccess(
        PersonDetailsResult.CreditsSuccess(
          knownForCredits = PersonCastCreditFactory.knownFor(),
          knownForDepartment = KnownForDepartment.Acting.value,
          movies = GroupedPersonCreditsSample.movies(),
          tvShows = GroupedPersonCreditsSample.tvShows(),
        ),
      )
      .buildViewModel()
      .onApplyFilter(CreditFilter.Department("Directing", 4))
      .assertUiState(
        createState(
          selectedTabIndex = PersonTab.ABOUT.order,
          personDetails = PersonDetailsUiState.Data.Prefetch(
            PersonDetailsFactory.steveCarell().person,
          ),
          credits = PersonCastCreditFactory.knownFor(),
          movies = GroupedPersonCreditsSample.movies(),
          tvShows = GroupedPersonCreditsSample.tvShows(),
          filteredCredits = mapOf(
            PersonTab.MOVIES.order to GroupedPersonCreditsSample.movies(),
            PersonTab.TV_SHOWS.order to GroupedPersonCreditsSample.tvShows(),
          ),
          filters = mapOf(
            PersonTab.MOVIES.order to emptyList(),
            PersonTab.TV_SHOWS.order to emptyList(),
          ),
        ),
      )
  }

  private fun createState(
    selectedTabIndex: Int = 0,
    isLoading: Boolean = false,
    isError: Boolean = false,
    personDetails: PersonDetailsUiState = PersonDetailsUiState.Loading,
    movies: GroupedPersonCredits = emptyMap(),
    tvShows: GroupedPersonCredits = emptyMap(),
    credits: List<PersonCredit>? = null,
    dropdownMenuItems: List<DetailsMenuOptions> = listOf(DetailsMenuOptions.SHARE),
    tabs: List<PersonTab> = PersonTab.entries,
    filters: Map<Int, List<CreditFilter>> = mapOf(
      PersonTab.MOVIES.order to emptyList(),
      PersonTab.TV_SHOWS.order to emptyList(),
    ),
    filteredCredits: Map<Int, GroupedPersonCredits> = mapOf(
      PersonTab.MOVIES.order to emptyMap(),
      PersonTab.TV_SHOWS.order to emptyMap(),
    ),
    layoutStyle: LayoutStyle = LayoutStyle.LIST,
  ): PersonUiState {
    val forms = mapOf(
      PersonTab.ABOUT.order to PersonForm.About(personDetails),
      PersonTab.MOVIES.order to PersonForm.Movies(movies),
      PersonTab.TV_SHOWS.order to PersonForm.TvShows(tvShows),
    )

    return PersonUiState(
      isLoading = isLoading,
      isError = isError,
      knownForCredits = credits,
      dropdownMenuItems = dropdownMenuItems,
      selectedTabIndex = selectedTabIndex,
      tabs = tabs,
      forms = forms,
      filters = filters,
      filteredCredits = filteredCredits,
      layoutStyle = layoutStyle,
    )
  }
}
