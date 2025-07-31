package com.divinelink.feature.lists.details

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsNotDisplayed
import androidx.compose.ui.test.longClick
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollToIndex
import androidx.compose.ui.test.performTouchInput
import androidx.compose.ui.test.swipeDown
import androidx.lifecycle.SavedStateHandle
import com.divinelink.core.fixtures.model.list.ListDetailsFactory
import com.divinelink.core.model.exception.AppException
import com.divinelink.core.model.media.MediaType
import com.divinelink.core.model.media.toStub
import com.divinelink.core.navigation.route.AddToListRoute
import com.divinelink.core.navigation.route.DetailsRoute
import com.divinelink.core.navigation.route.EditListRoute
import com.divinelink.core.testing.ComposeTest
import com.divinelink.core.testing.getString
import com.divinelink.core.testing.repository.TestListRepository
import com.divinelink.core.testing.setVisibilityScopeContent
import com.divinelink.core.testing.usecase.TestFetchListDetailsUseCase
import com.divinelink.core.ui.TestTags
import com.divinelink.feature.add.to.account.R
import com.divinelink.feature.add.to.account.modal.ActionMenuEntryPoint
import com.divinelink.feature.add.to.account.modal.ActionMenuViewModel
import com.divinelink.feature.lists.details.ui.ListDetailsScreen
import com.google.common.truth.Truth.assertThat
import io.kotest.matchers.shouldBe
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.test.mock.declare
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import com.divinelink.core.ui.R as uiR

class ListDetailsScreenTest : ComposeTest() {

  private val repository = TestListRepository()

  @BeforeTest
  fun setup() {
    startKoin {
      androidContext(composeTestRule.activity)
    }
  }

  @AfterTest
  fun tearDown() {
    stopKoin()
  }

  private val savedStateHandle = SavedStateHandle(
    mapOf(
      "id" to 1,
      "name" to ListDetailsFactory.mustWatch().name,
      "backdropPath" to ListDetailsFactory.mustWatch().backdropPath,
      "description" to ListDetailsFactory.mustWatch().description,
      "public" to ListDetailsFactory.mustWatch().public,
    ),
  )

  @Test
  fun `test top app bar is displayed but title is hidden initially`() {
    val fetchListDetailsUseCase = TestFetchListDetailsUseCase()

    fetchListDetailsUseCase.mockResponse(Result.success(ListDetailsFactory.page1()))
    setVisibilityScopeContent {
      ListDetailsScreen(
        onNavigateUp = {},
        onNavigateToMediaDetails = {},
        onNavigateToEdit = {},
        onNavigateToAddToList = {},
        viewModel = ListDetailsViewModel(
          fetchListDetailsUseCase = fetchListDetailsUseCase.mock,
          savedStateHandle = savedStateHandle,
          repository = repository.mock,

        ),
      )
    }

    with(composeTestRule) {
      onNodeWithTag(TestTags.Components.TopAppBar.TOP_APP_BAR).assertIsDisplayed()
      onNodeWithTag(TestTags.Components.TopAppBar.TOP_APP_BAR_TITLE).assertIsNotDisplayed()

      onNodeWithTag(TestTags.Components.MEDIA_LIST_CONTENT).performScrollToIndex(12)

      onNodeWithTag(TestTags.Components.TopAppBar.TOP_APP_BAR_TITLE).assertIsDisplayed()
    }
  }

  @Test
  fun `test on navigate up`() {
    var navigatedUp = false
    val fetchListDetailsUseCase = TestFetchListDetailsUseCase()

    setVisibilityScopeContent {
      ListDetailsScreen(
        onNavigateUp = {
          navigatedUp = true
        },
        onNavigateToMediaDetails = {},
        onNavigateToEdit = {},
        onNavigateToAddToList = {},
        viewModel = ListDetailsViewModel(
          fetchListDetailsUseCase = fetchListDetailsUseCase.mock,
          savedStateHandle = savedStateHandle,
          repository = repository.mock,
        ),
      )
    }

    with(composeTestRule) {
      assertThat(navigatedUp).isFalse()
      onNodeWithTag(TestTags.Components.TopAppBar.NAVIGATE_UP).performClick()
      assertThat(navigatedUp).isTrue()
    }
  }

