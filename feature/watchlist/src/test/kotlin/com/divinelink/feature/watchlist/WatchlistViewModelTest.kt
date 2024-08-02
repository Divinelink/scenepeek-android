package com.divinelink.feature.watchlist

import com.divinelink.core.data.session.model.SessionException
import com.divinelink.core.model.media.MediaItem
import com.divinelink.core.model.media.MediaType
import com.divinelink.core.testing.MainDispatcherRule
import com.divinelink.core.testing.assertUiState
import com.divinelink.core.testing.expectUiStates
import com.divinelink.core.testing.factories.model.watchlist.WatchlistResponseFactory
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import kotlin.test.Test

class WatchlistViewModelTest {

  private val testRobot = WatchlistViewModelTestRobot()

  @get:Rule
  val mainDispatcherRule = MainDispatcherRule()

  @Test
  fun `given user is unauthenticated when view model is created then show unauthenticated error`() =
    runTest {
      testRobot
        .mockObserveSession {
          mockFailure(SessionException.Unauthenticated())
        }
        .buildViewModel()
        .assertUiState(
          expectedUiState = createUiState(
            forms = mapOf(
              MediaType.MOVIE to WatchlistForm.Error.Unauthenticated,
              MediaType.TV to WatchlistForm.Error.Unauthenticated,
            ),
          ),
        )
    }

  @Test
  fun `given generic error when view model is created then show generic error`() = runTest {
    testRobot
      .mockObserveSession {
        mockFailure(Exception())
      }
      .buildViewModel()
      .assertUiState(
        expectedUiState = createUiState(
          forms = mapOf(
            MediaType.MOVIE to WatchlistForm.Error.Unknown,
            MediaType.TV to WatchlistForm.Error.Unknown,
          ),
        ),
      )
  }

  @Test
  fun `given user is authenticated when view model is created then fetch watchlist`() = runTest {
    testRobot
      .mockObserveSession {
        mockSuccess(Result.success(true))
      }
      .mockFetchWatchlist {
        mockSuccess(
          flowOf(
            Result.success(WatchlistResponseFactory.movies()),
            Result.success(WatchlistResponseFactory.tv()),
          ),
        )
      }
      .buildViewModel()
      .assertUiState(
        expectedUiState = createUiState(
          forms = mapOf(
            MediaType.MOVIE to WatchlistForm.Data(
              mediaType = MediaType.MOVIE,
              data = (1..2).map {
                WatchlistResponseFactory.movies()
              }.flatMap { it.data },
              totalResults = WatchlistResponseFactory.movies().totalResults,
            ),
            MediaType.TV to WatchlistForm.Data(
              mediaType = MediaType.TV,
              data = (1..2).map {
                WatchlistResponseFactory.tv()
              }.flatMap { it.data },
              totalResults = WatchlistResponseFactory.tv().totalResults,
            ),
          ),
          pages = mapOf(
            MediaType.MOVIE to 3,
            MediaType.TV to 3,
          ),
        ),
      )
  }

  @Test
  fun `test onSelectTab along with load more`() = runTest {
    val dataState = createUiState(
      forms = mapOf(
        MediaType.MOVIE to WatchlistForm.Data(
          mediaType = MediaType.MOVIE,
          data = WatchlistResponseFactory.movies().data + WatchlistResponseFactory.movies().data,
          totalResults = WatchlistResponseFactory.movies().totalResults,
        ),
        MediaType.TV to WatchlistForm.Data(
          mediaType = MediaType.TV,
          data = WatchlistResponseFactory.tv().data + WatchlistResponseFactory.tv().data,
          totalResults = WatchlistResponseFactory.tv().totalResults,
        ),
      ),
      pages = mapOf(
        MediaType.MOVIE to 3,
        MediaType.TV to 3,
      ),
    )

    val updatedIndexState = dataState.copy(selectedTabIndex = 1)

    val updatedTvPageState = updatedIndexState.copy(
      forms = mapOf(
        MediaType.MOVIE to WatchlistForm.Data(
          mediaType = MediaType.MOVIE,
          data = (1..2).map {
            WatchlistResponseFactory.movies()
          }.flatMap { it.data },
          totalResults = WatchlistResponseFactory.movies().totalResults,
        ),
        MediaType.TV to WatchlistForm.Data(
          mediaType = MediaType.TV,
          data = (1..3).map {
            WatchlistResponseFactory.tv()
          }.flatMap { it.data },
          totalResults = WatchlistResponseFactory.tv().totalResults,
        ),
      ),
      pages = mapOf(
        MediaType.MOVIE to 3,
        MediaType.TV to 4,
      ),
    )

    val expectedUiStates = listOf(
      dataState,
      updatedIndexState,
      updatedTvPageState,
    )

    testRobot
      .mockObserveSession {
        mockSuccess(Result.success(true))
      }
      .mockFetchWatchlist {
        mockSuccess(
          flowOf(
            Result.success(WatchlistResponseFactory.movies()),
            Result.success(WatchlistResponseFactory.tv()),
          ),
        )
      }
      .buildViewModel()
      .expectUiStates(
        action = {
          selectTab(1)
          mockFetchWatchlist {
            mockSuccess(Result.success(WatchlistResponseFactory.tv()))
          }
          onLoadMore()
        },
        uiStates = expectedUiStates,
      )
  }

