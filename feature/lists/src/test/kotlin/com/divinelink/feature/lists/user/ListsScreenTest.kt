package com.divinelink.feature.lists.user

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsNotDisplayed
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTouchInput
import androidx.compose.ui.test.swipeDown
import com.divinelink.core.domain.components.SwitchViewButtonViewModel
import com.divinelink.core.fixtures.data.preferences.TestPreferencesRepository
import com.divinelink.core.fixtures.model.list.ListItemFactory
import com.divinelink.core.model.exception.SessionException
import com.divinelink.core.model.ui.UiPreferences
import com.divinelink.core.model.ui.ViewMode
import com.divinelink.core.navigation.route.ListDetailsRoute
import com.divinelink.core.testing.ComposeTest
import com.divinelink.core.testing.setVisibilityScopeContent
import com.divinelink.core.testing.usecase.TestFetchUserListsUseCase
import com.divinelink.core.ui.TestTags
import com.google.common.truth.Truth.assertThat
import kotlin.test.Test

class ListsScreenTest : ComposeTest() {

  private val fetchUserListsUseCase = TestFetchUserListsUseCase()

  private val preferencesRepository = TestPreferencesRepository()
  private val switchViewButtonViewModel = SwitchViewButtonViewModel(
    repository = preferencesRepository,
  )

  @Test
  fun `test unauthenticated shows fab`() {
    fetchUserListsUseCase.mockResponse(
      Result.failure(SessionException.Unauthenticated()),
    )
    setVisibilityScopeContent(
      preferencesRepository = preferencesRepository,
    ) {
      ListsScreen(
        onNavigateToTMDBLogin = {},
        onNavigateUp = {},
        onNavigateToList = {},
        viewModel = ListsViewModel(
          fetchUserListsUseCase = fetchUserListsUseCase.mock,
        ),
        switchViewButtonViewModel = switchViewButtonViewModel,
      )
    }

    with(composeTestRule) {
      onNodeWithText("Login").assertIsDisplayed()
      onNodeWithTag(TestTags.BLANK_SLATE).assertIsDisplayed()
    }
  }

  @Test
  fun `test display lists`() {
    fetchUserListsUseCase.mockResponse(Result.success(ListItemFactory.page1()))

    setVisibilityScopeContent(
      preferencesRepository = preferencesRepository,
    ) {
      ListsScreen(
        onNavigateToTMDBLogin = {},
        onNavigateUp = {},
        onNavigateToList = {},
        viewModel = ListsViewModel(
          fetchUserListsUseCase = fetchUserListsUseCase.mock,
        ),
        switchViewButtonViewModel = switchViewButtonViewModel,
      )
    }

    with(composeTestRule) {
      onNodeWithTag(
        TestTags.Lists.SCROLLABLE_CONTENT.format(ViewMode.LIST.value),
      ).assertIsDisplayed()
      onNodeWithText("Elsolist 2").assertIsDisplayed()
    }
  }

  @Test
  fun `test refresh list`() {
    fetchUserListsUseCase.mockResponse(
      Result.success(ListItemFactory.page2()),
    )

    setVisibilityScopeContent(
      preferencesRepository = preferencesRepository,
    ) {
      ListsScreen(
        onNavigateToTMDBLogin = {},
        onNavigateUp = {},
        onNavigateToList = {},
        viewModel = ListsViewModel(
          fetchUserListsUseCase = fetchUserListsUseCase.mock,
        ),
        switchViewButtonViewModel = switchViewButtonViewModel,
      )
    }

    with(composeTestRule) {
      onNodeWithText("Elsolist").assertIsNotDisplayed()
      fetchUserListsUseCase.mockResponse(
        Result.success(ListItemFactory.page1()),
      )

      onNodeWithTag(TestTags.Lists.PULL_TO_REFRESH).performTouchInput {
        swipeDown()
      }

      onNodeWithText("Elsolist").assertIsDisplayed()
    }
  }

  @Test
  fun `test on navigate to list on list mode`() {
    var navigateRoute: ListDetailsRoute? = null
    fetchUserListsUseCase.mockResponse(Result.success(ListItemFactory.page1()))

    setVisibilityScopeContent(
      preferencesRepository = preferencesRepository,
    ) {
      ListsScreen(
        onNavigateToTMDBLogin = {},
        onNavigateUp = {},
        onNavigateToList = {
          navigateRoute = it
        },
        viewModel = ListsViewModel(
          fetchUserListsUseCase = fetchUserListsUseCase.mock,
        ),
        switchViewButtonViewModel = switchViewButtonViewModel,
      )
    }

    with(composeTestRule) {
      onNodeWithTag(
        TestTags.Lists.SCROLLABLE_CONTENT.format(ViewMode.LIST.value),
      ).assertIsDisplayed()
      onNodeWithText("Elsolist 2").assertIsDisplayed().performClick()
    }

    assertThat(navigateRoute).isEqualTo(
      ListDetailsRoute(
        id = 8452378,
        name = "Elsolist 2",
        backdropPath = "/4JNggqfyJWREqb0enzpUMbvIniV.jpg",
        description = "This is a new list to test v4 lists",
        public = true,
      ),
    )
  }

  @Test
  fun `test on navigate to list on grid mode`() {
    var navigateRoute: ListDetailsRoute? = null
    fetchUserListsUseCase.mockResponse(Result.success(ListItemFactory.page1()))

    setVisibilityScopeContent(
      preferencesRepository = TestPreferencesRepository(
        UiPreferences.Initial.copy(
          listsViewMode = ViewMode.GRID,
        ),
      ),
    ) {
      ListsScreen(
        onNavigateToTMDBLogin = {},
        onNavigateUp = {},
        onNavigateToList = {
          navigateRoute = it
        },
        viewModel = ListsViewModel(
          fetchUserListsUseCase = fetchUserListsUseCase.mock,
        ),
        switchViewButtonViewModel = switchViewButtonViewModel,
      )
    }

    with(composeTestRule) {
      onNodeWithTag(
        TestTags.Lists.SCROLLABLE_CONTENT.format(ViewMode.GRID.value),
      ).assertIsDisplayed()
      onNodeWithText("Elsolist 2").assertIsDisplayed().performClick()
    }

    assertThat(navigateRoute).isEqualTo(
      ListDetailsRoute(
        id = 8452378,
        name = "Elsolist 2",
        backdropPath = "/4JNggqfyJWREqb0enzpUMbvIniV.jpg",
        description = "This is a new list to test v4 lists",
        public = true,
      ),
    )
  }

  @Test
  fun `test update view mode`() {
    fetchUserListsUseCase.mockResponse(
      Result.success(ListItemFactory.page1()),
    )

    setVisibilityScopeContent(
      preferencesRepository = preferencesRepository,
    ) {
      ListsScreen(
        onNavigateToTMDBLogin = {},
        onNavigateUp = {},
        onNavigateToList = {},
        viewModel = ListsViewModel(
          fetchUserListsUseCase = fetchUserListsUseCase.mock,
        ),
        switchViewButtonViewModel = switchViewButtonViewModel,
      )
    }

    with(composeTestRule) {
      onNodeWithTag(
        TestTags.Lists.SCROLLABLE_CONTENT.format(ViewMode.LIST.value),
      ).assertIsDisplayed()

      onNodeWithTag(TestTags.Components.Button.SWITCH_VIEW).performClick()

      onNodeWithTag(
        TestTags.Lists.SCROLLABLE_CONTENT.format(ViewMode.GRID.value),
      ).assertIsDisplayed()
    }
  }
}
