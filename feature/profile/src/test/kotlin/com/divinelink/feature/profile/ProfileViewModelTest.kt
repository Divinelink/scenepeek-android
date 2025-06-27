package com.divinelink.feature.profile

import com.divinelink.core.fixtures.model.account.TMDBAccountFactory
import com.divinelink.core.testing.MainDispatcherRule
import com.divinelink.feature.profile.ui.TMDBAccountUiState
import org.junit.Rule
import kotlin.test.Test

class ProfileViewModelTest {

  private val robot = ProfileViewModelTestRobot()

  @get:Rule
  val mainDispatcherRule = MainDispatcherRule()

  @Test
  fun `test fetch account details when logged in`() {
    robot
      .mockFetchAccountDetails(Result.success(TMDBAccountFactory.loggedIn()))
      .buildViewModel()
      .assertUiState(
        ProfileUiState(
          accountUiState = TMDBAccountUiState.LoggedIn(
            TMDBAccountFactory.loggedIn(),
          ),
        ),
      )
  }

  @Test
  fun `test fetch account details when not logged in`() {
    robot
      .mockFetchAccountDetails(Result.success(TMDBAccountFactory.anonymous()))
      .buildViewModel()
      .assertUiState(
        ProfileUiState(
          accountUiState = TMDBAccountUiState.Anonymous,
        ),
      )
  }

  @Test
  fun `test fetch account details when error occurs`() {
    robot
      .mockFetchAccountDetails(Result.failure(Exception("Network Error")))
      .buildViewModel()
      .assertUiState(
        ProfileUiState(
          accountUiState = TMDBAccountUiState.Error,
        ),
      )
  }
}