  @Test
  fun `test refresh action`() {
    val fetchListDetailsUseCase = TestFetchListDetailsUseCase()

    fetchListDetailsUseCase.mockResponse(
      Result.success(ListDetailsFactory.empty()),
    )

    setVisibilityScopeContent {
      ListDetailsScreen(
        onNavigateUp = {},
        onNavigateToMediaDetails = {},
        onNavigateToEdit = {},
        onNavigateToAddToList = {},
        viewModel = ListDetailsViewModel(
          fetchListDetailsUseCase = fetchListDetailsUseCase.mock,
          savedStateHandle = savedStateHandle,
          repository = repository.mock,
        ),
      )
    }

    with(composeTestRule) {
      onNodeWithTag(TestTags.Lists.Details.EMPTY_ITEM).assertIsDisplayed()

      fetchListDetailsUseCase.mockResponse(
        Result.success(ListDetailsFactory.mustWatch()),
      )

      onNodeWithTag(TestTags.Lists.Details.PULL_TO_REFRESH).performTouchInput {
        swipeDown()
      }

      onNodeWithTag(TestTags.Lists.Details.EMPTY_ITEM).assertIsNotDisplayed()

      onNodeWithText("The Wire").assertIsDisplayed()

      fetchListDetailsUseCase.mockResponse(
        Result.success(ListDetailsFactory.empty()),
      )

      onNodeWithTag(TestTags.Lists.Details.PULL_TO_REFRESH).performTouchInput {
        swipeDown()
      }

      onNodeWithTag(TestTags.Lists.Details.EMPTY_ITEM).assertIsDisplayed()
    }
  }

  @Test
  fun `test on media click`() {
    val fetchListDetailsUseCase = TestFetchListDetailsUseCase()
    var detailsRoute: DetailsRoute? = null

    fetchListDetailsUseCase.mockResponse(
      Result.success(ListDetailsFactory.mustWatch()),
    )

    setVisibilityScopeContent {
      ListDetailsScreen(
        onNavigateUp = {},
        onNavigateToMediaDetails = {
          detailsRoute = it
        },
        onNavigateToEdit = {},
        onNavigateToAddToList = {},
        viewModel = ListDetailsViewModel(
          fetchListDetailsUseCase = fetchListDetailsUseCase.mock,
          savedStateHandle = savedStateHandle,
          repository = repository.mock,
        ),
      )
    }

    with(composeTestRule) {
      onNodeWithTag(TestTags.Lists.Details.EMPTY_ITEM).assertIsNotDisplayed()
      onNodeWithTag(TestTags.Components.MEDIA_LIST_CONTENT).assertIsDisplayed()

      assertThat(detailsRoute).isNull()

      onNodeWithText("The Wire").assertIsDisplayed().performClick()

      assertThat(detailsRoute).isEqualTo(
        DetailsRoute(
          id = 1438,
          mediaType = MediaType.TV,
          isFavorite = null,
        ),
      )
    }
  }

  @Test
  fun `test on load more`() {
    val fetchListDetailsUseCase = TestFetchListDetailsUseCase()

    fetchListDetailsUseCase.mockResponse(
      Result.success(ListDetailsFactory.page1()),
    )

    setVisibilityScopeContent {
      ListDetailsScreen(
        onNavigateUp = {},
        onNavigateToMediaDetails = {},
        onNavigateToEdit = {},
        onNavigateToAddToList = {},
        viewModel = ListDetailsViewModel(
          fetchListDetailsUseCase = fetchListDetailsUseCase.mock,
          savedStateHandle = savedStateHandle,
          repository = repository.mock,
        ),
      )
    }

    with(composeTestRule) {
      onNodeWithTag(TestTags.Lists.Details.EMPTY_ITEM).assertIsNotDisplayed()
      onNodeWithTag(TestTags.Components.MEDIA_LIST_CONTENT).assertIsDisplayed()

      onNodeWithText("Fight club 1").assertIsDisplayed()
      onNodeWithText("Fight club 16").assertIsNotDisplayed()
      onNodeWithText("Fight club 40").assertIsNotDisplayed()

      fetchListDetailsUseCase.mockResponse(
        Result.success(ListDetailsFactory.page2()),
      )

      onNodeWithTag(TestTags.Components.MEDIA_LIST_CONTENT).performScrollToIndex(19)

      onNodeWithText("Fight club 1").assertIsNotDisplayed()
      onNodeWithText("Fight club 40").assertIsNotDisplayed()
      onNodeWithText("Fight club 17").assertIsDisplayed()

      onNodeWithTag(TestTags.Components.MEDIA_LIST_CONTENT).performScrollToIndex(42)
      onNodeWithText("Fight club 40").assertIsDisplayed()
    }
  }

  @Test
  fun `test on refresh with initial offline error`() {
    val fetchListDetailsUseCase = TestFetchListDetailsUseCase()

    fetchListDetailsUseCase.mockResponse(
      Result.failure(AppException.Offline()),
    )

    setVisibilityScopeContent {
      ListDetailsScreen(
        onNavigateUp = {},
        onNavigateToMediaDetails = {},
        onNavigateToEdit = {},
        onNavigateToAddToList = {},
        viewModel = ListDetailsViewModel(
          fetchListDetailsUseCase = fetchListDetailsUseCase.mock,
          savedStateHandle = savedStateHandle,
          repository = repository.mock,
        ),
      )
    }

    with(composeTestRule) {
      onNodeWithTag(TestTags.BLANK_SLATE).assertIsDisplayed()

      fetchListDetailsUseCase.mockResponse(
        Result.success(ListDetailsFactory.mustWatch()),
      )

      onNodeWithText("Retry").assertIsDisplayed().performClick()

      onNodeWithTag(TestTags.BLANK_SLATE).assertIsNotDisplayed()
      onNodeWithTag(TestTags.Components.MEDIA_LIST_CONTENT).assertIsDisplayed()
    }
  }

