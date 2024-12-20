package com.divinelink.feature.details.person.ui

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsNotDisplayed
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.onAllNodesWithTag
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollToIndex
import androidx.compose.ui.test.performScrollToNode
import androidx.lifecycle.SavedStateHandle
import com.divinelink.core.data.person.details.model.PersonDetailsResult
import com.divinelink.core.model.media.MediaType
import com.divinelink.core.navigation.arguments.DetailsNavArguments
import com.divinelink.core.navigation.arguments.PersonNavArguments
import com.divinelink.core.testing.ComposeTest
import com.divinelink.core.fixtures.details.person.PersonDetailsFactory
import com.divinelink.core.fixtures.model.person.credit.PersonCastCreditFactory
import com.divinelink.core.fixtures.model.person.credit.PersonCastCreditFactory.bruceAlmighty
import com.divinelink.core.fixtures.model.person.credit.PersonCastCreditFactory.despicableMe
import com.divinelink.core.fixtures.model.person.credit.PersonCastCreditFactory.littleMissSunshine
import com.divinelink.core.fixtures.model.person.credit.PersonCastCreditFactory.theOffice
import com.divinelink.core.fixtures.model.person.credit.PersonCombinedCreditsFactory
import com.divinelink.core.testing.navigator.FakeDestinationsNavigator
import com.divinelink.core.testing.setContentWithTheme
import com.divinelink.core.testing.usecase.TestFetchChangesUseCase
import com.divinelink.core.testing.usecase.TestFetchPersonDetailsUseCase
import com.divinelink.core.ui.TestTags
import com.divinelink.core.ui.components.MOVIE_CARD_ITEM_TAG
import com.divinelink.feature.details.screens.destinations.DetailsScreenDestination
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.test.runTest
import kotlin.test.BeforeTest
import kotlin.test.Test

class PersonScreenTest : ComposeTest() {

  private lateinit var navigator: FakeDestinationsNavigator
  private lateinit var fetchPersonDetailsUseCase: TestFetchPersonDetailsUseCase
  private lateinit var fetchChangesUseCase: TestFetchChangesUseCase
  private lateinit var savedStateHandle: SavedStateHandle

  private lateinit var navArgs: PersonNavArguments

  @BeforeTest
  fun setUp() {
    navArgs = PersonNavArguments(
      id = PersonDetailsFactory.steveCarell().person.id,
      knownForDepartment = null,
      name = PersonDetailsFactory.steveCarell().person.name,
      profilePath = null,
      gender = null,
    )
    navigator = FakeDestinationsNavigator()
    fetchPersonDetailsUseCase = TestFetchPersonDetailsUseCase()
    fetchChangesUseCase = TestFetchChangesUseCase()
    savedStateHandle = SavedStateHandle(
      mapOf(
        "id" to navArgs.id,
        "knownForDepartment" to navArgs.knownForDepartment,
        "name" to navArgs.name,
        "profilePath" to navArgs.profilePath,
        "gender" to navArgs.gender,
      ),
    )
  }

