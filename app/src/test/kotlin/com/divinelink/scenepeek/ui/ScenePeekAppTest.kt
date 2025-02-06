package com.divinelink.scenepeek.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsNotDisplayed
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollToNode
import androidx.lifecycle.SavedStateHandle
import androidx.navigation.compose.ComposeNavigator
import androidx.navigation.compose.composable
import androidx.navigation.createGraph
import androidx.navigation.testing.TestNavHostController
import app.cash.turbine.test
import com.divinelink.core.domain.credits.SpoilersObfuscationUseCase
import com.divinelink.core.fixtures.model.details.MediaDetailsFactory
import com.divinelink.core.fixtures.model.media.MediaItemFactory
import com.divinelink.core.model.details.rating.RatingSource
import com.divinelink.core.model.media.MediaType
import com.divinelink.core.testing.ComposeTest
import com.divinelink.core.testing.MainDispatcherRule
import com.divinelink.core.testing.factories.model.watchlist.WatchlistResponseFactory
import com.divinelink.core.testing.getString
import com.divinelink.core.testing.network.TestNetworkMonitor
import com.divinelink.core.testing.setContentWithTheme
import com.divinelink.core.testing.usecase.FakeFetchWatchlistUseCase
import com.divinelink.core.testing.usecase.FakeRequestMediaUseCase
import com.divinelink.core.testing.usecase.TestFetchAllRatingsUseCase
import com.divinelink.core.testing.usecase.TestObserveAccountUseCase
import com.divinelink.core.testing.usecase.TestSpoilersObfuscationUseCase
import com.divinelink.core.ui.TestTags
import com.divinelink.feature.details.media.ui.DetailsViewModel
import com.divinelink.feature.details.media.ui.MediaDetailsResult
import com.divinelink.feature.watchlist.WatchlistViewModel
import com.divinelink.scenepeek.MainUiEvent
import com.divinelink.scenepeek.MainUiState
import com.divinelink.scenepeek.R
import com.divinelink.scenepeek.fakes.usecase.FakeFetchMultiInfoSearchUseCase
import com.divinelink.scenepeek.fakes.usecase.FakeGetFavoriteMoviesUseCase
import com.divinelink.scenepeek.fakes.usecase.FakeGetMediaDetailsUseCase
import com.divinelink.scenepeek.fakes.usecase.FakeGetPopularMoviesUseCase
import com.divinelink.scenepeek.fakes.usecase.FakeMarkAsFavoriteUseCase
import com.divinelink.scenepeek.fakes.usecase.details.FakeAddToWatchlistUseCase
import com.divinelink.scenepeek.fakes.usecase.details.FakeDeleteRatingUseCase
import com.divinelink.scenepeek.fakes.usecase.details.FakeSubmitRatingUseCase
import com.divinelink.scenepeek.home.ui.HomeViewModel
import com.divinelink.scenepeek.navigation.TopLevelDestination
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.runTest
import org.koin.android.ext.koin.androidContext
import org.koin.compose.KoinContext
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.test.mock.declare
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import com.divinelink.core.ui.R as uiR

class ScenePeekAppTest : ComposeTest() {

  private lateinit var uiState: MainUiState
  private lateinit var uiEvent: MainUiEvent

  private val networkMonitor = TestNetworkMonitor()

  private lateinit var state: ScenePeekAppState

  // Home use cases
  private lateinit var popularMoviesUseCase: FakeGetPopularMoviesUseCase
  private lateinit var fetchMultiInfoSearchUseCase: FakeFetchMultiInfoSearchUseCase
  private lateinit var markAsFavoriteUseCase: FakeMarkAsFavoriteUseCase
  private lateinit var getFavoriteMoviesUseCase: FakeGetFavoriteMoviesUseCase

  // Watchlist use cases
  private lateinit var observeAccountUseCase: TestObserveAccountUseCase
  private lateinit var fetchWatchlistUseCase: FakeFetchWatchlistUseCase

