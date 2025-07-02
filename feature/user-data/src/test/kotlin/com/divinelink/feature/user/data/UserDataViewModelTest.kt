package com.divinelink.feature.user.data

import com.divinelink.core.model.exception.SessionException
import com.divinelink.core.model.media.MediaItem
import com.divinelink.core.model.media.MediaType
import com.divinelink.core.model.user.data.UserDataSection
import com.divinelink.core.testing.MainDispatcherRule
import com.divinelink.core.testing.assertUiState
import com.divinelink.core.testing.expectUiStates
import com.divinelink.core.testing.factories.model.data.UserDataResponseFactory
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import kotlin.test.Test

class UserDataViewModelTest {

  private val testRobot = UserDataViewModelTestRobot()

  @get:Rule
  val mainDispatcherRule = MainDispatcherRule()

  @Test
  fun `given user is unauthenticated when view model is created then show unauthenticated error`() =
    runTest {
      testRobot
        .mockObserveAccount {
          mockResponse(Result.failure(SessionException.Unauthenticated()))
        }
        .withSection(UserDataSection.Watchlist)
        .buildViewModel()
        .assertUiState(
          expectedUiState = createUiState(
            section = UserDataSection.Watchlist,
            forms = mapOf(
              MediaType.MOVIE to UserDataForm.Error.Unauthenticated,
              MediaType.TV to UserDataForm.Error.Unauthenticated,
            ),
          ),
        )
    }

  @Test
  fun `given generic error when view model is created then show generic error`() = runTest {
    testRobot
      .mockObserveAccount {
        mockResponse(Result.failure(Exception()))
      }
      .withSection(UserDataSection.Watchlist)
      .buildViewModel()
      .assertUiState(
        expectedUiState = createUiState(
          section = UserDataSection.Watchlist,
          forms = mapOf(
            MediaType.MOVIE to UserDataForm.Error.Unknown,
            MediaType.TV to UserDataForm.Error.Unknown,
          ),
        ),
      )
  }

  @Test
  fun `given user is authenticated when view model is created then fetch watchlist`() = runTest {
    testRobot
      .mockObserveAccount {
        mockResponse(Result.success(true))
      }
      .mockFetchUserData {
        mockSuccess(
          flowOf(
            Result.success(UserDataResponseFactory.movies()),
            Result.success(UserDataResponseFactory.tv()),
          ),
        )
      }
      .withSection(UserDataSection.Watchlist)
      .buildViewModel()
      .assertUiState(
        expectedUiState = createUiState(
          section = UserDataSection.Watchlist,
          forms = mapOf(
            MediaType.MOVIE to UserDataForm.Data(
              mediaType = MediaType.MOVIE,
              data = (1..2).map {
                UserDataResponseFactory.movies()
              }.flatMap { it.data },
              totalResults = UserDataResponseFactory.movies().totalResults,
            ),
            MediaType.TV to UserDataForm.Data(
              mediaType = MediaType.TV,
              data = (1..2).map {
                UserDataResponseFactory.tv()
              }.flatMap { it.data },
              totalResults = UserDataResponseFactory.tv().totalResults,
            ),
          ),
          pages = mapOf(
            MediaType.MOVIE to 3,
            MediaType.TV to 3,
          ),
          tabs = mapOf(
            MediaTab.MOVIE to UserDataResponseFactory.movies().totalResults,
            MediaTab.TV to UserDataResponseFactory.tv().totalResults,
          ),
        ),
      )
  }

  @Test
  fun `test fetch rated movies with success`() = runTest {
    testRobot
      .mockObserveAccount {
        mockResponse(Result.success(true))
      }
      .mockFetchUserData {
        mockSuccess(
          flowOf(
            Result.success(UserDataResponseFactory.movies()),
            Result.success(UserDataResponseFactory.tv()),
          ),
        )
      }
      .withSection(UserDataSection.Ratings)
      .buildViewModel()
      .assertUiState(
        expectedUiState = createUiState(
          section = UserDataSection.Ratings,
          forms = mapOf(
            MediaType.MOVIE to UserDataForm.Data(
              mediaType = MediaType.MOVIE,
              data = (1..2).map {
                UserDataResponseFactory.movies()
              }.flatMap { it.data },
              totalResults = UserDataResponseFactory.movies().totalResults,
            ),
            MediaType.TV to UserDataForm.Data(
              mediaType = MediaType.TV,
              data = (1..2).map {
                UserDataResponseFactory.tv()
              }.flatMap { it.data },
              totalResults = UserDataResponseFactory.tv().totalResults,
            ),
          ),
          pages = mapOf(
            MediaType.MOVIE to 3,
            MediaType.TV to 3,
          ),
          tabs = mapOf(
            MediaTab.MOVIE to UserDataResponseFactory.movies().totalResults,
            MediaTab.TV to UserDataResponseFactory.tv().totalResults,
          ),
        ),
      )
  }

