package com.divinelink.feature.profile

import com.divinelink.core.fixtures.model.account.TMDBAccountFactory
import com.divinelink.core.model.exception.AppException
import com.divinelink.core.testing.MainDispatcherRule
import com.divinelink.feature.profile.ui.TMDBAccountUiState
import kotlinx.coroutines.flow.flowOf
import org.junit.Rule
import com.divinelink.feature.profile.ProfileUiState
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
        createState(
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
        createState(
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
        createState(
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
        createState(
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
        createState(
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
        createState(
          accountUiState = TMDBAccountUiState.Error,
        ),
      )
  }

  @Test
  fun `test jellyseerr feature enabled`() {
    robot
      .mockFetchAccountDetails(
        flowOf(
          Result.success(TMDBAccountFactory.anonymous()),
        ),
      )
      .mockJellyseerrEnabled(true)
      .buildViewModel()
      .assertUiState(
        createState(
          accountUiState = TMDBAccountUiState.Anonymous,
          isJellyseerrEnabled = true,
        ),
      )
  }

  private fun createState(
    accountUiState: TMDBAccountUiState = TMDBAccountUiState.Initial,
    isJellyseerrEnabled: Boolean = false,
  ) = ProfileUiState(
    accountUiState = accountUiState,
    isJellyseerrEnabled = isJellyseerrEnabled,
  )
}
