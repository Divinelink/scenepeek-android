package com.divinelink.feature.profile

import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.divinelink.core.fixtures.model.account.TMDBAccountFactory
import com.divinelink.core.testing.ComposeTest
import com.divinelink.core.testing.getString
import com.divinelink.core.testing.setVisibilityScopeContent
import com.divinelink.core.testing.usecase.FakeGetAccountDetailsUseCase
import com.divinelink.core.ui.R
import kotlin.test.Test
import kotlin.test.assertTrue

class ProfileScreenTest : ComposeTest() {

  private val getAccountDetailsUseCase = FakeGetAccountDetailsUseCase()

  private lateinit var viewModel: ProfileViewModel

  @Test
  fun `test navigate to login when logged out`() {
    var navigateToTMDBAuthCalled = false
    getAccountDetailsUseCase.mockSuccess(Result.success(TMDBAccountFactory.anonymous()))

    viewModel = ProfileViewModel(
      getAccountDetailsUseCase = getAccountDetailsUseCase.mock,
    )

    setVisibilityScopeContent {
      ProfileScreen(
        viewModel = viewModel,
        onNavigateToUserData = {},
        onNavigateToLists = {},
        onNavigateToTMDBAuth = {
          navigateToTMDBAuthCalled = true
        },
      )
    }
    with(composeTestRule) {
      onNodeWithText(getString(R.string.core_ui_login)).performClick()

      assertTrue { navigateToTMDBAuthCalled }
    }
  }

  @Test
  fun `test navigate to watchlist`() {
    var navigateToWatchlistCalled = false

    viewModel = ProfileViewModel(
      getAccountDetailsUseCase = getAccountDetailsUseCase.mock,
    )

    setVisibilityScopeContent {
      ProfileScreen(
        viewModel = viewModel,
        onNavigateToUserData = {
          navigateToWatchlistCalled = true
        },
        onNavigateToLists = {},
        onNavigateToTMDBAuth = { },
      )
    }
    with(composeTestRule) {
      onNodeWithText("Watchlist").performClick()

      assertTrue { navigateToWatchlistCalled }
    }
  }
}