  @Test
  fun `test fetchWatchlist with generic error`() = runTest {
    testRobot
      .mockObserveSession {
        mockSuccess(Result.success(true))
      }
      .buildViewModel()
      .assertUiState(
        expectedUiState = createUiState(
          forms = mapOf(
            MediaType.MOVIE to WatchlistForm.Error.Unknown,
            MediaType.TV to WatchlistForm.Error.Unknown,
          ),
        ),
      )
  }

  @Test
  fun `test onLoadMore`() = runTest {
    testRobot
      .mockObserveSession { mockSuccess(Result.success(true)) }
      .mockFetchWatchlist { mockSuccess(Result.success(WatchlistResponseFactory.movies())) }
      .buildViewModel()
      .expectUiStates(
        action = {
          mockFetchWatchlist {
            mockSuccess(
              Result.success(
                WatchlistResponseFactory.movies(
                  page = 2,
                  canFetchMore = false,
                ),
              ),
            )
          }
          onLoadMore()
        },
        uiStates = listOf(
          createUiState(
            forms = mapOf(
              MediaType.MOVIE to WatchlistForm.Data(
                mediaType = MediaType.MOVIE,
                data = WatchlistResponseFactory.movies().data +
                  WatchlistResponseFactory.movies().data,
                totalResults = WatchlistResponseFactory.movies().totalResults,
              ),
              MediaType.TV to WatchlistForm.Loading,
            ),
            pages = mapOf(
              MediaType.MOVIE to 3,
              MediaType.TV to 1,
            ),
          ),
          createUiState(
            forms = mapOf(
              MediaType.MOVIE to WatchlistForm.Data(
                mediaType = MediaType.MOVIE,
                data = WatchlistResponseFactory.movies().data +
                  WatchlistResponseFactory.movies().data +
                  WatchlistResponseFactory.movies(2).data,
                totalResults = WatchlistResponseFactory.movies().totalResults,
              ),
              MediaType.TV to WatchlistForm.Loading,
            ),
            pages = mapOf(
              MediaType.MOVIE to 4,
              MediaType.TV to 1,
            ),
            canFetchMore = mapOf(
              MediaType.MOVIE to false,
              MediaType.TV to true,
            ),
          ),
        ),
      )
  }

  private fun createUiState(
    selectedTabIndex: Int = 0,
    tabs: List<WatchlistTab> = listOf(WatchlistTab.MOVIE, WatchlistTab.TV),
    pages: Map<MediaType, Int> = mapOf(
      MediaType.MOVIE to 1,
      MediaType.TV to 1,
    ),
    forms: Map<MediaType, WatchlistForm<MediaItem.Media>> = mapOf(
      MediaType.MOVIE to WatchlistForm.Loading,
      MediaType.TV to WatchlistForm.Loading,
    ),
    canFetchMore: Map<MediaType, Boolean> = mapOf(
      MediaType.MOVIE to true,
      MediaType.TV to true,
    ),
  ): WatchlistUiState = WatchlistUiState(
    selectedTabIndex = selectedTabIndex,
    tabs = tabs,
    pages = pages,
    forms = forms,
    canFetchMore = canFetchMore,
  )
}
