package com.divinelink.feature.details.person.ui

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsNotDisplayed
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.lifecycle.SavedStateHandle
import com.divinelink.core.data.details.person.model.PersonDetailsResult
import com.divinelink.core.testing.ComposeTest
import com.divinelink.core.testing.factories.details.person.PersonDetailsFactory
import com.divinelink.core.testing.navigator.FakeDestinationsNavigator
import com.divinelink.core.testing.setContentWithTheme
import com.divinelink.core.testing.usecase.TestFetchPersonDetailsUseCase
import com.divinelink.core.ui.TestTags
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.test.runTest
import kotlin.test.BeforeTest
import kotlin.test.Test

class PersonScreenTest : ComposeTest() {

  private lateinit var navigator: FakeDestinationsNavigator
  private lateinit var fetchPersonDetailsUseCase: TestFetchPersonDetailsUseCase
  private lateinit var savedStateHandle: SavedStateHandle

  @BeforeTest
  fun setUp() {
    navigator = FakeDestinationsNavigator()
    fetchPersonDetailsUseCase = TestFetchPersonDetailsUseCase()
    savedStateHandle = SavedStateHandle(
      mapOf(
        "id" to PersonDetailsFactory.steveCarell().person.id,
      ),
    )
  }

  @Test
  fun `test loading content is visible when loading`() {
    val channel = Channel<Result<PersonDetailsResult>>()

    fetchPersonDetailsUseCase.mockSuccess(
      response = channel,
    )

    val viewModel = PersonViewModel(
      fetchPersonDetailsUseCase = fetchPersonDetailsUseCase.mock,
      savedStateHandle = savedStateHandle,
    )

    setContentWithTheme {
      PersonScreen(
        navigator = navigator,
        viewModel = viewModel,
      )
    }
    with(composeTestRule) {
      onNodeWithTag(TestTags.LOADING_CONTENT).assertIsDisplayed()
    }
  }

  @Test
  fun `test topAppBar is visible with success`() {
    fetchPersonDetailsUseCase.mockSuccess(
      PersonDetailsResult.DetailsSuccess(PersonDetailsFactory.steveCarell()),
    )
    val viewModel = PersonViewModel(
      fetchPersonDetailsUseCase = fetchPersonDetailsUseCase.mock,
      savedStateHandle = savedStateHandle,
    )

    setContentWithTheme {
      PersonScreen(
        navigator = navigator,
        viewModel = viewModel,
      )
    }
    with(composeTestRule) {
      onNodeWithTag(TestTags.Components.TopAppBar.TOP_APP_BAR).assertIsDisplayed()
      onNodeWithTag(TestTags.Components.TopAppBar.TOP_APP_BAR_TITLE)
        .assertIsDisplayed()
        .assertTextEquals("Steve Carell")
    }
  }

  @Test
  fun `test topAppBar is not visible when failure`() {
    val viewModel = PersonViewModel(
      fetchPersonDetailsUseCase = fetchPersonDetailsUseCase.mock,
      savedStateHandle = savedStateHandle,
    )

    setContentWithTheme {
      PersonScreen(
        navigator = navigator,
        viewModel = viewModel,
      )
    }
    with(composeTestRule) {
      onNodeWithTag(TestTags.Components.TopAppBar.TOP_APP_BAR).assertIsNotDisplayed()
      onNodeWithTag(TestTags.Components.TopAppBar.TOP_APP_BAR_TITLE).assertIsNotDisplayed()
    }
  }

  @Test
  fun `test DropDownMenu is visible when success`() {
    fetchPersonDetailsUseCase.mockSuccess(
      PersonDetailsResult.DetailsSuccess(PersonDetailsFactory.steveCarell()),
    )
    val viewModel = PersonViewModel(
      fetchPersonDetailsUseCase = fetchPersonDetailsUseCase.mock,
      savedStateHandle = savedStateHandle,
    )

    setContentWithTheme {
      PersonScreen(
        navigator = navigator,
        viewModel = viewModel,
      )
    }
    with(composeTestRule) {
      onNodeWithTag(TestTags.Menu.MENU_BUTTON_VERTICAL).assertIsDisplayed().performClick()
      onNodeWithTag(TestTags.Menu.DROPDOWN_MENU).assertIsDisplayed()
      onNodeWithTag(TestTags.Menu.MENU_ITEM.format("Share")).assertIsDisplayed().performClick()
      onNodeWithTag(TestTags.Menu.DROPDOWN_MENU).assertIsNotDisplayed()
    }
  }

  @Test
  fun `test person content is visible when success personal details`() {
    fetchPersonDetailsUseCase.mockSuccess(
      PersonDetailsResult.DetailsSuccess(PersonDetailsFactory.steveCarell()),
    )
    val viewModel = PersonViewModel(
      fetchPersonDetailsUseCase = fetchPersonDetailsUseCase.mock,
      savedStateHandle = savedStateHandle,
    )

    setContentWithTheme {
      PersonScreen(
        navigator = navigator,
        viewModel = viewModel,
      )
    }
    with(composeTestRule) {
      onNodeWithTag(TestTags.Person.CONTENT_LIST).assertIsDisplayed()
      onNodeWithTag(TestTags.Person.PERSONAL_DETAILS).assertIsDisplayed()
    }
  }

  @Test
  fun `test state is updated on success person details`() = runTest {
    val channel = Channel<Result<PersonDetailsResult>>()

    fetchPersonDetailsUseCase.mockSuccess(
      response = channel,
    )

    val viewModel = PersonViewModel(
      fetchPersonDetailsUseCase = fetchPersonDetailsUseCase.mock,
      savedStateHandle = savedStateHandle,
    )

    setContentWithTheme {
      PersonScreen(
        navigator = navigator,
        viewModel = viewModel,
      )
    }
    with(composeTestRule) {
      onNodeWithTag(TestTags.LOADING_CONTENT).assertIsDisplayed()

      channel.send(
        Result.success(PersonDetailsResult.DetailsSuccess(PersonDetailsFactory.steveCarell())),
      )

      onNodeWithTag(TestTags.LOADING_CONTENT).assertIsNotDisplayed()
      onNodeWithTag(TestTags.Person.CONTENT_LIST).assertIsDisplayed()
      onNodeWithTag(TestTags.Person.PERSONAL_DETAILS).assertIsDisplayed()
    }
  }
}