  // Details use cases
  private lateinit var getMediaDetailsUseCase: FakeGetMediaDetailsUseCase
  private lateinit var submitRatingUseCase: FakeSubmitRatingUseCase
  private lateinit var deleteRatingUseCase: FakeDeleteRatingUseCase
  private lateinit var addToWatchlistUseCase: FakeAddToWatchlistUseCase
  private lateinit var requestMediaUseCase: FakeRequestMediaUseCase
  private lateinit var spoilersObfuscationUseCase: SpoilersObfuscationUseCase
  private lateinit var fetchAllRatingsUseCase: TestFetchAllRatingsUseCase

  @BeforeTest
  fun setUp() {
    uiState = MainUiState.Completed
    uiEvent = MainUiEvent.None

    popularMoviesUseCase = FakeGetPopularMoviesUseCase()
    fetchMultiInfoSearchUseCase = FakeFetchMultiInfoSearchUseCase()
    markAsFavoriteUseCase = FakeMarkAsFavoriteUseCase()
    getFavoriteMoviesUseCase = FakeGetFavoriteMoviesUseCase()

    observeAccountUseCase = TestObserveAccountUseCase()
    fetchWatchlistUseCase = FakeFetchWatchlistUseCase()

    getMediaDetailsUseCase = FakeGetMediaDetailsUseCase()
    submitRatingUseCase = FakeSubmitRatingUseCase()
    deleteRatingUseCase = FakeDeleteRatingUseCase()
    addToWatchlistUseCase = FakeAddToWatchlistUseCase()
    requestMediaUseCase = FakeRequestMediaUseCase()
    fetchAllRatingsUseCase = TestFetchAllRatingsUseCase()
    spoilersObfuscationUseCase = TestSpoilersObfuscationUseCase().useCase()

    startKoin {
      androidContext(composeTestRule.activity)
      modules()
    }
  }

  @AfterTest
  fun tearDown() {
    stopKoin()
  }

  @Test
  fun `test navigation items are visible`() = runTest {
    val homeTab = getString(R.string.home)
    val watchlistTab = getString(R.string.watchlist)

    popularMoviesUseCase.mockFetchPopularMovies(
      response = Result.success(MediaItemFactory.MoviesList()),
    )

    declare {
      HomeViewModel(
        getPopularMoviesUseCase = popularMoviesUseCase.mock,
        fetchMultiInfoSearchUseCase = fetchMultiInfoSearchUseCase.mock,
        markAsFavoriteUseCase = markAsFavoriteUseCase,
        getFavoriteMoviesUseCase = getFavoriteMoviesUseCase.mock,
      )
    }

    setContentWithTheme {
      val state = rememberScenePeekAppState(networkMonitor)

      KoinContext {
        ScenePeekApp(
          state = state,
          uiState = uiState,
          uiEvent = uiEvent,
          onConsumeEvent = {},
        )
      }
    }

    with(composeTestRule) {
      onNodeWithText(homeTab).assertExists()
      onNodeWithText(watchlistTab).assertExists()

      onNodeWithContentDescription(
        getString(R.string.top_level_navigation_content_description_selected, homeTab),
        useUnmergedTree = true,
      ).assertExists()

      onNodeWithContentDescription(
        getString(R.string.top_level_navigation_content_description_unselected, watchlistTab),
        useUnmergedTree = true,
      ).assertExists()
    }
  }

