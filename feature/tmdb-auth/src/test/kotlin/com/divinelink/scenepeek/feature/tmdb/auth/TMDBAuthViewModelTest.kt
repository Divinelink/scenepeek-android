package com.divinelink.scenepeek.feature.tmdb.auth

import app.cash.turbine.test
import com.divinelink.core.testing.MainDispatcherRule
import com.divinelink.core.testing.usecase.TestCreateSessionUseCase
import com.divinelink.core.testing.usecase.session.FakeCreateRequestTokenUseCase
import com.divinelink.feature.tmdb.auth.TMDBAuthUiState
import com.divinelink.feature.tmdb.auth.TMDBAuthViewModel
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import kotlin.test.Test

class TMDBAuthViewModelTest {

  @get:Rule
  val mainDispatcherRule = MainDispatcherRule()

  private val createRequestTokenUseCase = FakeCreateRequestTokenUseCase()
  private val createSessionUseCase = TestCreateSessionUseCase()

  @Test
  fun `test initialise viewModel with success request token emits openUrlTab`() = runTest {
    createRequestTokenUseCase.mockSuccess(Result.success("requestToken"))

    val viewModel = TMDBAuthViewModel(
      createRequestTokenUseCase = createRequestTokenUseCase.mock,
      createSessionUseCase = createSessionUseCase.mock,
    )

    viewModel.openUrlTab.test {
      assertThat(awaitItem()).isEqualTo(
        "https://www.themoviedb.org/auth/access?request_token=requestToken",
      )
    }
  }

  @Test
  fun `test initialise viewModel with failure request token does not emit openUrlTab`() = runTest {
    createRequestTokenUseCase.mockFailure()

    val viewModel = TMDBAuthViewModel(
      createRequestTokenUseCase = createRequestTokenUseCase.mock,
      createSessionUseCase = createSessionUseCase.mock,
    )

    viewModel.openUrlTab.test {
      expectNoEvents()
    }
  }

  @Test
  fun `test handleCloseWeb emits onNavigateUp`() = runTest {
    createRequestTokenUseCase.mockSuccess(Result.success("requestToken"))

    val viewModel = TMDBAuthViewModel(
      createRequestTokenUseCase = createRequestTokenUseCase.mock,
      createSessionUseCase = createSessionUseCase.mock,
    )

    viewModel.onNavigateUp.test {
      viewModel.handleCloseWeb()

      assertThat(awaitItem()).isEqualTo(Unit)
    }
  }

  @Test
  fun `test setWebViewFallback sets webViewFallback to true`() = runTest {
    val viewModel = TMDBAuthViewModel(
      createRequestTokenUseCase = createRequestTokenUseCase.mock,
      createSessionUseCase = createSessionUseCase.mock,
    )

    viewModel.uiState.test {
      assertThat(awaitItem()).isEqualTo(TMDBAuthUiState.initial)

      viewModel.setWebViewFallback()

      assertThat(awaitItem()).isEqualTo(
        TMDBAuthUiState.initial.copy(
          webViewFallback = true,
        ),
      )
    }
  }

  @Test
  fun `test createSession with success navigates up`() = runTest {
    createSessionUseCase.mockResponse(Result.success(Unit))
    val viewModel = TMDBAuthViewModel(
      createRequestTokenUseCase = createRequestTokenUseCase.mock,
      createSessionUseCase = createSessionUseCase.mock,
    )

    viewModel.onNavigateUp.test {
      viewModel.createSession()

      assertThat(awaitItem()).isEqualTo(Unit)
    }
  }

  @Test
  fun `test createSession with failure navigates up`() = runTest {
    createSessionUseCase.mockResponse(Result.failure(Exception()))
    val viewModel = TMDBAuthViewModel(
      createRequestTokenUseCase = createRequestTokenUseCase.mock,
      createSessionUseCase = createSessionUseCase.mock,
    )

    viewModel.onNavigateUp.test {
      viewModel.createSession()

      assertThat(awaitItem()).isEqualTo(Unit)
    }
  }
}
