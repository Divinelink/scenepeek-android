package com.divinelink.feature.details.person.ui

import com.divinelink.core.data.details.person.model.PersonDetailsResult
import com.divinelink.core.navigation.arguments.PersonNavArguments
import com.divinelink.core.testing.MainDispatcherRule
import com.divinelink.core.testing.assertUiState
import com.divinelink.core.testing.factories.details.person.PersonDetailsFactory
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
        PersonNavArguments(id = PersonDetailsFactory.steveCarell().person.id),
      )
      .buildViewModel()
      .assertUiState(PersonUiState.Error)
  }

  @Test
  fun `test initialise viewModel with success person details`() = runTest {
    robot
      .withNavArgs(
        PersonNavArguments(id = PersonDetailsFactory.steveCarell().person.id),
      )
      .mockFetchPersonDetailsUseCaseSuccess(
        PersonDetailsResult.DetailsSuccess(PersonDetailsFactory.steveCarell()),
      )
      .buildViewModel()
      .assertUiState(PersonUiState.Success(PersonDetailsFactory.steveCarell()))
  }

  @Test
  fun `test initialise viewModel with error on details`() = runTest {
    robot
      .withNavArgs(
        PersonNavArguments(id = PersonDetailsFactory.steveCarell().person.id),
      )
      .mockFetchPersonDetailsUseCaseSuccess(PersonDetailsResult.DetailsFailure)
      .buildViewModel()
      .assertUiState(PersonUiState.Error)
  }
}