  @Test
  fun `test navigate to watchlist`() = runTest {
    val homeTab = getString(R.string.home)
    val watchlistTab = getString(R.string.watchlist)

    popularMoviesUseCase.mockFetchPopularMovies(
      response = Result.success(MediaItemFactory.MoviesList()),
    )

    declare {
      HomeViewModel(
        getPopularMoviesUseCase = popularMoviesUseCase.mock,
        fetchMultiInfoSearchUseCase = fetchMultiInfoSearchUseCase.mock,
        markAsFavoriteUseCase = markAsFavoriteUseCase,
        getFavoriteMoviesUseCase = getFavoriteMoviesUseCase.mock,
      )
    }

    declare {
      WatchlistViewModel(
        observeAccountUseCase = observeAccountUseCase.mock,
        fetchWatchlistUseCase = fetchWatchlistUseCase.mock,
      )
    }

    setContentWithTheme {
      val state = rememberScenePeekAppState(networkMonitor)

      KoinContext {
        ScenePeekApp(
          state = state,
          uiState = uiState,
          uiEvent = uiEvent,
          onConsumeEvent = {},
        )
      }
    }

    with(composeTestRule) {
      onNodeWithText(homeTab).assertExists()
      onNodeWithText(watchlistTab).assertExists()

      onNodeWithContentDescription(
        getString(R.string.top_level_navigation_content_description_selected, homeTab),
        useUnmergedTree = true,
      ).assertExists()

      onNodeWithContentDescription(
        getString(R.string.top_level_navigation_content_description_unselected, watchlistTab),
        useUnmergedTree = true,
      ).assertExists()

      onNodeWithText(watchlistTab).performClick()

      onNodeWithContentDescription(
        getString(R.string.top_level_navigation_content_description_selected, watchlistTab),
        useUnmergedTree = true,
      ).assertExists()

      onNodeWithContentDescription(
        getString(R.string.top_level_navigation_content_description_unselected, homeTab),
        useUnmergedTree = true,
      ).assertExists()
    }
  }

  @Test
  fun `test navigate to watchlist when is on movie details`() = runTest {
    val homeTab = getString(R.string.home)
    val watchlistTab = getString(R.string.watchlist)

    popularMoviesUseCase.mockFetchPopularMovies(
      response = Result.success(MediaItemFactory.MoviesList()),
    )

    observeAccountUseCase.mockSuccess(Result.success(true))
    fetchWatchlistUseCase.mockSuccess(
      Result.success(
        WatchlistResponseFactory.movies().copy(canFetchMore = false),
      ),
    )

    getMediaDetailsUseCase.mockFetchMediaDetails(
      response = flowOf(
        Result.success(
          MediaDetailsResult.DetailsSuccess(
            mediaDetails = MediaDetailsFactory.FightClub(),
            ratingSource = RatingSource.TMDB,
          ),
        ),
      ),
    )

    declare {
      HomeViewModel(
        getPopularMoviesUseCase = popularMoviesUseCase.mock,
        fetchMultiInfoSearchUseCase = fetchMultiInfoSearchUseCase.mock,
        markAsFavoriteUseCase = markAsFavoriteUseCase,
        getFavoriteMoviesUseCase = getFavoriteMoviesUseCase.mock,
      )
    }

    declare {
      WatchlistViewModel(
        observeAccountUseCase = observeAccountUseCase.mock,
        fetchWatchlistUseCase = fetchWatchlistUseCase.mock,
      )
    }

    declare {
      DetailsViewModel(
        getMediaDetailsUseCase = getMediaDetailsUseCase.mock,
        onMarkAsFavoriteUseCase = markAsFavoriteUseCase,
        submitRatingUseCase = submitRatingUseCase.mock,
        deleteRatingUseCase = deleteRatingUseCase.mock,
        addToWatchlistUseCase = addToWatchlistUseCase.mock,
        requestMediaUseCase = requestMediaUseCase.mock,
        spoilersObfuscationUseCase = spoilersObfuscationUseCase,
        fetchAllRatingsUseCase = fetchAllRatingsUseCase.mock,
        savedStateHandle = SavedStateHandle(
          mapOf(
            "id" to 1,
            "isFavorite" to false,
            "mediaType" to MediaType.MOVIE,
          ),
        ),
      )
    }

    setContentWithTheme {
      val state = rememberScenePeekAppState(networkMonitor)

      KoinContext {
        ScenePeekApp(
          state = state,
          uiState = uiState,
          uiEvent = uiEvent,
          onConsumeEvent = {},
        )
      }
    }

    with(composeTestRule) {
      onNodeWithText(homeTab).assertIsDisplayed()
      onNodeWithText(watchlistTab).assertIsDisplayed()

      onNodeWithText(watchlistTab).performClick()

      onNodeWithText(WatchlistResponseFactory.movies().data.first().name).assertIsDisplayed()
      onNodeWithText(WatchlistResponseFactory.movies().data.last().name).assertDoesNotExist()
      // Scroll to last items of watchlist
      onNodeWithTag(TestTags.Watchlist.WATCHLIST_CONTENT).performScrollToNode(
        hasText(WatchlistResponseFactory.movies().data.last().name),
      )

      onNodeWithText(WatchlistResponseFactory.movies().data[3].name).assertDoesNotExist()
      onNodeWithText(WatchlistResponseFactory.movies().data.last().name)
        .assertIsDisplayed()
        .performClick()

      // Has navigated to details screen
      onNodeWithTag(TestTags.Details.CONTENT_SCAFFOLD).assertIsDisplayed()
      onNodeWithTag(TestTags.Watchlist.WATCHLIST_SCREEN).assertIsNotDisplayed()

      onNodeWithText(watchlistTab).performClick()
      onNodeWithTag(TestTags.Watchlist.WATCHLIST_SCREEN).assertIsDisplayed()
      onNodeWithTag(TestTags.Details.CONTENT_SCAFFOLD).assertIsNotDisplayed()

      // Has navigated back to watchlist while keeping the scroll position
      onNodeWithText(WatchlistResponseFactory.movies().data[3].name).assertIsNotDisplayed()
      onNodeWithText(WatchlistResponseFactory.movies().data.last().name).assertIsDisplayed()
    }
  }

