package com.divinelink.feature.lists.details

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsNotDisplayed
import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.isNotDisplayed
import androidx.compose.ui.test.longClick
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollToIndex
import androidx.compose.ui.test.performScrollToNode
import androidx.compose.ui.test.performTouchInput
import androidx.compose.ui.test.swipeDown
import androidx.lifecycle.SavedStateHandle
import com.divinelink.core.domain.components.SwitchViewButtonViewModel
import com.divinelink.core.fixtures.data.preferences.TestPreferencesRepository
import com.divinelink.core.fixtures.model.list.ListDetailsFactory
import com.divinelink.core.model.exception.AppException
import com.divinelink.core.model.media.MediaType
import com.divinelink.core.model.media.toStub
import com.divinelink.core.model.ui.ViewMode
import com.divinelink.core.navigation.route.Navigation
import com.divinelink.core.navigation.route.Navigation.DetailsRoute
import com.divinelink.core.navigation.route.Navigation.EditListRoute
import com.divinelink.core.testing.ComposeTest
import com.divinelink.core.testing.repository.TestListRepository
import com.divinelink.core.testing.setVisibilityScopeContent
import com.divinelink.core.testing.uiTest
import com.divinelink.core.testing.usecase.TestFetchListDetailsUseCase
import com.divinelink.core.testing.usecase.TestMarkAsFavoriteUseCase
import com.divinelink.core.ui.TestTags
import com.divinelink.core.ui.UiString
import com.divinelink.core.ui.resources.core_ui_close_multiple_select
import com.divinelink.core.ui.resources.core_ui_deselect_all
import com.divinelink.core.ui.resources.core_ui_select
import com.divinelink.core.ui.resources.core_ui_select_all
import com.divinelink.feature.add.to.account.modal.ActionMenuEntryPoint
import com.divinelink.feature.add.to.account.modal.ActionMenuViewModel
import com.divinelink.feature.add.to.account.resources.feature_add_to_account_list_title
import com.divinelink.feature.lists.details.ui.ListDetailsScreen
import io.kotest.matchers.shouldBe
import org.jetbrains.compose.resources.getString
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.test.mock.declare
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import com.divinelink.feature.add.to.account.resources.Res as R

class ListDetailsScreenTest : ComposeTest() {

  private val repository = TestListRepository()
  private val markAsFavoriteUseCase = TestMarkAsFavoriteUseCase()

  private lateinit var preferencesRepository: TestPreferencesRepository
  private lateinit var switchViewButtonViewModel: SwitchViewButtonViewModel