  @Test
  fun `test onSelectTab along with load more`() = runTest {
    val dataState = createUiState(
      section = UserDataSection.Watchlist,
      forms = mapOf(
        MediaType.MOVIE to UserDataForm.Data(
          mediaType = MediaType.MOVIE,
          data = UserDataResponseFactory.movies().data + UserDataResponseFactory.movies().data,
          totalResults = UserDataResponseFactory.movies().totalResults,
        ),
        MediaType.TV to UserDataForm.Data(
          mediaType = MediaType.TV,
          data = UserDataResponseFactory.tv().data + UserDataResponseFactory.tv().data,
          totalResults = UserDataResponseFactory.tv().totalResults,
        ),
      ),
      pages = mapOf(
        MediaType.MOVIE to 3,
        MediaType.TV to 3,
      ),
      tabs = mapOf(
        MediaTab.MOVIE to 30,
        MediaTab.TV to 30,
      ),
    )

    val updatedIndexState = dataState.copy(selectedTabIndex = 1)

    val updatedTvPageState = updatedIndexState.copy(
      forms = mapOf(
        MediaType.MOVIE to UserDataForm.Data(
          mediaType = MediaType.MOVIE,
          data = (1..2).map {
            UserDataResponseFactory.movies()
          }.flatMap { it.data },
          totalResults = UserDataResponseFactory.movies().totalResults,
        ),
        MediaType.TV to UserDataForm.Data(
          mediaType = MediaType.TV,
          data = (1..3).map {
            UserDataResponseFactory.tv()
          }.flatMap { it.data },
          totalResults = UserDataResponseFactory.tv().totalResults,
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
      .mockObserveAccount {
        mockResponse(Result.success(true))
      }
      .mockFetchUserData {
        mockSuccess(
          flowOf(
            Result.success(UserDataResponseFactory.movies()),
            Result.success(UserDataResponseFactory.tv()),
          ),
        )
      }
      .withSection(UserDataSection.Watchlist)
      .buildViewModel()
      .expectUiStates(
        action = {
          selectTab(1)
          mockFetchUserData {
            mockSuccess(Result.success(UserDataResponseFactory.tv()))
          }
          onLoadMore()
        },
        uiStates = expectedUiStates,
      )
  }

  @Test
  fun `test fetchWatchlist with generic error`() = runTest {
    testRobot
      .mockObserveAccount {
        mockResponse(Result.success(true))
      }
      .withSection(UserDataSection.Watchlist)
      .buildViewModel()
      .assertUiState(
        expectedUiState = createUiState(
          section = UserDataSection.Watchlist,
          forms = mapOf(
            MediaType.MOVIE to UserDataForm.Error.Unknown,
            MediaType.TV to UserDataForm.Error.Unknown,
          ),
        ),
      )
  }

  @Test
  fun `test onLoadMore`() = runTest {
    testRobot
      .mockObserveAccount { mockResponse(Result.success(true)) }
      .mockFetchUserData { mockSuccess(Result.success(UserDataResponseFactory.movies())) }
      .withSection(UserDataSection.Watchlist)
      .buildViewModel()
      .expectUiStates(
        action = {
          mockFetchUserData {
            mockSuccess(
              Result.success(
                UserDataResponseFactory.movies(
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
            section = UserDataSection.Watchlist,
            forms = mapOf(
              MediaType.MOVIE to UserDataForm.Data(
                mediaType = MediaType.MOVIE,
                data = UserDataResponseFactory.movies().data +
                  UserDataResponseFactory.movies().data,
                totalResults = UserDataResponseFactory.movies().totalResults,
              ),
              MediaType.TV to UserDataForm.Loading,
            ),
            pages = mapOf(
              MediaType.MOVIE to 3,
              MediaType.TV to 1,
            ),
            tabs = mapOf(
              MediaTab.MOVIE to UserDataResponseFactory.movies().totalResults,
              MediaTab.TV to null,
            ),
          ),
          createUiState(
            section = UserDataSection.Watchlist,
            forms = mapOf(
              MediaType.MOVIE to UserDataForm.Data(
                mediaType = MediaType.MOVIE,
                data = UserDataResponseFactory.movies().data +
                  UserDataResponseFactory.movies().data +
                  UserDataResponseFactory.movies(2).data,
                totalResults = UserDataResponseFactory.movies().totalResults,
              ),
              MediaType.TV to UserDataForm.Loading,
            ),
            pages = mapOf(
              MediaType.MOVIE to 4,
              MediaType.TV to 1,
            ),
            canFetchMore = mapOf(
              MediaType.MOVIE to false,
              MediaType.TV to true,
            ),
            tabs = mapOf(
              MediaTab.MOVIE to UserDataResponseFactory.movies().totalResults,
              MediaTab.TV to null,
            ),
          ),
        ),
      )
  }

  @Test
  fun `test onRefresh when state is unknown error for movie`() = runTest {
    testRobot
      .mockObserveAccount {
        mockResponse(Result.failure(Exception()))
      }
      .withSection(UserDataSection.Watchlist)
      .buildViewModel()
      .assertUiState(
        expectedUiState = createUiState(
          section = UserDataSection.Watchlist,
          forms = mapOf(
            MediaType.MOVIE to UserDataForm.Error.Unknown,
            MediaType.TV to UserDataForm.Error.Unknown,
          ),
        ),
      )
      .mockFetchUserData {
        mockSuccess(
          flowOf(Result.success(UserDataResponseFactory.movies())),
        )
      }
      .onRefresh()
      .assertUiState(
        expectedUiState = createUiState(
          section = UserDataSection.Watchlist,
          forms = mapOf(
            MediaType.MOVIE to UserDataForm.Data(
              mediaType = MediaType.MOVIE,
              data = UserDataResponseFactory.movies().data,
              totalResults = UserDataResponseFactory.movies().totalResults,
            ),
            MediaType.TV to UserDataForm.Error.Unknown,
          ),
          pages = mapOf(
            MediaType.MOVIE to 2,
            MediaType.TV to 1,
          ),
          tabs = mapOf(
            MediaTab.MOVIE to 30,
            MediaTab.TV to null,
          ),
        ),
      )
  }

  @Test
  fun `test onRefresh when state is unknown error for tv`() = runTest {
    testRobot
      .mockObserveAccount {
        mockResponse(Result.failure(Exception()))
      }
      .withSection(UserDataSection.Watchlist)
      .buildViewModel()
      .assertUiState(
        expectedUiState = createUiState(
          section = UserDataSection.Watchlist,
          forms = mapOf(
            MediaType.MOVIE to UserDataForm.Error.Unknown,
            MediaType.TV to UserDataForm.Error.Unknown,
          ),
        ),
      )
      .selectTab(1)
      .mockFetchUserData {
        mockSuccess(
          flowOf(Result.success(UserDataResponseFactory.tv())),
        )
      }
      .onRefresh()
      .assertUiState(
        expectedUiState = createUiState(
          section = UserDataSection.Watchlist,
          forms = mapOf(
            MediaType.MOVIE to UserDataForm.Error.Unknown,
            MediaType.TV to UserDataForm.Data(
              mediaType = MediaType.TV,
              data = UserDataResponseFactory.tv().data,
              totalResults = UserDataResponseFactory.tv().totalResults,
            ),
          ),
          selectedTabIndex = 1,
          pages = mapOf(
            MediaType.MOVIE to 1,
            MediaType.TV to 2,
          ),
          tabs = mapOf(
            MediaTab.MOVIE to null,
            MediaTab.TV to UserDataResponseFactory.tv().totalResults,
          ),
        ),
      )
  }

  private fun createUiState(
    selectedTabIndex: Int = 0,
    tabs: Map<MediaTab, Int?> = mapOf(
      MediaTab.MOVIE to null,
      MediaTab.TV to null,
    ),
    pages: Map<MediaType, Int> = mapOf(
      MediaType.MOVIE to 1,
      MediaType.TV to 1,
    ),
    forms: Map<MediaType, UserDataForm<MediaItem.Media>> = mapOf(
      MediaType.MOVIE to UserDataForm.Loading,
      MediaType.TV to UserDataForm.Loading,
    ),
    canFetchMore: Map<MediaType, Boolean> = mapOf(
      MediaType.MOVIE to true,
      MediaType.TV to true,
    ),
    section: UserDataSection,
  ): UserDataUiState = UserDataUiState(
    selectedTabIndex = selectedTabIndex,
    tabs = tabs,
    pages = pages,
    forms = forms,
    canFetchMore = canFetchMore,
    section = section,
  )
}