  @Test
  fun `test navigate between home and watchlist does not re-create watchlist`() = runTest {
    val homeTab = getString(R.string.home)
    val watchlistTab = getString(R.string.watchlist)

    popularMoviesUseCase.mockFetchPopularMovies(
      response = Result.success(MediaItemFactory.MoviesList()),
    )

    observeAccountUseCase.mockSuccess(Result.success(true))
    fetchWatchlistUseCase.mockSuccess(
      Result.success(
        WatchlistResponseFactory.movies().copy(canFetchMore = false),
      ),
    )

    declare {
      HomeViewModel(
        getPopularMoviesUseCase = popularMoviesUseCase.mock,
        fetchMultiInfoSearchUseCase = fetchMultiInfoSearchUseCase.mock,
        markAsFavoriteUseCase = markAsFavoriteUseCase,
        getFavoriteMoviesUseCase = getFavoriteMoviesUseCase.mock,
      )
    }

    declare {
      WatchlistViewModel(
        observeAccountUseCase = observeAccountUseCase.mock,
        fetchWatchlistUseCase = fetchWatchlistUseCase.mock,
      )
    }

    setContentWithTheme {
      val state = rememberScenePeekAppState(networkMonitor)

      KoinContext {
        ScenePeekApp(
          state = state,
          uiState = uiState,
          uiEvent = uiEvent,
          onConsumeEvent = {},
        )
      }
    }

    with(composeTestRule) {
      onNodeWithText(homeTab).assertIsDisplayed()
      onNodeWithText(watchlistTab).assertIsDisplayed()

      // Go to watchlist
      onNodeWithText(watchlistTab).performClick()

      onNodeWithText(WatchlistResponseFactory.movies().data.first().name).assertIsDisplayed()
      onNodeWithText(WatchlistResponseFactory.movies().data.last().name).assertDoesNotExist()
      // Scroll to last items of watchlist
      onNodeWithTag(TestTags.Watchlist.WATCHLIST_CONTENT).performScrollToNode(
        hasText(WatchlistResponseFactory.movies().data.last().name),
      )

      onNodeWithText(WatchlistResponseFactory.movies().data[3].name).assertDoesNotExist()
      onNodeWithText(WatchlistResponseFactory.movies().data.last().name).assertIsDisplayed()

      onNodeWithText(homeTab).performClick()
      onNodeWithTag(TestTags.Watchlist.WATCHLIST_SCREEN).assertIsNotDisplayed()

      // Has navigated back to watchlist while keeping the scroll position
      onNodeWithText(watchlistTab).performClick()
      onNodeWithTag(TestTags.Watchlist.WATCHLIST_SCREEN).assertIsDisplayed()
      onNodeWithText(WatchlistResponseFactory.movies().data[3].name).assertDoesNotExist()
      onNodeWithText(WatchlistResponseFactory.movies().data.last().name).assertIsDisplayed()
    }
  }

