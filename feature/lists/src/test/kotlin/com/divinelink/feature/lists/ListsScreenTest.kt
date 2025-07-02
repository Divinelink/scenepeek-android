package com.divinelink.feature.lists

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import com.divinelink.core.fixtures.model.list.ListItemFactory
import com.divinelink.core.testing.ComposeTest
import com.divinelink.core.testing.setVisibilityScopeContent
import com.divinelink.core.testing.usecase.TestFetchUserListsUseCase
import com.divinelink.core.testing.usecase.TestObserveAccountUseCase
import com.divinelink.core.ui.TestTags
import com.divinelink.feature.lists.ui.ListsScreen
import kotlin.test.Test

class ListsScreenTest : ComposeTest() {

  private val observeAccountUseCase = TestObserveAccountUseCase()
  private val fetchUserListsUseCase = TestFetchUserListsUseCase()

  @Test
  fun `test unauthenticated shows fab`() {
    observeAccountUseCase.mockResponse(Result.failure(Exception()))

    setVisibilityScopeContent {
      ListsScreen(
        onNavigateToTMDBLogin = {},
        onNavigateUp = {},
        viewModel = ListsViewModel(
          observeAccountUseCase = observeAccountUseCase.mock,
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
    observeAccountUseCase.mockResponse(Result.success(true))
    fetchUserListsUseCase.mockResponse(Result.success(ListItemFactory.page1()))

    setVisibilityScopeContent {
      ListsScreen(
        onNavigateToTMDBLogin = {},
        onNavigateUp = {},
        viewModel = ListsViewModel(
          observeAccountUseCase = observeAccountUseCase.mock,
          fetchUserListsUseCase = fetchUserListsUseCase.mock,
        ),
      )
    }

    with(composeTestRule) {
      onNodeWithTag(TestTags.Lists.SCROLLABLE_CONTENT).assertIsDisplayed()
      onNodeWithText("Elsolist 2").assertIsDisplayed()
    }
  }
}
