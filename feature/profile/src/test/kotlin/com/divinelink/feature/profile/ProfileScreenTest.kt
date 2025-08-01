package com.divinelink.feature.profile

import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollToIndex
import com.divinelink.core.fixtures.model.account.TMDBAccountFactory
import com.divinelink.core.model.user.data.UserDataSection
import com.divinelink.core.navigation.route.Navigation
import com.divinelink.core.testing.ComposeTest
import com.divinelink.core.testing.getString
import com.divinelink.core.testing.setVisibilityScopeContent
import com.divinelink.core.testing.usecase.FakeGetAccountDetailsUseCase
import com.divinelink.core.ui.R
import com.divinelink.core.ui.TestTags
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.flow.flowOf
import kotlin.test.Test

class ProfileScreenTest : ComposeTest() {

  private val getAccountDetailsUseCase = FakeGetAccountDetailsUseCase()

  private lateinit var viewModel: ProfileViewModel

  @Test
  fun `test navigate to login when logged out`() {
    var navigateToTMDBAuthCalled = false
    getAccountDetailsUseCase.mockSuccess(flowOf(Result.success(TMDBAccountFactory.anonymous())))

    viewModel = ProfileViewModel(
      getAccountDetailsUseCase = getAccountDetailsUseCase.mock,
    )

    setVisibilityScopeContent {
      ProfileScreen(
        viewModel = viewModel,
        onNavigate = {
          if (it is Navigation.TMDBAuthRoute) {
            navigateToTMDBAuthCalled = true
          }
        },
      )
    }
    with(composeTestRule) {
      onNodeWithText(getString(R.string.core_ui_login)).performClick()

      navigateToTMDBAuthCalled shouldBe true
    }
  }

  @Test
  fun `test navigate to watchlist`() {
    var userDataSection: UserDataSection? = null

    viewModel = ProfileViewModel(
      getAccountDetailsUseCase = getAccountDetailsUseCase.mock,
    )

    setVisibilityScopeContent {
      ProfileScreen(
        viewModel = viewModel,
        onNavigate = {
          if (it is Navigation.UserDataRoute) {
            userDataSection = it.userDataSection
          }
        },
      )
    }
    with(composeTestRule) {
      onNodeWithText("Watchlist").performClick()

      userDataSection shouldBe UserDataSection.Watchlist
    }
  }

  @Test
  fun `test navigate to ratings`() {
    var userDataSection: UserDataSection? = null

    viewModel = ProfileViewModel(
      getAccountDetailsUseCase = getAccountDetailsUseCase.mock,
    )

    setVisibilityScopeContent {
      ProfileScreen(
        viewModel = viewModel,
        onNavigate = {
          if (it is Navigation.UserDataRoute) {
            userDataSection = it.userDataSection
          }
        },
      )
    }
    composeTestRule.onNodeWithText("Ratings").performClick()

    userDataSection shouldBe UserDataSection.Ratings
  }

  @Test
  fun `test navigate to lists`() {
    var navigateToLists = false

    viewModel = ProfileViewModel(
      getAccountDetailsUseCase = getAccountDetailsUseCase.mock,
    )

    setVisibilityScopeContent {
      ProfileScreen(
        viewModel = viewModel,
        onNavigate = {
          if (it is Navigation.ListsRoute) {
            navigateToLists = true
          }
        },
      )
    }
    with(composeTestRule) {
      onNodeWithTag(TestTags.Profile.CONTENT).performScrollToIndex(5)
      onNodeWithText("Lists").performClick()

      navigateToLists shouldBe true
    }
  }
}