  @Test
  fun `test on refresh with generic error`() {
    val fetchListDetailsUseCase = TestFetchListDetailsUseCase()

    fetchListDetailsUseCase.mockResponse(
      Result.failure(Exception("Foo")),
    )

    setVisibilityScopeContent {
      ListDetailsScreen(
        onNavigateUp = {},
        onNavigateToMediaDetails = {},
        onNavigateToEdit = {},
        onNavigateToAddToList = {},
        viewModel = ListDetailsViewModel(
          fetchListDetailsUseCase = fetchListDetailsUseCase.mock,
          savedStateHandle = savedStateHandle,
          repository = repository.mock,
        ),
      )
    }

    with(composeTestRule) {
      onNodeWithTag(TestTags.BLANK_SLATE).assertIsDisplayed()

      fetchListDetailsUseCase.mockResponse(
        Result.success(ListDetailsFactory.mustWatch()),
      )

      onNodeWithText("Retry").assertIsDisplayed().performClick()

      onNodeWithTag(TestTags.BLANK_SLATE).assertIsNotDisplayed()
      onNodeWithTag(TestTags.Components.MEDIA_LIST_CONTENT).assertIsDisplayed()
    }
  }

  @Test
  fun `test on long press media shows action menu`() {
    val fetchListDetailsUseCase = TestFetchListDetailsUseCase()

    fetchListDetailsUseCase.mockResponse(
      Result.success(ListDetailsFactory.page1()),
    )

    declare {
      ActionMenuViewModel(
        entryPoint = ActionMenuEntryPoint.ListDetails(
          listId = 1234,
          listName = "TV Shows",
        ),
        mediaItem = ListDetailsFactory.page1().media.first(),
        listRepository = repository.mock,
      )
    }

    setVisibilityScopeContent {
      ListDetailsScreen(
        onNavigateUp = {},
        onNavigateToMediaDetails = {},
        onNavigateToEdit = {},
        onNavigateToAddToList = {},
        viewModel = ListDetailsViewModel(
          fetchListDetailsUseCase = fetchListDetailsUseCase.mock,
          savedStateHandle = savedStateHandle,
          repository = repository.mock,
        ),
      )
    }

    with(composeTestRule) {
      onNodeWithTag(TestTags.Lists.Details.EMPTY_ITEM).assertIsNotDisplayed()
      onNodeWithTag(TestTags.Components.MEDIA_LIST_CONTENT).assertIsDisplayed()

      onNodeWithText("Fight club 1").assertIsDisplayed().performTouchInput {
        longClick()
      }

      onNodeWithTag(TestTags.Modal.ACTION_MENU).assertIsDisplayed()
    }
  }

  @Test
  fun `test navigate to edit using fab`() {
    var editListRoute: EditListRoute? = null
    val fetchListDetailsUseCase = TestFetchListDetailsUseCase()

    fetchListDetailsUseCase.mockResponse(
      Result.success(ListDetailsFactory.page1()),
    )

    setVisibilityScopeContent {
      ListDetailsScreen(
        onNavigateUp = {},
        onNavigateToMediaDetails = {},
        onNavigateToEdit = {
          editListRoute = it
        },
        onNavigateToAddToList = {},
        viewModel = ListDetailsViewModel(
          fetchListDetailsUseCase = fetchListDetailsUseCase.mock,
          savedStateHandle = savedStateHandle,
          repository = repository.mock,
        ),
      )
    }

    with(composeTestRule) {
      editListRoute shouldBe null

      onNodeWithTag(TestTags.Lists.Details.EDIT_LIST_FAB).performClick()

      editListRoute shouldBe EditListRoute(
        id = 1,
        name = ListDetailsFactory.page1().name,
        backdropPath = ListDetailsFactory.page1().backdropPath,
        description = ListDetailsFactory.page1().description,
        public = ListDetailsFactory.page1().public,
      )
    }
  }

