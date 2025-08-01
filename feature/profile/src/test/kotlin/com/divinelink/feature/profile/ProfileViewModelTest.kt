package com.divinelink.feature.profile

import com.divinelink.core.fixtures.model.account.TMDBAccountFactory
import com.divinelink.core.model.exception.AppException
import com.divinelink.core.testing.MainDispatcherRule
import com.divinelink.feature.profile.ui.TMDBAccountUiState
import kotlinx.coroutines.flow.flowOf
import org.junit.Rule
import kotlin.test.Test

class ProfileViewModelTest {

  private val robot = ProfileViewModelTestRobot()

  @get:Rule
  val mainDispatcherRule = MainDispatcherRule()

  @Test
  fun `test fetch account details when logged in`() {
    robot
      .mockFetchAccountDetails(flowOf(Result.success(TMDBAccountFactory.loggedIn())))
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
      .mockFetchAccountDetails(flowOf(Result.success(TMDBAccountFactory.anonymous())))
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
      .mockFetchAccountDetails(flowOf(Result.failure(Exception("Network Error"))))
      .buildViewModel()
      .assertUiState(
        ProfileUiState(
          accountUiState = TMDBAccountUiState.Error,
        ),
      )
  }

  @Test
  fun `test fetch account details with offline exception and logged in`() {
    robot
      .mockFetchAccountDetails(
        flowOf(
          Result.success(TMDBAccountFactory.loggedIn()),
          Result.failure(AppException.Offline()),
        ),
      )
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
  fun `test fetch account details with offline exception and not logged in`() {
    robot
      .mockFetchAccountDetails(
        flowOf(
          Result.success(TMDBAccountFactory.anonymous()),
          Result.failure(AppException.Offline()),
        ),
      )
      .buildViewModel()
      .assertUiState(
        ProfileUiState(
          accountUiState = TMDBAccountUiState.Anonymous,
        ),
      )
  }

  @Test
  fun `test fetch account details with generic error and logged in`() {
    robot
      .mockFetchAccountDetails(
        flowOf(
          Result.success(TMDBAccountFactory.loggedIn()),
          Result.failure(AppException.Unknown()),
        ),
      )
      .buildViewModel()
      .assertUiState(
        ProfileUiState(
          accountUiState = TMDBAccountUiState.Error,
        ),
      )
  }
}
