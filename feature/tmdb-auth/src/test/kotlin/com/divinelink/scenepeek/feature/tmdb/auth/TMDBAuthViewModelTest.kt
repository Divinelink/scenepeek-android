package com.divinelink.scenepeek.feature.tmdb.auth

import app.cash.turbine.test
import com.divinelink.core.testing.MainDispatcherRule
import com.divinelink.core.testing.usecase.session.FakeCreateRequestTokenUseCase
import com.divinelink.feature.tmdb.auth.TMDBAuthViewModel
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import kotlin.test.Test

class TMDBAuthViewModelTest {

  @get:Rule
  val mainDispatcherRule = MainDispatcherRule()

  private val createRequestTokenUseCase = FakeCreateRequestTokenUseCase()

  @Test
  fun `test initialise viewModel with success request token emits openUrlTab`() = runTest {
    createRequestTokenUseCase.mockSuccess(Result.success("requestToken"))

    val viewModel = TMDBAuthViewModel(createRequestTokenUseCase.mock)

    viewModel.openUrlTab.test {
      assertThat(awaitItem()).isEqualTo(
        "https://www.themoviedb.org/auth/access?request_token=requestToken",
      )
    }
  }

  @Test
  fun `test initialise viewModel with failure request token does not emit openUrlTab`() = runTest {
    createRequestTokenUseCase.mockFailure()

    val viewModel = TMDBAuthViewModel(createRequestTokenUseCase.mock)

    viewModel.openUrlTab.test {
      expectNoEvents()
    }
  }

  @Test
  fun `test handleCloseWeb emits onNavigateUp`() = runTest {
    createRequestTokenUseCase.mockSuccess(Result.success("requestToken"))

    val viewModel = TMDBAuthViewModel(createRequestTokenUseCase.mock)

    viewModel.onNavigateUp.test {
      viewModel.handleCloseWeb()

      assertThat(awaitItem()).isEqualTo(Unit)
    }
  }
}