  @BeforeTest
  fun setup() {
    startKoin {
      // Do nothing
    }

    preferencesRepository = TestPreferencesRepository()
    switchViewButtonViewModel = SwitchViewButtonViewModel(
      repository = preferencesRepository,
    )
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
  fun `test on navigate up`() = uiTest {
    var navigatedUp = false
    val fetchListDetailsUseCase = TestFetchListDetailsUseCase()

    val viewModel = ListDetailsViewModel(
      fetchListDetailsUseCase = fetchListDetailsUseCase.mock,
      savedStateHandle = savedStateHandle,
      repository = repository.mock,
    )

    setVisibilityScopeContent {
      ListDetailsScreen(
        onNavigate = {
          if (it is Navigation.Back) {
            navigatedUp = true
          }
        },
        viewModel = viewModel,
        switchViewButtonViewModel = switchViewButtonViewModel,
      )
    }

    navigatedUp shouldBe false
    onNodeWithTag(TestTags.Components.TopAppBar.NAVIGATE_UP).performClick()
    navigatedUp shouldBe true
  }

  @Test
  fun `test refresh action`() = uiTest {
    val fetchListDetailsUseCase = TestFetchListDetailsUseCase()

    fetchListDetailsUseCase.mockResponse(
      Result.success(ListDetailsFactory.empty()),
    )

    val viewModel = ListDetailsViewModel(
      fetchListDetailsUseCase = fetchListDetailsUseCase.mock,
      savedStateHandle = savedStateHandle,
      repository = repository.mock,
    )

    setVisibilityScopeContent {
      ListDetailsScreen(
        onNavigate = {},
        viewModel = viewModel,
        switchViewButtonViewModel = switchViewButtonViewModel,
      )
    }

    onNodeWithTag(TestTags.Lists.Details.EMPTY_ITEM).assertIsDisplayed()

    fetchListDetailsUseCase.mockResponse(
      Result.success(ListDetailsFactory.mustWatch()),
    )

    onNodeWithTag(TestTags.Lists.Details.PULL_TO_REFRESH).performTouchInput {
      swipeDown()
    }

    waitUntil {
      onNodeWithTag(TestTags.Lists.Details.EMPTY_ITEM).isNotDisplayed()
    }
    onNodeWithTag(TestTags.Lists.Details.EMPTY_ITEM).assertIsNotDisplayed()

    onNodeWithText("The Wire").assertIsDisplayed()
  }

  @Test
  fun `test on media click`() = uiTest {
    val fetchListDetailsUseCase = TestFetchListDetailsUseCase()
    var detailsRoute: DetailsRoute? = null

    fetchListDetailsUseCase.mockResponse(
      Result.success(ListDetailsFactory.mustWatch()),
    )

    val viewModel = ListDetailsViewModel(
      fetchListDetailsUseCase = fetchListDetailsUseCase.mock,
      savedStateHandle = savedStateHandle,
      repository = repository.mock,
    )

    setVisibilityScopeContent {
      ListDetailsScreen(
        onNavigate = {
          if (it is DetailsRoute) {
            detailsRoute = it
          }
        },
        viewModel = viewModel,
        switchViewButtonViewModel = switchViewButtonViewModel,
      )
    }

    onNodeWithTag(TestTags.Lists.Details.EMPTY_ITEM).assertIsNotDisplayed()
    onNodeWithTag(TestTags.Components.MEDIA_GRID_CONTENT.format(ViewMode.GRID.value))
      .assertIsDisplayed()
      .performScrollToNode(hasText("The Wire"))

    detailsRoute shouldBe null

    onNodeWithText("The Wire").assertIsDisplayed().performClick()

    detailsRoute shouldBe DetailsRoute(
      id = 1438,
      mediaType = MediaType.TV.value,
      isFavorite = null,
    )
  }

  @Test
  fun `test on load more`() = uiTest {
    val fetchListDetailsUseCase = TestFetchListDetailsUseCase()

    fetchListDetailsUseCase.mockResponse(
      Result.success(ListDetailsFactory.page1()),
    )

    val viewModel = ListDetailsViewModel(
      fetchListDetailsUseCase = fetchListDetailsUseCase.mock,
      savedStateHandle = savedStateHandle,
      repository = repository.mock,
    )

    setVisibilityScopeContent {
      ListDetailsScreen(
        onNavigate = {},
        viewModel = viewModel,
        switchViewButtonViewModel = switchViewButtonViewModel,
      )
    }

    onNodeWithTag(TestTags.Lists.Details.EMPTY_ITEM).assertIsNotDisplayed()
    onNodeWithTag(
      TestTags.Components.MEDIA_GRID_CONTENT.format(ViewMode.GRID.value),
    ).assertIsDisplayed()

    onNodeWithText("Fight club 1").assertIsDisplayed()
    onNodeWithText("Fight club 16").assertIsNotDisplayed()
    onNodeWithText("Fight club 40").assertIsNotDisplayed()

    fetchListDetailsUseCase.mockResponse(
      Result.success(ListDetailsFactory.page2()),
    )

    onNodeWithTag(
      TestTags.Components.MEDIA_GRID_CONTENT.format(ViewMode.GRID.value),
    ).performScrollToIndex(
      24,
    )

    onNodeWithText("Fight club 1").assertIsNotDisplayed()
    onNodeWithText("Fight club 40").assertIsNotDisplayed()
    onNodeWithText("Fight club 20").assertIsDisplayed()

    onNodeWithTag(
      TestTags.Components.MEDIA_GRID_CONTENT.format(ViewMode.GRID.value),
    ).performScrollToIndex(
      42,
    )
    onNodeWithText("Fight club 40").assertIsDisplayed()
  }

  @Test
  fun `test on refresh with initial offline error`() = uiTest {
    val fetchListDetailsUseCase = TestFetchListDetailsUseCase()

    fetchListDetailsUseCase.mockResponse(
      Result.failure(AppException.Offline()),
    )

    val viewModel = ListDetailsViewModel(
      fetchListDetailsUseCase = fetchListDetailsUseCase.mock,
      savedStateHandle = savedStateHandle,
      repository = repository.mock,
    )

    setVisibilityScopeContent {
      ListDetailsScreen(
        onNavigate = {},
        viewModel = viewModel,
        switchViewButtonViewModel = switchViewButtonViewModel,
      )
    }

    onNodeWithTag(TestTags.BLANK_SLATE).assertIsDisplayed()

    fetchListDetailsUseCase.mockResponse(
      Result.success(ListDetailsFactory.mustWatch()),
    )

    onNodeWithText("Retry").assertIsDisplayed().performClick()

    onNodeWithTag(TestTags.BLANK_SLATE).assertIsNotDisplayed()
    onNodeWithTag(
      TestTags.Components.MEDIA_GRID_CONTENT.format(ViewMode.GRID.value),
    ).assertIsDisplayed()
  }

  @Test
  fun `test on refresh with generic error`() = uiTest {
    val fetchListDetailsUseCase = TestFetchListDetailsUseCase()

    fetchListDetailsUseCase.mockResponse(
      Result.failure(Exception("Foo")),
    )

    val viewModel = ListDetailsViewModel(
      fetchListDetailsUseCase = fetchListDetailsUseCase.mock,
      savedStateHandle = savedStateHandle,
      repository = repository.mock,
    )

    setVisibilityScopeContent {
      ListDetailsScreen(
        onNavigate = {},
        viewModel = viewModel,
        switchViewButtonViewModel = switchViewButtonViewModel,
      )
    }

    onNodeWithTag(TestTags.BLANK_SLATE).assertIsDisplayed()

    fetchListDetailsUseCase.mockResponse(
      Result.success(ListDetailsFactory.mustWatch()),
    )

    onNodeWithText("Retry").assertIsDisplayed().performClick()

    onNodeWithTag(TestTags.BLANK_SLATE).assertIsNotDisplayed()
    onNodeWithTag(
      TestTags.Components.MEDIA_GRID_CONTENT.format(ViewMode.GRID.value),
    ).assertIsDisplayed()
  }

  @Test
  fun `test on long press media shows action menu`() = uiTest {
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
        markAsFavoriteUseCase = markAsFavoriteUseCase,
      )
    }

