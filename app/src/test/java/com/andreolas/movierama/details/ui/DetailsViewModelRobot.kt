package com.andreolas.movierama.details.ui

import androidx.lifecycle.SavedStateHandle
import com.andreolas.movierama.MainDispatcherRule
import com.andreolas.movierama.details.domain.model.MovieDetailsResult
import com.andreolas.movierama.fakes.usecase.FakeGetMoviesDetailsUseCase
import com.andreolas.movierama.fakes.usecase.FakeMarkAsFavoriteUseCase
import com.andreolas.movierama.home.domain.model.MediaType
import com.google.common.truth.Truth.assertThat
import gr.divinelink.core.util.domain.Result
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import org.junit.Rule

@OptIn(ExperimentalCoroutinesApi::class)
class DetailsViewModelRobot {

  private lateinit var viewModel: DetailsViewModel

  @get:Rule
  val mainDispatcherRule = MainDispatcherRule()

  private val fakeMarkAsFavoriteUseCase = FakeMarkAsFavoriteUseCase()
  private val fakeGetMovieDetailsUseCase = FakeGetMoviesDetailsUseCase()

  fun buildViewModel(
    id: Int,
    mediaType: MediaType,
  ) = apply {
    viewModel = DetailsViewModel(
      onMarkAsFavoriteUseCase = fakeMarkAsFavoriteUseCase.mock,
      getMovieDetailsUseCase = fakeGetMovieDetailsUseCase.mock,
      savedStateHandle = SavedStateHandle(
        mapOf(
          "id" to id,
          "mediaType" to mediaType.value,
          "isFavorite" to false,
        )
      ),
    )
  }

  fun assertViewState(
    expectedViewState: DetailsViewState,
  ) = apply {
    assertThat(viewModel.viewState.value).isEqualTo(expectedViewState)
  }

  fun mockFetchMovieDetails(
    response: Flow<Result<MovieDetailsResult>>,
  ) = apply {
    fakeGetMovieDetailsUseCase.mockFetchMovieDetails(
      response = response,
    )
  }

  fun onMarkAsFavorite() = apply {
    viewModel.onMarkAsFavorite()
  }

  suspend fun mockMarkAsFavoriteUseCase(
    response: Result<Unit>,
  ) = apply {
    fakeMarkAsFavoriteUseCase.mockMarkAsFavoriteResult(
      response
    )
  }
}