  @Test
  fun `test loading content is visible when uiState is loading`() = runTest {
    uiState = MainUiState.Loading

    setContentWithTheme {
      val navController = rememberTestNavController()

      KoinContext {
        ScenePeekApp(
          state = ScenePeekAppState(
            navController = navController,
            scope = backgroundScope,
            networkMonitor = networkMonitor,
          ),
          uiState = uiState,
          uiEvent = uiEvent,
          onConsumeEvent = {},
        )
      }
    }

    with(composeTestRule) {
      onNodeWithTag(TestTags.LOADING_CONTENT).assertIsDisplayed()
    }
  }

  @Test
  fun `test network monitor emits correct value`() = runTest(
    MainDispatcherRule().testDispatcher.unconfined,
  ) {
    setContentWithTheme {
      val navController = rememberTestNavController()
      state = ScenePeekAppState(
        navController = navController,
        scope = backgroundScope,
        networkMonitor = networkMonitor,
      )
    }

    state.isOffline.test {
      assertThat(awaitItem()).isFalse()

      networkMonitor.setConnected(false)

      assertThat(awaitItem()).isTrue()
    }
  }

  @Test
  fun `test when state is offline not connected snackbar is visible`() = runTest(
    MainDispatcherRule().testDispatcher.unconfined,
  ) {
    popularMoviesUseCase.mockFetchPopularMovies(
      response = Result.failure(Exception("")),
    )

    declare {
      HomeViewModel(
        getPopularMoviesUseCase = popularMoviesUseCase.mock,
        fetchMultiInfoSearchUseCase = fetchMultiInfoSearchUseCase.mock,
        markAsFavoriteUseCase = markAsFavoriteUseCase,
        getFavoriteMoviesUseCase = getFavoriteMoviesUseCase.mock,
      )
    }

    setContentWithTheme {
      val state = rememberScenePeekAppState(
        networkMonitor = networkMonitor,
        scope = backgroundScope,
      )

      KoinContext {
        ScenePeekApp(
          state = state,
          uiState = uiState,
          uiEvent = uiEvent,
          onConsumeEvent = {},
        )
      }
    }

    networkMonitor.setConnected(false)

    with(composeTestRule) {
      onNodeWithText(getString(uiR.string.core_ui_not_connected)).assertIsDisplayed()

      networkMonitor.setConnected(true)

      onNodeWithText(getString(uiR.string.core_ui_connected)).assertIsDisplayed()
      onNodeWithText(getString(uiR.string.core_ui_not_connected)).assertIsNotDisplayed()

      advanceTimeBy(5000)

      onNodeWithText(getString(uiR.string.core_ui_connected)).assertIsNotDisplayed()
    }
  }

  fun `test currentDestination`() = runTest {
    var currentDestination: String? = null

    setContentWithTheme {
      val navController = rememberTestNavController()
      state = remember(navController) {
        ScenePeekAppState(
          navController = navController,
          scope = backgroundScope,
          networkMonitor = networkMonitor,
        )
      }

      currentDestination = state.currentDestination?.route

      LaunchedEffect(Unit) {
        navController.setCurrentDestination("Screen B")
      }
    }

    assertThat(currentDestination).isEqualTo("Screen B")
  }

  @Test
  fun `test TopLevelDestinations`() = runTest {
    lateinit var state: ScenePeekAppState

    setContentWithTheme {
      state = rememberScenePeekAppState(
        networkMonitor = networkMonitor,
      )
    }

    assertThat(state.topLevelDestinations.size).isEqualTo(2)
    assertThat(state.topLevelDestinations[0].name).contains(TopLevelDestination.HOME.name)
    assertThat(state.topLevelDestinations[1].name).contains(TopLevelDestination.WATCHLIST.name)
  }
}

@Composable
private fun rememberTestNavController(): TestNavHostController {
  val context = LocalContext.current
  return remember {
    TestNavHostController(context).apply {
      navigatorProvider.addNavigator(ComposeNavigator())
      graph = createGraph(startDestination = "Screen A") {
        composable("Screen A") { }
        composable("Screen B") { }
        composable("Screen C") { }
      }
    }
  }
}