    val viewModel = ListDetailsViewModel(
      fetchListDetailsUseCase = fetchListDetailsUseCase.mock,
      savedStateHandle = savedStateHandle,
      repository = repository.mock,
    )

    setVisibilityScopeContent {
      ListDetailsScreen(
        onNavigate = {},
        viewModel = viewModel,
        switchViewButtonViewModel = switchViewButtonViewModel,
      )
    }

    onNodeWithTag(TestTags.Lists.Details.EMPTY_ITEM).assertIsNotDisplayed()
    onNodeWithTag(TestTags.Components.MEDIA_GRID_CONTENT.format(ViewMode.GRID.value))
      .assertIsDisplayed()
      .performScrollToNode(hasText("Fight club 1"))

    onNodeWithText("Fight club 1").assertIsDisplayed().performTouchInput {
      longClick()
    }

    onNodeWithTag(TestTags.Modal.ACTION_MENU).assertIsDisplayed()
  }

  @Test
  fun `test navigate to edit using fab`() = uiTest {
    var editListRoute: EditListRoute? = null
    val fetchListDetailsUseCase = TestFetchListDetailsUseCase()

    fetchListDetailsUseCase.mockResponse(
      Result.success(ListDetailsFactory.page1()),
    )

    val viewModel = ListDetailsViewModel(
      fetchListDetailsUseCase = fetchListDetailsUseCase.mock,
      savedStateHandle = savedStateHandle,
      repository = repository.mock,
    )

    setVisibilityScopeContent {
      ListDetailsScreen(
        onNavigate = {
          if (it is EditListRoute) {
            editListRoute = it
          }
        },
        viewModel = viewModel,
        switchViewButtonViewModel = switchViewButtonViewModel,
      )
    }

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

  @Test
  fun `test select multiple items`() = uiTest {
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
        markAsFavoriteUseCase = markAsFavoriteUseCase,
      )
    }

    val viewModel = ListDetailsViewModel(
      fetchListDetailsUseCase = fetchListDetailsUseCase.mock,
      savedStateHandle = savedStateHandle,
      repository = repository.mock,
    )

    setVisibilityScopeContent {
      ListDetailsScreen(
        onNavigate = {},
        viewModel = viewModel,
        switchViewButtonViewModel = switchViewButtonViewModel,
      )
    }

    onNodeWithTag(TestTags.Lists.Details.EMPTY_ITEM).assertIsNotDisplayed()
    onNodeWithTag(
      TestTags.Components.MEDIA_GRID_CONTENT.format(ViewMode.GRID.value),
    )
      .performScrollToNode(hasText("Fight club 1"))
      .assertIsDisplayed()

    onNodeWithText("Fight club 1").assertIsDisplayed().performTouchInput {
      longClick()
    }

    onNodeWithTag(TestTags.Modal.ACTION_MENU).assertIsDisplayed()

    onNodeWithTag(TestTags.Components.MULTIPLE_SELECT_HEADER).assertIsNotDisplayed()

    onNodeWithText(getString(UiString.core_ui_select)).performClick()

    onNodeWithTag(TestTags.Components.MULTIPLE_SELECT_HEADER).assertIsDisplayed()

    onNodeWithContentDescription(
      TestTags.Lists.Details.SELECTED_CARD.format(
        ListDetailsFactory.page1().media.first().name,
        true,
      ),
    ).assertIsDisplayed()

    onNodeWithText("1/20 selected").assertIsDisplayed()

    onNodeWithTag(TestTags.Components.MEDIA_GRID_CONTENT.format(ViewMode.GRID.value))
      .performScrollToIndex(6)

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
    onNodeWithContentDescription(getString(UiString.core_ui_select_all)).performClick()
    onNodeWithText("20/20 selected").assertIsDisplayed()

    // Deselect all items
    onNodeWithContentDescription(getString(UiString.core_ui_deselect_all)).performClick()
    onNodeWithText("0/20 selected").assertIsDisplayed()

    // Dismiss selection
    onNodeWithContentDescription(
      getString(UiString.core_ui_close_multiple_select),
    ).performClick()

    onNodeWithTag(TestTags.Components.MULTIPLE_SELECT_HEADER).assertIsNotDisplayed()
  }

  @Test
  fun `test on navigate to add to list`() = uiTest {
    var addToListRoute: Navigation.AddToListRoute? = null
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
        markAsFavoriteUseCase = markAsFavoriteUseCase,
      )
    }

    val viewModel = ListDetailsViewModel(
      fetchListDetailsUseCase = fetchListDetailsUseCase.mock,
      savedStateHandle = savedStateHandle,
      repository = repository.mock,
    )

    setVisibilityScopeContent {
      ListDetailsScreen(
        onNavigate = {
          if (it is Navigation.AddToListRoute) {
            addToListRoute = it
          }
        },
        viewModel = viewModel,
        switchViewButtonViewModel = switchViewButtonViewModel,
      )
    }

    onNodeWithTag(TestTags.Components.MEDIA_GRID_CONTENT.format(ViewMode.GRID.value))
      .performScrollToNode(hasText("Fight club 1"))

    onNodeWithText("Fight club 1").assertIsDisplayed().performTouchInput {
      longClick()
    }

    onNodeWithTag(TestTags.Modal.ACTION_MENU).assertIsDisplayed()

    onNodeWithText(
      getString(R.string.feature_add_to_account_list_title),
    ).performClick()

    val stub = ListDetailsFactory.page1().media.first().toStub()

    addToListRoute shouldBe Navigation.AddToListRoute(
      id = stub.mediaId,
      mediaType = stub.mediaType.value,
    )
  }

  @Test
  fun `test update view mode`() = uiTest {
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
        markAsFavoriteUseCase = markAsFavoriteUseCase,
      )
    }

    val viewModel = ListDetailsViewModel(
      fetchListDetailsUseCase = fetchListDetailsUseCase.mock,
      savedStateHandle = savedStateHandle,
      repository = repository.mock,
    )

    setVisibilityScopeContent(
      preferencesRepository = preferencesRepository,
    ) {
      ListDetailsScreen(
        onNavigate = {},
        viewModel = viewModel,
        switchViewButtonViewModel = switchViewButtonViewModel,
      )
    }

    onNodeWithTag(TestTags.Components.MEDIA_GRID_CONTENT.format(ViewMode.GRID.value))
      .assertIsDisplayed()
      .performScrollToNode(
        hasTestTag(TestTags.Components.Button.SWITCH_VIEW),
      )
      .performScrollToIndex(1)

    onNodeWithTag(TestTags.Components.Button.SWITCH_VIEW).performClick()

    onNodeWithTag(
      TestTags.Components.MEDIA_GRID_CONTENT.format(ViewMode.LIST.value),
    ).assertIsDisplayed()
  }
}