  @Test
  fun `test select multiple items`() {
    val fetchListDetailsUseCase = TestFetchListDetailsUseCase()

    fetchListDetailsUseCase.mockResponse(
      Result.success(ListDetailsFactory.page1()),
    )

    declare {
      ActionMenuViewModel(
        entryPoint = ActionMenuEntryPoint.ListDetails(
          listId = 1234,
          listName = "TV Shows",
        ),
        mediaItem = ListDetailsFactory.page1().media.first(),
        listRepository = repository.mock,
      )
    }

    setVisibilityScopeContent {
      ListDetailsScreen(
        onNavigateUp = {},
        onNavigateToMediaDetails = {},
        onNavigateToEdit = {},
        onNavigateToAddToList = {},
        viewModel = ListDetailsViewModel(
          fetchListDetailsUseCase = fetchListDetailsUseCase.mock,
          savedStateHandle = savedStateHandle,
          repository = repository.mock,
        ),
      )
    }

    with(composeTestRule) {
      onNodeWithTag(TestTags.Lists.Details.EMPTY_ITEM).assertIsNotDisplayed()
      onNodeWithTag(TestTags.Components.MEDIA_LIST_CONTENT).assertIsDisplayed()

      onNodeWithText("Fight club 1").assertIsDisplayed().performTouchInput {
        longClick()
      }

      onNodeWithTag(TestTags.Modal.ACTION_MENU).assertIsDisplayed()

      onNodeWithTag(TestTags.Components.MULTIPLE_SELECT_HEADER).assertIsNotDisplayed()

      onNodeWithText(getString(com.divinelink.core.ui.R.string.core_ui_select)).performClick()

      onNodeWithTag(TestTags.Components.MULTIPLE_SELECT_HEADER).assertIsDisplayed()

      onNodeWithContentDescription(
        TestTags.Lists.Details.SELECTED_CARD.format(
          ListDetailsFactory.page1().media.first().name,
          true,
        ),
      ).assertIsDisplayed()

      onNodeWithText("1/20 selected").assertIsDisplayed()

      onNodeWithTag(TestTags.Components.MEDIA_LIST_CONTENT).performScrollToIndex(3)

      // Select second item
      onNodeWithContentDescription(
        TestTags.Lists.Details.SELECTED_CARD.format(
          ListDetailsFactory.page1().media[1].name,
          false,
        ),
      )
        .assertIsDisplayed()
        .performClick()
        .assertIsNotDisplayed()

      onNodeWithContentDescription(
        TestTags.Lists.Details.SELECTED_CARD.format(
          ListDetailsFactory.page1().media[1].name,
          true,
        ),
      ).assertIsDisplayed()

      onNodeWithText("2/20 selected").assertIsDisplayed()

      // Re-selecting same item removes it from selection
      onNodeWithContentDescription(
        TestTags.Lists.Details.SELECTED_CARD.format(
          ListDetailsFactory.page1().media[1].name,
          true,
        ),
      ).performClick()
      onNodeWithText("1/20 selected").assertIsDisplayed()

      // Select all items
      onNodeWithContentDescription(getString(uiR.string.core_ui_select_all)).performClick()
      onNodeWithText("20/20 selected").assertIsDisplayed()

      // Deselect all items
      onNodeWithContentDescription(getString(uiR.string.core_ui_deselect_all)).performClick()
      onNodeWithText("0/20 selected").assertIsDisplayed()

      // Dismiss selection
      onNodeWithContentDescription(
        getString(uiR.string.core_ui_close_multiple_select),
      ).performClick()

      onNodeWithTag(TestTags.Components.MULTIPLE_SELECT_HEADER).assertIsNotDisplayed()
    }
  }

  @Test
  fun `test on navigate to add to list`() {
    var addToListRoute: AddToListRoute? = null
    val fetchListDetailsUseCase = TestFetchListDetailsUseCase()

    fetchListDetailsUseCase.mockResponse(
      Result.success(ListDetailsFactory.page1()),
    )

    declare {
      ActionMenuViewModel(
        entryPoint = ActionMenuEntryPoint.ListDetails(
          listId = 1234,
          listName = "TV Shows",
        ),
        mediaItem = ListDetailsFactory.page1().media.first(),
        listRepository = repository.mock,
      )
    }

    setVisibilityScopeContent {
      ListDetailsScreen(
        onNavigateUp = {},
        onNavigateToMediaDetails = {},
        onNavigateToEdit = {},
        onNavigateToAddToList = {
          addToListRoute = it
        },
        viewModel = ListDetailsViewModel(
          fetchListDetailsUseCase = fetchListDetailsUseCase.mock,
          savedStateHandle = savedStateHandle,
          repository = repository.mock,
        ),
      )
    }

    with(composeTestRule) {
      onNodeWithText("Fight club 1").assertIsDisplayed().performTouchInput {
        longClick()
      }

      onNodeWithTag(TestTags.Modal.ACTION_MENU).assertIsDisplayed()

      onNodeWithText(
        getString(R.string.feature_add_to_account_list_title),
      ).performClick()

      addToListRoute shouldBe AddToListRoute(
        media = ListDetailsFactory.page1().media.first().toStub(),
      )
    }
  }
}
