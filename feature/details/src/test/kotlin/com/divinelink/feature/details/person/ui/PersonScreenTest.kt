package com.divinelink.feature.details.person.ui

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsNotDisplayed
import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.onAllNodesWithTag
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollToIndex
import androidx.compose.ui.test.performScrollToNode
import androidx.lifecycle.SavedStateHandle
import com.divinelink.core.data.person.details.model.PersonDetailsResult
import com.divinelink.core.fixtures.details.person.PersonDetailsFactory
import com.divinelink.core.fixtures.model.person.credit.GroupedPersonCreditsSample
import com.divinelink.core.fixtures.model.person.credit.PersonCastCreditFactory
import com.divinelink.core.fixtures.model.person.credit.PersonCastCreditFactory.bruceAlmighty
import com.divinelink.core.fixtures.model.person.credit.PersonCastCreditFactory.despicableMe
import com.divinelink.core.fixtures.model.person.credit.PersonCastCreditFactory.littleMissSunshine
import com.divinelink.core.fixtures.model.person.credit.PersonCastCreditFactory.theOffice
import com.divinelink.core.model.media.MediaType
import com.divinelink.core.model.person.KnownForDepartment
import com.divinelink.core.navigation.arguments.DetailsNavArguments
import com.divinelink.core.navigation.arguments.PersonNavArguments
import com.divinelink.core.testing.ComposeTest
import com.divinelink.core.testing.getString
import com.divinelink.core.testing.navigator.FakeDestinationsNavigator
import com.divinelink.core.testing.setContentWithTheme
import com.divinelink.core.testing.usecase.TestFetchChangesUseCase
import com.divinelink.core.testing.usecase.TestFetchPersonDetailsUseCase
import com.divinelink.core.ui.TestTags
import com.divinelink.core.ui.components.MOVIE_CARD_ITEM_TAG
import com.divinelink.feature.details.person.ui.tab.PersonTab
import com.divinelink.feature.details.screens.destinations.DetailsScreenDestination
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.test.runTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import com.divinelink.core.ui.R as uiR

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
  fun `test topAppBar is not visible when scrolled on top`() {
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
      onNodeWithTag(TestTags.Components.TopAppBar.TOP_APP_BAR_TITLE).assertIsNotDisplayed()
      onNodeWithTag(TestTags.Person.COLLAPSIBLE_CONTENT).assertIsDisplayed()
      onNodeWithTag(TestTags.Person.PERSON_NAME).assertIsDisplayed()
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
            knownForDepartment = KnownForDepartment.Acting.value,
            movies = GroupedPersonCreditsSample.movies(),
            tvShows = GroupedPersonCreditsSample.tvShows(),
          ),
        ),
      )

      onNodeWithTag(TestTags.Person.ABOUT_FORM).performScrollToIndex(1)

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
            knownForDepartment = KnownForDepartment.Acting.value,
            movies = GroupedPersonCreditsSample.movies(),
            tvShows = GroupedPersonCreditsSample.tvShows(),
          ),
        ),
      )

      onNodeWithTag(TestTags.Person.ABOUT_FORM).performScrollToNode(
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
            knownForDepartment = KnownForDepartment.Acting.value,
            movies = GroupedPersonCreditsSample.movies(),
            tvShows = GroupedPersonCreditsSample.tvShows(),
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
              knownForDepartment = KnownForDepartment.Acting.value,
              movies = GroupedPersonCreditsSample.movies(),
              tvShows = GroupedPersonCreditsSample.tvShows(),
            ),
          ),
        )

        onNodeWithTag(TestTags.LOADING_CONTENT).assertIsDisplayed()
      }
    }

  @Test
  fun `test loading content is gone when details are fetched with initial loading`() = runTest {
    val channel = Channel<Result<PersonDetailsResult>>()

    fetchPersonDetailsUseCase.mockSuccess(response = channel)

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
            knownForDepartment = KnownForDepartment.Acting.value,
            movies = GroupedPersonCreditsSample.movies(),
            tvShows = GroupedPersonCreditsSample.tvShows(),
          ),
        ),
      )

      onNodeWithTag(TestTags.LOADING_CONTENT).assertIsDisplayed()

      channel.send(
        Result.success(
          PersonDetailsResult.DetailsSuccess(PersonDetailsFactory.steveCarell()),
        ),
      )

      onNodeWithTag(TestTags.LOADING_CONTENT).assertIsNotDisplayed()
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
            knownForDepartment = KnownForDepartment.Acting.value,
            movies = GroupedPersonCreditsSample.movies(),
            tvShows = GroupedPersonCreditsSample.tvShows(),
          ),
        ),
      )

      onNodeWithContentDescription(
        getString(uiR.string.core_ui_movie_image_placeholder),
      ).assertIsDisplayed()

      onNodeWithTag(TestTags.Person.ABOUT_FORM).performScrollToNode(
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
            knownForDepartment = KnownForDepartment.Acting.value,
            movies = GroupedPersonCreditsSample.movies(),
            tvShows = GroupedPersonCreditsSample.tvShows(),
          ),
        ),
      )

      onNodeWithTag(TestTags.Person.CONTENT_LIST).performScrollToIndex(1)

      onNodeWithTag(TestTags.Person.KNOWN_FOR_SECTION).assertDoesNotExist()

      onNodeWithTag(TestTags.Person.KNOWN_FOR_SECTION).assertIsNotDisplayed()
      onNodeWithTag(TestTags.Person.KNOWN_FOR_SECTION_LIST).assertIsNotDisplayed()
    }
  }

  @Test
  fun `selecting Movies and TV Shows tabs displays correct content`() {
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
      // Switch to Movies tab
      onNodeWithTag(TestTags.Person.TAB_BAR.format(PersonTab.MOVIES.value)).performClick()
      onNodeWithTag(TestTags.Person.MOVIES_FORM.format(false)).assertIsDisplayed()

      // Switch to TV Shows tab
      onNodeWithText("TV Shows").performClick()
      onNodeWithTag(TestTags.Person.TV_SHOWS_FORM.format(false)).assertIsDisplayed()
    }
  }

  @Test
  fun `toggling layout style updates credit display format`() = runTest {
    val channel = Channel<Result<PersonDetailsResult>>()
    fetchPersonDetailsUseCase.mockSuccess(response = channel)

    val viewModel = PersonViewModel(
      fetchPersonDetailsUseCase = fetchPersonDetailsUseCase.mock,
      fetchChangesUseCase = fetchChangesUseCase.mock,
      savedStateHandle = savedStateHandle,
    )

    setContentWithTheme {
      PersonScreen(navigator = navigator, viewModel = viewModel)
    }

    with(composeTestRule) {
      // Load initial data
      channel.send(
        Result.success(PersonDetailsResult.DetailsSuccess(PersonDetailsFactory.steveCarell())),
      )
      channel.send(
        Result.success(
          PersonDetailsResult.CreditsSuccess(
            knownForCredits = PersonCastCreditFactory.knownFor(),
            knownForDepartment = KnownForDepartment.Acting.value,
            movies = GroupedPersonCreditsSample.movies(),
            tvShows = GroupedPersonCreditsSample.tvShows(),
          ),
        ),
      )

      // Switch to Movies tab
      onNodeWithText("Movies").performClick()

      // Verify initial grid layout
      onNodeWithTag(TestTags.Person.MOVIES_FORM.format(false)).assertIsDisplayed()
      onNodeWithTag(TestTags.Person.MOVIES_FORM.format(true)).assertIsNotDisplayed()

      onNodeWithTag(TestTags.Person.LAYOUT_SWITCHER).performClick()

      onNodeWithTag(TestTags.Person.MOVIES_FORM.format(true)).assertIsDisplayed()
      onNodeWithTag(TestTags.Person.MOVIES_FORM.format(false)).assertIsNotDisplayed()
    }
  }

  @Test
  fun `applying department filter updates displayed credits`() = runTest {
    val channel = Channel<Result<PersonDetailsResult>>()
    fetchPersonDetailsUseCase.mockSuccess(response = channel)

    val viewModel = PersonViewModel(
      fetchPersonDetailsUseCase = fetchPersonDetailsUseCase.mock,
      fetchChangesUseCase = fetchChangesUseCase.mock,
      savedStateHandle = savedStateHandle,
    )

    setContentWithTheme {
      PersonScreen(navigator = navigator, viewModel = viewModel)
    }

    with(composeTestRule) {
      channel.send(
        Result.success(PersonDetailsResult.DetailsSuccess(PersonDetailsFactory.steveCarell())),
      )
      channel.send(
        Result.success(
          PersonDetailsResult.CreditsSuccess(
            knownForCredits = emptyList(),
            knownForDepartment = KnownForDepartment.Acting.value,
            movies = GroupedPersonCreditsSample.movies(),
            tvShows = emptyMap(),
          ),
        ),
      )
      // Switch to Movies tab
      onNodeWithText("Movies").performClick()

      onNodeWithTag(TestTags.Person.DEPARTMENT_STICKY_HEADER.format("Acting")).assertIsDisplayed()
      onNodeWithText("Acting (3)").assertIsDisplayed()
      onNodeWithTag(TestTags.Person.MOVIES_FORM.format(false)).performScrollToIndex(1)
      onNodeWithTag(TestTags.Person.CREDIT_MEDIA_ITEM.format("Bruce Almighty")).assertIsDisplayed()

      // Open filter dialog
      onNodeWithTag(TestTags.Components.FILTER_BUTTON).performClick()

      // Select directing filter
      onNodeWithText("Production (2)").performClick()

      onNodeWithTag(TestTags.Person.DEPARTMENT_STICKY_HEADER).assertIsNotDisplayed()
      onNodeWithText("Acting").assertIsNotDisplayed()
      onNodeWithText(bruceAlmighty().title).assertIsNotDisplayed()
      onNodeWithText("Writing").assertIsNotDisplayed()

      onNodeWithText("Production (2)").assertIsDisplayed()
      onNodeWithTag(TestTags.Person.CREDIT_MEDIA_ITEM.format("Bruce Almighty"))
        .assertIsNotDisplayed()
    }
  }

  @Test
  fun `show empty state when no credits available`() = runTest {
    val channel = Channel<Result<PersonDetailsResult>>()
    fetchPersonDetailsUseCase.mockSuccess(response = channel)

    val viewModel = PersonViewModel(
      fetchPersonDetailsUseCase = fetchPersonDetailsUseCase.mock,
      fetchChangesUseCase = fetchChangesUseCase.mock,
      savedStateHandle = savedStateHandle,
    )

    setContentWithTheme {
      PersonScreen(navigator = navigator, viewModel = viewModel)
    }

    with(composeTestRule) {
      // Send empty credits
      channel.send(
        Result.success(PersonDetailsResult.DetailsSuccess(PersonDetailsFactory.steveCarell())),
      )
      channel.send(
        Result.success(
          PersonDetailsResult.CreditsSuccess(
            knownForCredits = emptyList(),
            knownForDepartment = KnownForDepartment.Acting.value,
            movies = emptyMap(),
            tvShows = emptyMap(),
          ),
        ),
      )

      onNodeWithTag(TestTags.Person.TAB_BAR.format(PersonTab.MOVIES.value)).performClick()

      // Verify empty state
      onNodeWithTag(
        TestTags.Person.EMPTY_CONTENT_CREDIT_CARD.format(MediaType.MOVIE.name),
      ).assertIsDisplayed()

      onNodeWithTag(TestTags.Person.TAB_BAR.format(PersonTab.TV_SHOWS.value)).performClick()

      // Verify empty state
      onNodeWithTag(
        TestTags.Person.EMPTY_CONTENT_CREDIT_CARD.format(MediaType.TV.name),
      ).assertIsDisplayed()
    }
  }

  @Test
  fun `display current department in header when no filters applied`() = runTest {
    val channel = Channel<Result<PersonDetailsResult>>()
    fetchPersonDetailsUseCase.mockSuccess(response = channel)

    val viewModel = PersonViewModel(
      fetchPersonDetailsUseCase = fetchPersonDetailsUseCase.mock,
      fetchChangesUseCase = fetchChangesUseCase.mock,
      savedStateHandle = savedStateHandle,
    )

    setContentWithTheme {
      PersonScreen(navigator = navigator, viewModel = viewModel)
    }

    with(composeTestRule) {
      channel.send(
        Result.success(PersonDetailsResult.DetailsSuccess(PersonDetailsFactory.steveCarell())),
      )
      channel.send(
        Result.success(
          PersonDetailsResult.CreditsSuccess(
            knownForCredits = emptyList(),
            knownForDepartment = KnownForDepartment.Acting.value,
            movies = GroupedPersonCreditsSample.movies(),
            tvShows = GroupedPersonCreditsSample.tvShows(),
          ),
        ),
      )
      // Switch to Movies tab
      onNodeWithTag(TestTags.Person.TAB_BAR.format(PersonTab.MOVIES.value)).performClick()

      onNodeWithTag(TestTags.Person.DEPARTMENT_STICKY_HEADER.format("Acting")).assertIsDisplayed()
      onNodeWithTag(
        TestTags.Person.DEPARTMENT_STICKY_HEADER.format("Writing"),
      ).assertIsNotDisplayed()

      onNodeWithTag(TestTags.Person.MOVIES_FORM.format(false)).performScrollToIndex(4)

      onNodeWithTag(TestTags.Person.DEPARTMENT_STICKY_HEADER.format("Writing")).assertIsDisplayed()

      // Switch to TV Shows tab
      onNodeWithTag(TestTags.Person.TAB_BAR.format(PersonTab.TV_SHOWS.value)).performClick()

      // Ensuring department is updated when switching tabs
      onNodeWithTag(TestTags.Person.DEPARTMENT_STICKY_HEADER.format("Acting")).assertIsDisplayed()
    }
  }

  @Test
  fun `filters persist when switching tabs`() = runTest {
    val channel = Channel<Result<PersonDetailsResult>>()
    fetchPersonDetailsUseCase.mockSuccess(response = channel)

    val viewModel = PersonViewModel(
      fetchPersonDetailsUseCase = fetchPersonDetailsUseCase.mock,
      fetchChangesUseCase = fetchChangesUseCase.mock,
      savedStateHandle = savedStateHandle,
    )

    setContentWithTheme {
      PersonScreen(navigator = navigator, viewModel = viewModel)
    }

    with(composeTestRule) {
      // Load data for both tabs
      channel.send(
        Result.success(PersonDetailsResult.DetailsSuccess(PersonDetailsFactory.steveCarell())),
      )
      channel.send(
        Result.success(
          PersonDetailsResult.CreditsSuccess(
            knownForCredits = emptyList(),
            knownForDepartment = KnownForDepartment.Acting.value,
            movies = GroupedPersonCreditsSample.movies(),
            tvShows = GroupedPersonCreditsSample.tvShows(),
          ),
        ),
      )
      onNodeWithText("Movies").performClick()

      // Apply filter to Movies tab
      onNodeWithTag(TestTags.Components.FILTER_BUTTON).performClick()
      onNodeWithText("Writing (1)").performClick()

      // Switch to TV Shows tab
      onNodeWithText("TV Shows").performClick()

      // Verify TV Shows tab has no filters
      onNodeWithTag(TestTags.Components.FILTER_BUTTON).assertIsDisplayed()
      onNodeWithText(getString(uiR.string.core_ui_filter)).assertIsDisplayed()
      onNodeWithTag(TestTags.Person.DEPARTMENT_STICKY_HEADER.format("Acting")).assertIsDisplayed()
    }
  }
}
