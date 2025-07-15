package com.divinelink.feature.lists.user

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.divinelink.core.fixtures.model.list.ListItemFactory
import com.divinelink.core.model.exception.SessionException
import com.divinelink.core.navigation.route.ListDetailsRoute
import com.divinelink.core.testing.ComposeTest
import com.divinelink.core.testing.setVisibilityScopeContent
import com.divinelink.core.testing.usecase.TestFetchUserListsUseCase
import com.divinelink.core.ui.TestTags
import com.google.common.truth.Truth.assertThat
import kotlin.test.Test

class ListsScreenTest : ComposeTest() {

  private val fetchUserListsUseCase = TestFetchUserListsUseCase()

  @Test
  fun `test unauthenticated shows fab`() {
    fetchUserListsUseCase.mockResponse(
      Result.failure(SessionException.Unauthenticated()),
    )
    setVisibilityScopeContent {
      ListsScreen(
        onNavigateToTMDBLogin = {},
        onNavigateUp = {},
        onNavigateToList = {},
        viewModel = ListsViewModel(
          fetchUserListsUseCase = fetchUserListsUseCase.mock,
        ),
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

    setVisibilityScopeContent {
      ListsScreen(
        onNavigateToTMDBLogin = {},
        onNavigateUp = {},
        onNavigateToList = {},
        viewModel = ListsViewModel(
          fetchUserListsUseCase = fetchUserListsUseCase.mock,
        ),
      )
    }

    with(composeTestRule) {
      onNodeWithTag(TestTags.Lists.SCROLLABLE_CONTENT).assertIsDisplayed()
      onNodeWithText("Elsolist 2").assertIsDisplayed()
    }
  }

  @Test
  fun `test on navigate to list`() {
    var navigateRoute: ListDetailsRoute? = null
    fetchUserListsUseCase.mockResponse(Result.success(ListItemFactory.page1()))

    setVisibilityScopeContent {
      ListsScreen(
        onNavigateToTMDBLogin = {},
        onNavigateUp = {},
        onNavigateToList = {
          navigateRoute = it
        },
        viewModel = ListsViewModel(
          fetchUserListsUseCase = fetchUserListsUseCase.mock,
        ),
      )
    }

    with(composeTestRule) {
      onNodeWithTag(TestTags.Lists.SCROLLABLE_CONTENT).assertIsDisplayed()
      onNodeWithText("Elsolist 2").assertIsDisplayed().performClick()
    }

    assertThat(navigateRoute).isEqualTo(
      ListDetailsRoute(
        id = 8452378,
        name = "Elsolist 2",
      ),
    )
  }
}
