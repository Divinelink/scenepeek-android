package com.divinelink.feature.profile

import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollToIndex
import androidx.compose.ui.test.performScrollToNode
import com.divinelink.core.fixtures.model.account.TMDBAccountFactory
import com.divinelink.core.model.user.data.UserDataSection
import com.divinelink.core.navigation.route.Navigation
import com.divinelink.core.testing.ComposeTest
import com.divinelink.core.testing.repository.TestAuthRepository
import com.divinelink.core.testing.setVisibilityScopeContent
import com.divinelink.core.testing.uiTest
import com.divinelink.core.testing.usecase.FakeGetAccountDetailsUseCase
import com.divinelink.core.ui.TestTags
import com.divinelink.core.ui.UiString
import com.divinelink.core.ui.resources.core_ui_login
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.flow.flowOf
import org.jetbrains.compose.resources.getString
import kotlin.test.Test

class ProfileScreenTest : ComposeTest() {

  private val getAccountDetailsUseCase = FakeGetAccountDetailsUseCase()
  private val authRepository = TestAuthRepository()

  private lateinit var viewModel: ProfileViewModel

  @Test
  fun `test navigate to login when logged out`() = uiTest {
    var navigateToTMDBAuthCalled = false
    getAccountDetailsUseCase.mockSuccess(flowOf(Result.success(TMDBAccountFactory.anonymous())))
    authRepository.mockJellyseerrEnabled(false)
    viewModel = ProfileViewModel(
      getAccountDetailsUseCase = getAccountDetailsUseCase.mock,
      authRepository = authRepository.mock,
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
    onNodeWithText(getString(UiString.core_ui_login)).performClick()

    navigateToTMDBAuthCalled shouldBe true
  }

  @Test
  fun `test navigate to watchlist`() = uiTest {
    var userDataSection: UserDataSection? = null
    authRepository.mockJellyseerrEnabled(false)
    viewModel = ProfileViewModel(
      getAccountDetailsUseCase = getAccountDetailsUseCase.mock,
      authRepository = authRepository.mock,
    )

    setVisibilityScopeContent {
      ProfileScreen(
        viewModel = viewModel,
        onNavigate = {
          if (it is Navigation.UserDataRoute) {
            userDataSection = UserDataSection.from(it.section)
          }
        },
      )
    }
    onNodeWithText("Watchlist").performClick()

    userDataSection shouldBe UserDataSection.Watchlist
  }

  @Test
  fun `test navigate to ratings`() = uiTest {
    var userDataSection: UserDataSection? = null
    authRepository.mockJellyseerrEnabled(false)
    viewModel = ProfileViewModel(
      getAccountDetailsUseCase = getAccountDetailsUseCase.mock,
      authRepository = authRepository.mock,
    )

    setVisibilityScopeContent {
      ProfileScreen(
        viewModel = viewModel,
        onNavigate = {
          if (it is Navigation.UserDataRoute) {
            userDataSection = UserDataSection.from(it.section)
          }
        },
      )
    }
    onNodeWithText("Ratings").performClick()

    userDataSection shouldBe UserDataSection.Ratings
  }

  @Test
  fun `test navigate to lists`() = uiTest {
    var navigateToLists = false
    authRepository.mockJellyseerrEnabled(false)

    viewModel = ProfileViewModel(
      getAccountDetailsUseCase = getAccountDetailsUseCase.mock,
      authRepository = authRepository.mock,
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
    onNodeWithTag(TestTags.Profile.CONTENT).performScrollToIndex(5)
    onNodeWithText("Lists").performClick()

    navigateToLists shouldBe true
  }

  @Test
  fun `test navigate jellyseerr requests`() = uiTest {
    var route: Navigation? = null
    authRepository.mockJellyseerrEnabled(true)

    viewModel = ProfileViewModel(
      getAccountDetailsUseCase = getAccountDetailsUseCase.mock,
      authRepository = authRepository.mock,
    )

    setVisibilityScopeContent {
      ProfileScreen(
        viewModel = viewModel,
        onNavigate = {
          route = it
        },
      )
    }
    onNodeWithTag(TestTags.Profile.CONTENT).performScrollToNode(
      hasText("Requests"),
    )
    onNodeWithText("Requests").performClick()

    route shouldBe Navigation.JellyseerrRequestsRoute
  }
}
