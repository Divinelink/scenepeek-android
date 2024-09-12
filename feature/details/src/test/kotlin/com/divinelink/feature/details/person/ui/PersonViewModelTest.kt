package com.divinelink.feature.details.person.ui

import com.divinelink.core.data.person.details.model.PersonDetailsResult
import com.divinelink.core.model.details.DetailsMenuOptions
import com.divinelink.core.navigation.arguments.PersonNavArguments
import com.divinelink.core.navigation.arguments.map
import com.divinelink.core.testing.MainDispatcherRule
import com.divinelink.core.testing.assertUiState
import com.divinelink.core.testing.expectUiStates
import com.divinelink.core.testing.factories.details.person.PersonDetailsFactory
import com.divinelink.core.testing.factories.model.person.credit.PersonCastCreditFactory
import com.divinelink.core.testing.factories.model.person.credit.PersonCombinedCreditsFactory
import com.divinelink.feature.details.person.ui.credits.PersonCreditsUiState
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
          credits = PersonCombinedCreditsFactory.all(),
        ),
      )
      .buildViewModel()
      .assertUiState(
        createState(
          personDetails = PersonDetailsUiState.Data.Prefetch(
            PersonDetailsFactory.steveCarell().person,
          ),
          credits = PersonCreditsUiState.Visible(
            knownFor = PersonCastCreditFactory.knownFor(),
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
                  credits = PersonCombinedCreditsFactory.all(),
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
            credits = PersonCreditsUiState.Visible(
              knownFor = PersonCastCreditFactory.knownFor(),
            ),
            personDetails = PersonDetailsUiState.Data.Visible(PersonDetailsFactory.steveCarell()),
          ),
        ),
      )
  }

  private fun createState(
    isLoading: Boolean = false,
    isError: Boolean = false,
    personDetails: PersonDetailsUiState = PersonDetailsUiState.Loading,
    credits: PersonCreditsUiState = PersonCreditsUiState.Hidden,
    dropdownMenuItems: List<DetailsMenuOptions> = listOf(DetailsMenuOptions.SHARE),
  ) = PersonUiState(
    isLoading = isLoading,
    isError = isError,
    personDetails = personDetails,
    credits = credits,
    dropdownMenuItems = dropdownMenuItems,
  )
}
