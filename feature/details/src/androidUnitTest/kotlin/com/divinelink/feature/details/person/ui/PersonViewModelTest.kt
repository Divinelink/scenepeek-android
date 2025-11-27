package com.divinelink.feature.details.person.ui

import com.divinelink.core.data.person.details.model.PersonDetailsResult
import com.divinelink.core.fixtures.details.person.PersonDetailsFactory
import com.divinelink.core.fixtures.model.person.credit.GroupedPersonCreditsSample
import com.divinelink.core.fixtures.model.person.credit.PersonCastCreditFactory
import com.divinelink.core.model.details.DetailsMenuOptions
import com.divinelink.core.model.details.person.GroupedPersonCredits
import com.divinelink.core.model.person.Gender
import com.divinelink.core.model.person.KnownForDepartment
import com.divinelink.core.model.person.credits.PersonCredit
import com.divinelink.core.model.tab.PersonTab
import com.divinelink.core.model.tab.Tab
import com.divinelink.core.navigation.route.Navigation.PersonRoute
import com.divinelink.core.navigation.route.toPersonRoute
import com.divinelink.core.testing.MainDispatcherRule
import com.divinelink.core.testing.assertUiState
import com.divinelink.core.testing.expectUiStates
import com.divinelink.feature.details.person.ui.filter.CreditFilter
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
        PersonRoute(
          id = PersonDetailsFactory.steveCarell().person.id,
          knownForDepartment = null,
          name = null,
          profilePath = null,
          gender = Gender.NOT_SET.value,
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
      .withNavArgs(PersonDetailsFactory.steveCarell().person.toPersonRoute())
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
      .withNavArgs(PersonDetailsFactory.steveCarell().person.toPersonRoute())
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
      .withNavArgs(PersonDetailsFactory.steveCarell().person.toPersonRoute())
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
      .withNavArgs(PersonDetailsFactory.steveCarell().person.toPersonRoute())
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
            PersonTab.Movies.order to GroupedPersonCreditsSample.movies(),
            PersonTab.TVShows.order to GroupedPersonCreditsSample.tvShows(),
          ),
        ),
      )
  }

  @Test
  fun `test updateCredits when uiState is already success`() = runTest {
    val channel = Channel<Result<PersonDetailsResult>>()

    robot
      .withNavArgs(PersonDetailsFactory.steveCarell().person.toPersonRoute())
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
      .withNavArgs(PersonDetailsFactory.steveCarell().person.toPersonRoute())
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
            PersonTab.Movies.order to GroupedPersonCreditsSample.movies(),
            PersonTab.TVShows.order to GroupedPersonCreditsSample.tvShows(),
          ),
          credits = PersonCastCreditFactory.knownFor(),
        ),
      )
      .onTabSelected(PersonTab.TVShows)
      .assertUiState(
        createState(
          selectedTabIndex = PersonTab.TVShows.order,
          personDetails = PersonDetailsUiState.Data.Prefetch(
            PersonDetailsFactory.steveCarell().person,
          ),
          credits = PersonCastCreditFactory.knownFor(),
          movies = GroupedPersonCreditsSample.movies(),
          tvShows = GroupedPersonCreditsSample.tvShows(),
          filteredCredits = mapOf(
            PersonTab.Movies.order to GroupedPersonCreditsSample.movies(),
            PersonTab.TVShows.order to GroupedPersonCreditsSample.tvShows(),
          ),
        ),
      )
  }

  @Test
  fun `test onApplyFilter with Directing Department shows only data with Directing`() {
    robot
      .withNavArgs(PersonDetailsFactory.steveCarell().person.toPersonRoute())
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
            PersonTab.Movies.order to GroupedPersonCreditsSample.movies(),
            PersonTab.TVShows.order to GroupedPersonCreditsSample.tvShows(),
          ),
          credits = PersonCastCreditFactory.knownFor(),
        ),
      )
      .onTabSelected(PersonTab.TVShows)
      .onApplyFilter(CreditFilter.Department("Directing", 4))
      .assertUiState(
        createState(
          selectedTabIndex = PersonTab.TVShows.order,
          personDetails = PersonDetailsUiState.Data.Prefetch(
            PersonDetailsFactory.steveCarell().person,
          ),
          credits = PersonCastCreditFactory.knownFor(),
          movies = GroupedPersonCreditsSample.movies(),
          tvShows = GroupedPersonCreditsSample.tvShows(),
          filteredCredits = mapOf(
            PersonTab.Movies.order to GroupedPersonCreditsSample.movies(),
            PersonTab.TVShows.order to GroupedPersonCreditsSample.tvShows()
              .filterKeys { it == "Directing" },
          ),
          filters = mapOf(
            PersonTab.Movies.order to emptyList(),
            PersonTab.TVShows.order to listOf(CreditFilter.Department("Directing", 4)),
          ),
        ),
      )
  }

  @Test
  fun `test onApplyFilter clears filter when toggling the same filter`() {
    robot
      .withNavArgs(PersonDetailsFactory.steveCarell().person.toPersonRoute())
      .mockFetchPersonDetailsUseCaseSuccess(
        PersonDetailsResult.CreditsSuccess(
          knownForCredits = PersonCastCreditFactory.knownFor(),
          knownForDepartment = KnownForDepartment.Acting.value,
          movies = GroupedPersonCreditsSample.movies(),
          tvShows = GroupedPersonCreditsSample.tvShows(),
        ),
      )
      .buildViewModel()
      .onTabSelected(PersonTab.TVShows)
      .onApplyFilter(CreditFilter.Department("Directing", 4))
      .assertUiState(
        createState(
          selectedTabIndex = PersonTab.TVShows.order,
          personDetails = PersonDetailsUiState.Data.Prefetch(
            PersonDetailsFactory.steveCarell().person,
          ),
          credits = PersonCastCreditFactory.knownFor(),
          movies = GroupedPersonCreditsSample.movies(),
          tvShows = GroupedPersonCreditsSample.tvShows(),
          filteredCredits = mapOf(
            PersonTab.Movies.order to GroupedPersonCreditsSample.movies(),
            PersonTab.TVShows.order to GroupedPersonCreditsSample.tvShows()
              .filterKeys { it == "Directing" },
          ),
          filters = mapOf(
            PersonTab.Movies.order to emptyList(),
            PersonTab.TVShows.order to listOf(CreditFilter.Department("Directing", 4)),
          ),
        ),
      )
      .onApplyFilter(CreditFilter.Department("Directing", 4))
      .assertUiState(
        createState(
          selectedTabIndex = PersonTab.TVShows.order,
          personDetails = PersonDetailsUiState.Data.Prefetch(
            PersonDetailsFactory.steveCarell().person,
          ),
          credits = PersonCastCreditFactory.knownFor(),
          movies = GroupedPersonCreditsSample.movies(),
          tvShows = GroupedPersonCreditsSample.tvShows(),
          filteredCredits = mapOf(
            PersonTab.Movies.order to GroupedPersonCreditsSample.movies(),
            PersonTab.TVShows.order to GroupedPersonCreditsSample.tvShows(),
          ),
          filters = mapOf(
            PersonTab.Movies.order to emptyList(),
            PersonTab.TVShows.order to emptyList(),
          ),
        ),
      )
  }

  @Test
  fun `test onApplyFilter for movies`() {
    robot
      .withNavArgs(PersonDetailsFactory.steveCarell().person.toPersonRoute())
      .mockFetchPersonDetailsUseCaseSuccess(
        PersonDetailsResult.CreditsSuccess(
          knownForCredits = PersonCastCreditFactory.knownFor(),
          knownForDepartment = KnownForDepartment.Acting.value,
          movies = GroupedPersonCreditsSample.movies(),
          tvShows = GroupedPersonCreditsSample.tvShows(),
        ),
      )
      .buildViewModel()
      .onTabSelected(PersonTab.Movies)
      .onApplyFilter(CreditFilter.Department("Directing", 4))
      .assertUiState(
        createState(
          selectedTabIndex = PersonTab.Movies.order,
          personDetails = PersonDetailsUiState.Data.Prefetch(
            PersonDetailsFactory.steveCarell().person,
          ),
          credits = PersonCastCreditFactory.knownFor(),
          movies = GroupedPersonCreditsSample.movies(),
          tvShows = GroupedPersonCreditsSample.tvShows(),
          filteredCredits = mapOf(
            PersonTab.Movies.order to GroupedPersonCreditsSample.movies()
              .filterKeys { it == "Directing" },
            PersonTab.TVShows.order to GroupedPersonCreditsSample.tvShows(),
          ),
          filters = mapOf(
            PersonTab.Movies.order to listOf(CreditFilter.Department("Directing", 4)),
            PersonTab.TVShows.order to emptyList(),
          ),
        ),
      )
      .onApplyFilter(CreditFilter.Department("Directing", 4))
      .assertUiState(
        createState(
          selectedTabIndex = PersonTab.Movies.order,
          personDetails = PersonDetailsUiState.Data.Prefetch(
            PersonDetailsFactory.steveCarell().person,
          ),
          credits = PersonCastCreditFactory.knownFor(),
          movies = GroupedPersonCreditsSample.movies(),
          tvShows = GroupedPersonCreditsSample.tvShows(),
          filteredCredits = mapOf(
            PersonTab.Movies.order to GroupedPersonCreditsSample.movies(),
            PersonTab.TVShows.order to GroupedPersonCreditsSample.tvShows(),
          ),
          filters = mapOf(
            PersonTab.Movies.order to emptyList(),
            PersonTab.TVShows.order to emptyList(),
          ),
        ),
      )
  }

  // Not valid case but we should handle it
  @Test
  fun `test onApplyFilter without correct tab`() {
    robot
      .withNavArgs(PersonDetailsFactory.steveCarell().person.toPersonRoute())
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
          selectedTabIndex = PersonTab.About.order,
          personDetails = PersonDetailsUiState.Data.Prefetch(
            PersonDetailsFactory.steveCarell().person,
          ),
          credits = PersonCastCreditFactory.knownFor(),
          movies = GroupedPersonCreditsSample.movies(),
          tvShows = GroupedPersonCreditsSample.tvShows(),
          filteredCredits = mapOf(
            PersonTab.Movies.order to GroupedPersonCreditsSample.movies(),
            PersonTab.TVShows.order to GroupedPersonCreditsSample.tvShows(),
          ),
          filters = mapOf(
            PersonTab.Movies.order to emptyList(),
            PersonTab.TVShows.order to emptyList(),
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
    tabs: List<Tab> = PersonTab.entries,
    filters: Map<Int, List<CreditFilter>> = mapOf(
      PersonTab.Movies.order to emptyList(),
      PersonTab.TVShows.order to emptyList(),
    ),
    filteredCredits: Map<Int, GroupedPersonCredits> = mapOf(
      PersonTab.Movies.order to emptyMap(),
      PersonTab.TVShows.order to emptyMap(),
    ),
  ): PersonUiState {
    val forms = mapOf(
      PersonTab.About.order to PersonForm.About(personDetails),
      PersonTab.Movies.order to PersonForm.Movies(movies),
      PersonTab.TVShows.order to PersonForm.TvShows(tvShows),
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
    )
  }
}
