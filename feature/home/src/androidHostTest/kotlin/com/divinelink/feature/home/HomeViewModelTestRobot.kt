package com.divinelink.feature.home

import com.divinelink.core.domain.search.SearchStateManager
import com.divinelink.core.fixtures.core.commons.ClockFactory
import com.divinelink.core.model.PaginationData
import com.divinelink.core.model.home.MediaListSection
import com.divinelink.core.model.media.MediaItem
import com.divinelink.core.testing.MainDispatcherRule
import com.divinelink.core.testing.repository.TestMediaRepository
import com.divinelink.core.testing.usecase.TestMarkAsFavoriteUseCase
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import org.junit.Rule
import kotlin.time.Clock

class HomeViewModelTestRobot {

  private lateinit var viewModel: HomeViewModel

  @get:Rule
  val mainDispatcherRule = MainDispatcherRule()

  private val repository = TestMediaRepository()
  private val fakeMarkAsFavoriteUseCase = TestMarkAsFavoriteUseCase()

  fun buildViewModel(
    clock: Clock = ClockFactory.decemberFirst2021(),
  ) = apply {
    viewModel = HomeViewModel(
      repository = repository.mock,
      clock = clock,
      markAsFavoriteUseCase = fakeMarkAsFavoriteUseCase,
      searchStateManager = SearchStateManager(),
    )
  }

  fun assertUiState(expectedUiState: HomeUiState) = apply {
    assertThat(viewModel.uiState.value).isEqualTo(expectedUiState)
  }

  suspend fun mockFetchSectionData(
    response: Flow<Result<PaginationData<MediaItem>>>,
  ) = apply {
    repository.mockFetchMediaLists(
      response = response,
    )
    repository.mockFetchTrending(
      response = response,
    )
  }

  suspend fun mockFetchSectionData(
    response: Result<PaginationData<MediaItem>>,
  ) = apply {
    repository.mockFetchMediaLists(
      response = flowOf(response),
    )
    repository.mockFetchTrending(
      response = flowOf(response),
    )
  }

  fun mockMarkAsFavorite(
    mediaItem: MediaItem.Media,
    result: Result<Boolean>,
  ) = apply {
    fakeMarkAsFavoriteUseCase.mockMarkAsFavoriteResult(
      media = mediaItem,
      result = result,
    )
  }

  fun onRetrySection(section: MediaListSection) = apply {
    viewModel.onAction(HomeAction.RetrySection(section))
  }

  fun onRetryAll() = apply {
    viewModel.onAction(HomeAction.RetryAll)
  }

  fun onLoadNextPage(section: MediaListSection) = apply {
    viewModel.onAction(HomeAction.LoadMore(section))
  }

  fun onMarkAsFavorite(movie: MediaItem.Media) = apply {
    viewModel.onMarkAsFavoriteClicked(movie)
  }
}