  @Test
  fun `test loading content is visible when navArgs name is null`() {
    val channel = Channel<Result<PersonDetailsResult>>()

    fetchPersonDetailsUseCase.mockSuccess(
      response = channel,
    )

    val viewModel = PersonViewModel(
      fetchPersonDetailsUseCase = fetchPersonDetailsUseCase.mock,
      fetchChangesUseCase = fetchChangesUseCase.mock,
      savedStateHandle = SavedStateHandle(
        mapOf(
          "id" to navArgs.id,
          "knownForDepartment" to navArgs.knownForDepartment,
          "name" to null,
          "profilePath" to navArgs.profilePath,
          "gender" to navArgs.gender,
        ),
      ),
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
      fetchChangesUseCase = fetchChangesUseCase.mock,
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
  fun `test topAppBar is not visible when personDetails is not success`() {
    val viewModel = PersonViewModel(
      fetchPersonDetailsUseCase = fetchPersonDetailsUseCase.mock,
      fetchChangesUseCase = fetchChangesUseCase.mock,
      savedStateHandle = SavedStateHandle(
        mapOf(
          "id" to navArgs.id,
          "knownForDepartment" to navArgs.knownForDepartment,
          "name" to null,
          "profilePath" to navArgs.profilePath,
          "gender" to navArgs.gender,
        ),
      ),
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
      fetchChangesUseCase = fetchChangesUseCase.mock,
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
      fetchChangesUseCase = fetchChangesUseCase.mock,
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
      fetchChangesUseCase = fetchChangesUseCase.mock,
      savedStateHandle = savedStateHandle,
    )

    setContentWithTheme {
      PersonScreen(
        navigator = navigator,
        viewModel = viewModel,
      )
    }
    with(composeTestRule) {
      onNodeWithTag(TestTags.LOADING_CONTENT).assertIsNotDisplayed()

      channel.send(
        Result.success(PersonDetailsResult.DetailsSuccess(PersonDetailsFactory.steveCarell())),
      )

      onNodeWithTag(TestTags.LOADING_CONTENT).assertIsNotDisplayed()
      onNodeWithTag(TestTags.Person.CONTENT_LIST).assertIsDisplayed()
      onNodeWithTag(TestTags.Person.PERSONAL_DETAILS).assertIsDisplayed()
    }
  }

  @Test
  fun `test knownFor section is visible when credits are available`() = runTest {
    val channel = Channel<Result<PersonDetailsResult>>()

    fetchPersonDetailsUseCase.mockSuccess(
      response = channel,
    )

    val viewModel = PersonViewModel(
      fetchPersonDetailsUseCase = fetchPersonDetailsUseCase.mock,
      fetchChangesUseCase = fetchChangesUseCase.mock,
      savedStateHandle = savedStateHandle,
    )

    setContentWithTheme {
      PersonScreen(
        navigator = navigator,
        viewModel = viewModel,
      )
    }
    with(composeTestRule) {
      onNodeWithTag(TestTags.LOADING_CONTENT).assertIsNotDisplayed()

      channel.send(
        Result.success(PersonDetailsResult.DetailsSuccess(PersonDetailsFactory.steveCarell())),
      )
      channel.send(
        Result.success(
          PersonDetailsResult.CreditsSuccess(
            knownForCredits = PersonCastCreditFactory.knownFor(),
            credits = PersonCombinedCreditsFactory.all(),
          ),
        ),
      )

      onNodeWithTag(TestTags.Person.CONTENT_LIST).performScrollToIndex(2)

      onNodeWithTag(TestTags.Person.KNOWN_FOR_SECTION).assertIsDisplayed()
      onNodeWithTag(TestTags.Person.KNOWN_FOR_SECTION_LIST).assertIsDisplayed()
    }
  }

  @Test
  fun `test knownFor section scroll position does not change when new items are added`() = runTest {
    val channel = Channel<Result<PersonDetailsResult>>()

    fetchPersonDetailsUseCase.mockSuccess(
      response = channel,
    )

    val viewModel = PersonViewModel(
      fetchPersonDetailsUseCase = fetchPersonDetailsUseCase.mock,
      fetchChangesUseCase = fetchChangesUseCase.mock,
      savedStateHandle = savedStateHandle,
    )

    setContentWithTheme {
      PersonScreen(
        navigator = navigator,
        viewModel = viewModel,
      )
    }
    with(composeTestRule) {
      onNodeWithTag(TestTags.LOADING_CONTENT).assertIsNotDisplayed()

      channel.send(
        Result.success(PersonDetailsResult.DetailsSuccess(PersonDetailsFactory.steveCarell())),
      )
      channel.send(
        Result.success(
          PersonDetailsResult.CreditsSuccess(
            knownForCredits = PersonCastCreditFactory.knownFor(),
            credits = PersonCombinedCreditsFactory.all(),
          ),
        ),
      )

      onNodeWithTag(TestTags.Person.CONTENT_LIST).performScrollToNode(
        matcher = hasTestTag(TestTags.Person.KNOWN_FOR_SECTION),
      )

      onNodeWithTag(TestTags.Person.KNOWN_FOR_SECTION).assertIsDisplayed()
      onNodeWithTag(TestTags.Person.KNOWN_FOR_SECTION_LIST).assertIsDisplayed()

      onNodeWithText("8.6").assertIsDisplayed()
      onNodeWithText("9.6").assertIsNotDisplayed()
      onNodeWithText("9.5").assertIsNotDisplayed()
      onNodeWithText("9.4").assertIsNotDisplayed()

      channel.send(
        Result.success(
          PersonDetailsResult.CreditsSuccess(
            knownForCredits = listOf(
              theOffice().copy(id = 1, voteAverage = 9.3),
              littleMissSunshine().copy(id = 2, voteAverage = 9.4),
              despicableMe().copy(id = 3, voteAverage = 9.5),
              bruceAlmighty().copy(id = 4, voteAverage = 9.6),
              theOffice().copy(id = 5, voteAverage = 5.2),
              littleMissSunshine().copy(id = 6, voteAverage = 4.3),
              despicableMe().copy(id = 7, voteAverage = 3.2),
              bruceAlmighty().copy(id = 8, voteAverage = 2.1),
              theOffice(),
              littleMissSunshine(),
              despicableMe(),
              bruceAlmighty(),
            ),
            credits = PersonCombinedCreditsFactory.all(),
          ),
        ),
      )

      // We assert that the newly added items - that have higher ratings - are visible, while the
      // older ones that have lower ratings are moved on the list. However our scroll position has
      // not changed
      onNodeWithText("8.6").assertIsNotDisplayed()
      onNodeWithText("9.5").assertIsDisplayed()
      onNodeWithText("9.4").assertIsDisplayed()
    }
  }

  @Test
  fun `test loading content is visible when ui state is success but person details are loading`() =
    runTest {
      val channel = Channel<Result<PersonDetailsResult>>()

      fetchPersonDetailsUseCase.mockSuccess(
        response = channel,
      )

      val viewModel = PersonViewModel(
        fetchPersonDetailsUseCase = fetchPersonDetailsUseCase.mock,
        fetchChangesUseCase = fetchChangesUseCase.mock,
        savedStateHandle = SavedStateHandle(
          mapOf(
            "id" to navArgs.id,
            "knownForDepartment" to null,
            "name" to null,
            "profilePath" to null,
            "gender" to null,
          ),
        ),
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
          Result.success(
            PersonDetailsResult.CreditsSuccess(
              knownForCredits = PersonCastCreditFactory.knownFor(),
              credits = PersonCombinedCreditsFactory.all(),
            ),
          ),
        )

        onNodeWithTag(TestTags.LOADING_CONTENT).assertIsDisplayed()
      }
    }

  @Test
  fun `test onMediaClick navigates to detail`() = runTest {
    val channel = Channel<Result<PersonDetailsResult>>()

    fetchPersonDetailsUseCase.mockSuccess(
      response = channel,
    )

    val viewModel = PersonViewModel(
      fetchPersonDetailsUseCase = fetchPersonDetailsUseCase.mock,
      fetchChangesUseCase = fetchChangesUseCase.mock,
      savedStateHandle = savedStateHandle,
    )

    setContentWithTheme {
      PersonScreen(
        navigator = navigator,
        viewModel = viewModel,
      )
    }
    with(composeTestRule) {
      onNodeWithTag(TestTags.LOADING_CONTENT).assertIsNotDisplayed()

      channel.send(
        Result.success(PersonDetailsResult.DetailsSuccess(PersonDetailsFactory.steveCarell())),
      )
      channel.send(
        Result.success(
          PersonDetailsResult.CreditsSuccess(
            knownForCredits = PersonCastCreditFactory.knownFor(),
            credits = PersonCombinedCreditsFactory.all(),
          ),
        ),
      )

      onNodeWithTag(TestTags.Person.CONTENT_LIST).performScrollToNode(
        matcher = hasTestTag(TestTags.Person.KNOWN_FOR_SECTION),
      )

      onNodeWithTag(TestTags.Person.KNOWN_FOR_SECTION).assertIsDisplayed()

      composeTestRule
        .onAllNodesWithTag(MOVIE_CARD_ITEM_TAG)[0]
        .performClick()

      composeTestRule.runOnIdle {
        navigator.verifyNavigatedToDirection(
          expectedDirection = DetailsScreenDestination(
            DetailsNavArguments(
              id = 2316,
              mediaType = MediaType.TV.value,
              isFavorite = null,
            ),
          ),
        )
      }
    }
  }

  @Test
  fun `test knownForSection is hidden iff credits are not Visible`() = runTest {
    val channel = Channel<Result<PersonDetailsResult>>()

    fetchPersonDetailsUseCase.mockSuccess(
      response = channel,
    )

    val viewModel = PersonViewModel(
      fetchPersonDetailsUseCase = fetchPersonDetailsUseCase.mock,
      fetchChangesUseCase = fetchChangesUseCase.mock,
      savedStateHandle = savedStateHandle,
    )

    setContentWithTheme {
      PersonScreen(
        navigator = navigator,
        viewModel = viewModel,
      )
    }
    with(composeTestRule) {
      onNodeWithTag(TestTags.LOADING_CONTENT).assertIsNotDisplayed()

      channel.send(
        Result.success(PersonDetailsResult.DetailsSuccess(PersonDetailsFactory.steveCarell())),
      )

      onNodeWithTag(TestTags.Person.CONTENT_LIST).performScrollToIndex(1)

      onNodeWithTag(TestTags.Person.KNOWN_FOR_SECTION).assertDoesNotExist()

      onNodeWithTag(TestTags.Person.KNOWN_FOR_SECTION).assertIsNotDisplayed()
      onNodeWithTag(TestTags.Person.KNOWN_FOR_SECTION_LIST).assertIsNotDisplayed()
    }
  }

  @Test
  fun `test knownForSection is hidden iff credits are empty`() = runTest {
    val channel = Channel<Result<PersonDetailsResult>>()

    fetchPersonDetailsUseCase.mockSuccess(
      response = channel,
    )

    val viewModel = PersonViewModel(
      fetchPersonDetailsUseCase = fetchPersonDetailsUseCase.mock,
      fetchChangesUseCase = fetchChangesUseCase.mock,
      savedStateHandle = savedStateHandle,
    )

    setContentWithTheme {
      PersonScreen(
        navigator = navigator,
        viewModel = viewModel,
      )
    }
    with(composeTestRule) {
      onNodeWithTag(TestTags.LOADING_CONTENT).assertIsNotDisplayed()

      channel.send(
        Result.success(PersonDetailsResult.DetailsSuccess(PersonDetailsFactory.steveCarell())),
      )

      channel.send(
        Result.success(
          PersonDetailsResult.CreditsSuccess(
            knownForCredits = emptyList(),
            credits = PersonCombinedCreditsFactory.all(),
          ),
        ),
      )

      onNodeWithTag(TestTags.Person.CONTENT_LIST).performScrollToIndex(1)

      onNodeWithTag(TestTags.Person.KNOWN_FOR_SECTION).assertDoesNotExist()

      onNodeWithTag(TestTags.Person.KNOWN_FOR_SECTION).assertIsNotDisplayed()
      onNodeWithTag(TestTags.Person.KNOWN_FOR_SECTION_LIST).assertIsNotDisplayed()
    }
  }
}
