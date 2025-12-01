package com.divinelink.scenepeek.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsNotDisplayed
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollToNode
import androidx.compose.ui.test.performTextInput
import androidx.compose.ui.test.performTouchInput
import androidx.compose.ui.test.swipeDown
import androidx.lifecycle.SavedStateHandle
import androidx.navigation.compose.ComposeNavigator
import androidx.navigation.compose.composable
import androidx.navigation.createGraph
import androidx.navigation.testing.TestNavHostController
import app.cash.turbine.test
import com.divinelink.core.domain.credits.SpoilersObfuscationUseCase
import com.divinelink.core.domain.search.SearchStateManager
import com.divinelink.core.fixtures.core.data.network.TestNetworkMonitor
import com.divinelink.core.fixtures.data.preferences.TestPreferencesRepository
import com.divinelink.core.fixtures.manager.TestOnboardingManager
import com.divinelink.core.fixtures.model.account.TMDBAccountFactory
import com.divinelink.core.fixtures.model.details.MediaDetailsFactory
import com.divinelink.core.fixtures.model.media.MediaItemFactory
import com.divinelink.core.model.details.media.MediaDetailsResult
import com.divinelink.core.model.details.rating.RatingSource
import com.divinelink.core.model.media.MediaType
import com.divinelink.core.scaffold.NavGraphExtension
import com.divinelink.core.scaffold.ScenePeekApp
import com.divinelink.core.scaffold.ScenePeekAppState
import com.divinelink.core.scaffold.TopLevelDestination
import com.divinelink.core.scaffold.rememberScenePeekAppState
import com.divinelink.core.testing.ComposeTest
import com.divinelink.core.testing.MainDispatcherRule
import com.divinelink.core.testing.repository.TestAuthRepository
import com.divinelink.core.testing.setContentWithTheme
import com.divinelink.core.testing.uiTest
import com.divinelink.core.testing.usecase.FakeAddToWatchlistUseCase
import com.divinelink.core.testing.usecase.FakeDeleteRatingUseCase
import com.divinelink.core.testing.usecase.FakeFetchMultiInfoSearchUseCase
import com.divinelink.core.testing.usecase.FakeGetAccountDetailsUseCase
import com.divinelink.core.testing.usecase.FakeGetFavoriteMoviesUseCase
import com.divinelink.core.testing.usecase.FakeGetJellyseerrDetailsUseCase
import com.divinelink.core.testing.usecase.FakeGetMediaDetailsUseCase
import com.divinelink.core.testing.usecase.FakeGetPopularMoviesUseCase
import com.divinelink.core.testing.usecase.FakeRequestMediaUseCase
import com.divinelink.core.testing.usecase.FakeSubmitRatingUseCase
import com.divinelink.core.testing.usecase.TestDeleteMediaUseCase
import com.divinelink.core.testing.usecase.TestDeleteRequestUseCase
import com.divinelink.core.testing.usecase.TestFetchAllRatingsUseCase
import com.divinelink.core.testing.usecase.TestMarkAsFavoriteUseCase
import com.divinelink.core.testing.usecase.TestMarkOnboardingCompleteUseCase
import com.divinelink.core.testing.usecase.TestSpoilersObfuscationUseCase
import com.divinelink.core.ui.MainUiEvent
import com.divinelink.core.ui.MainUiState
import com.divinelink.core.ui.TestTags
import com.divinelink.core.ui.UiString
import com.divinelink.core.ui.components.ToolbarState
import com.divinelink.core.ui.core_ui_connected
import com.divinelink.core.ui.core_ui_not_connected
import com.divinelink.core.ui.core_ui_toolbar_search_placeholder
import com.divinelink.feature.details.media.ui.DetailsViewModel
import com.divinelink.feature.onboarding.manager.IntroSections
import com.divinelink.feature.onboarding.ui.IntroViewModel
import com.divinelink.feature.profile.ProfileViewModel
import com.divinelink.feature.search.ui.SearchViewModel
import com.divinelink.scenepeek.di.navigationModule
import com.divinelink.scenepeek.home.ui.HomeViewModel
import com.divinelink.scenepeek.top_level_navigation_content_description_selected
import com.divinelink.scenepeek.top_level_navigation_content_description_unselected
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.runTest
import org.jetbrains.compose.resources.getString
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.test.get
import org.koin.test.mock.declare
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import com.divinelink.scenepeek.Res as R

class ScenePeekAppTest : ComposeTest() {

  private lateinit var uiState: MainUiState
  private lateinit var uiEvent: MainUiEvent

  private val networkMonitor = TestNetworkMonitor()
  private val onboardingManager = TestOnboardingManager()
  private val preferencesRepository = TestPreferencesRepository()
  private lateinit var navigationProvider: List<NavGraphExtension>

  private val homeTab = "Home"
  private val profileTab = "Profile"
  private val searchTab = "Search"

  private lateinit var state: ScenePeekAppState

  // HOME use cases
  private lateinit var popularMoviesUseCase: FakeGetPopularMoviesUseCase
  private lateinit var fetchMultiInfoSearchUseCase: FakeFetchMultiInfoSearchUseCase
  private lateinit var markAsFavoriteUseCase: TestMarkAsFavoriteUseCase
  private lateinit var getFavoriteMoviesUseCase: FakeGetFavoriteMoviesUseCase

  private val searchStateManager = SearchStateManager()

  // Profile use cases
  private lateinit var getAccountDetailsUseCaseTest: FakeGetAccountDetailsUseCase

  // DETAILS use cases
  private lateinit var getMediaDetailsUseCase: FakeGetMediaDetailsUseCase
  private lateinit var submitRatingUseCase: FakeSubmitRatingUseCase
  private lateinit var deleteRatingUseCase: FakeDeleteRatingUseCase
  private lateinit var addToWatchlistUseCase: FakeAddToWatchlistUseCase
  private lateinit var requestMediaUseCase: FakeRequestMediaUseCase
  private lateinit var spoilersObfuscationUseCase: SpoilersObfuscationUseCase
  private lateinit var fetchAllRatingsUseCase: TestFetchAllRatingsUseCase
  private lateinit var deleteRequestUseCase: TestDeleteRequestUseCase
  private lateinit var deleteMediaUseCase: TestDeleteMediaUseCase
  private lateinit var authRepository: TestAuthRepository

  // Onboarding use cases
  private val markOnboardingCompleteUseCase = TestMarkOnboardingCompleteUseCase()
  private val getAccountDetailsUseCase = FakeGetAccountDetailsUseCase()
  private val getJellyseerrAccountDetailsUseCase = FakeGetJellyseerrDetailsUseCase()

  @BeforeTest
  override fun setUp() {
    uiState = MainUiState.Completed
    uiEvent = MainUiEvent.None

    popularMoviesUseCase = FakeGetPopularMoviesUseCase()
    fetchMultiInfoSearchUseCase = FakeFetchMultiInfoSearchUseCase()
    markAsFavoriteUseCase = TestMarkAsFavoriteUseCase()
    getFavoriteMoviesUseCase = FakeGetFavoriteMoviesUseCase()

    getAccountDetailsUseCaseTest = FakeGetAccountDetailsUseCase()
    authRepository = TestAuthRepository()

    getMediaDetailsUseCase = FakeGetMediaDetailsUseCase()
    submitRatingUseCase = FakeSubmitRatingUseCase()
    deleteRatingUseCase = FakeDeleteRatingUseCase()
    addToWatchlistUseCase = FakeAddToWatchlistUseCase()
    requestMediaUseCase = FakeRequestMediaUseCase()
    fetchAllRatingsUseCase = TestFetchAllRatingsUseCase()
    spoilersObfuscationUseCase = TestSpoilersObfuscationUseCase().useCase()
    deleteRequestUseCase = TestDeleteRequestUseCase()
    deleteMediaUseCase = TestDeleteMediaUseCase()
    authRepository = TestAuthRepository()

    startKoin {
      modules(
        navigationModule,
      )
    }

    navigationProvider = get<List<NavGraphExtension>>()
  }

  @AfterTest
  fun tearDown() {
    stopKoin()
  }

  @Test
  fun `test navigation items are visible`() = uiTest {
    popularMoviesUseCase.mockFetchPopularMovies(
      response = Result.success(MediaItemFactory.MoviesList()),
    )

    declare {
      HomeViewModel(
        getPopularMoviesUseCase = popularMoviesUseCase.mock,
        markAsFavoriteUseCase = markAsFavoriteUseCase,
        getFavoriteMoviesUseCase = getFavoriteMoviesUseCase.mock,
        searchStateManager = searchStateManager,
      )
    }

    setContentWithTheme {
      val state = rememberScenePeekAppState(
        networkMonitor = networkMonitor,
        onboardingManager = onboardingManager,
        navigationProvider = navigationProvider,
        preferencesRepository = preferencesRepository,
      )

      ScenePeekApp(
        state = state,
        uiState = uiState,
        uiEvent = uiEvent,
        onConsumeEvent = {},
      )
    }

    onNodeWithText(homeTab).assertExists()
    onNodeWithText(profileTab).assertExists()

    onNodeWithContentDescription(
      getString(R.string.top_level_navigation_content_description_selected, homeTab),
      useUnmergedTree = true,
    ).assertExists()

    onNodeWithContentDescription(
      getString(R.string.top_level_navigation_content_description_unselected, profileTab),
      useUnmergedTree = true,
    ).assertExists()

    onNodeWithContentDescription(
      getString(R.string.top_level_navigation_content_description_unselected, searchTab),
      useUnmergedTree = true,
    ).assertExists()
  }

  @Test
  fun `test navigate to profile`() = uiTest {
    popularMoviesUseCase.mockFetchPopularMovies(
      response = Result.success(MediaItemFactory.MoviesList()),
    )

    authRepository.mockJellyseerrEnabled(true)

    declare {
      HomeViewModel(
        getPopularMoviesUseCase = popularMoviesUseCase.mock,
        markAsFavoriteUseCase = markAsFavoriteUseCase,
        getFavoriteMoviesUseCase = getFavoriteMoviesUseCase.mock,
        searchStateManager = searchStateManager,
      )
    }

    declare {
      ProfileViewModel(
        getAccountDetailsUseCase = getAccountDetailsUseCase.mock,
        authRepository = authRepository.mock,
      )
    }

    setContentWithTheme {
      val state = rememberScenePeekAppState(
        networkMonitor = networkMonitor,
        onboardingManager = onboardingManager,
        navigationProvider = navigationProvider,
        preferencesRepository = preferencesRepository,
      )

      ScenePeekApp(
        state = state,
        uiState = uiState,
        uiEvent = uiEvent,
        onConsumeEvent = {},
      )
    }

    onNodeWithText(homeTab).assertExists()
    onNodeWithText(profileTab).assertExists()

    onNodeWithContentDescription(
      getString(R.string.top_level_navigation_content_description_selected, homeTab),
      useUnmergedTree = true,
    ).assertExists()

    onNodeWithContentDescription(
      getString(R.string.top_level_navigation_content_description_unselected, profileTab),
      useUnmergedTree = true,
    ).assertExists()

    onNodeWithText(profileTab).performClick()

    onNodeWithContentDescription(
      getString(R.string.top_level_navigation_content_description_selected, profileTab),
      useUnmergedTree = true,
    ).assertExists()

    onNodeWithContentDescription(
      getString(R.string.top_level_navigation_content_description_unselected, homeTab),
      useUnmergedTree = true,
    ).assertExists()
  }

  @Test
  fun `test navigate to search from home search bar open search with focused search`() = uiTest {
    val homeTab = "Home"
    val searchTab = "Search"

    popularMoviesUseCase.mockFetchPopularMovies(
      response = Result.success(MediaItemFactory.MoviesList()),
    )

    declare {
      HomeViewModel(
        getPopularMoviesUseCase = popularMoviesUseCase.mock,
        markAsFavoriteUseCase = markAsFavoriteUseCase,
        getFavoriteMoviesUseCase = getFavoriteMoviesUseCase.mock,
        searchStateManager = searchStateManager,
      )
    }

    declare {
      SearchViewModel(
        fetchMultiInfoSearchUseCase = fetchMultiInfoSearchUseCase.mock,
        markAsFavoriteUseCase = markAsFavoriteUseCase,
        searchStateManager = searchStateManager,
      )
    }

    setContentWithTheme {
      val state = rememberScenePeekAppState(
        networkMonitor = networkMonitor,
        onboardingManager = onboardingManager,
        navigationProvider = navigationProvider,
        preferencesRepository = preferencesRepository,
      )

      ScenePeekApp(
        state = state,
        uiState = uiState,
        uiEvent = uiEvent,
        onConsumeEvent = {},
      )
    }

    onNodeWithText(homeTab).assertExists()

    onNodeWithContentDescription(
      getString(R.string.top_level_navigation_content_description_selected, homeTab),
      useUnmergedTree = true,
    ).assertIsDisplayed()

    onNodeWithContentDescription(
      getString(R.string.top_level_navigation_content_description_unselected, searchTab),
      useUnmergedTree = true,
    ).assertIsDisplayed()

    onNodeWithTag(TestTags.Components.SearchBar.CLICKABLE_SEARCH_BAR).performClick()

    onNodeWithContentDescription(
      getString(R.string.top_level_navigation_content_description_selected, searchTab),
      useUnmergedTree = true,
    ).assertExists()

    onNodeWithContentDescription(
      getString(R.string.top_level_navigation_content_description_unselected, homeTab),
      useUnmergedTree = true,
    ).assertExists()

    onNodeWithTag(
      TestTags.Components.SearchBar.SEARCH_BAR.format(ToolbarState.Focused),
    ).assertIsDisplayed()
  }

  /**
   * Test case where we make sure that when navigation from home to search by pressing
   * the search bar, the search bar gets focused.
   *
   * Then when leaving search tab and re-entering it this time by clicking on the search tab,
   * the search bar should not be focused.
   */
  @Test
  fun `test search bar remains unfocused when navigating between home and search`() = uiTest {
    popularMoviesUseCase.mockFetchPopularMovies(
      response = Result.success(MediaItemFactory.MoviesList()),
    )

    declare {
      HomeViewModel(
        getPopularMoviesUseCase = popularMoviesUseCase.mock,
        markAsFavoriteUseCase = markAsFavoriteUseCase,
        getFavoriteMoviesUseCase = getFavoriteMoviesUseCase.mock,
        searchStateManager = searchStateManager,
      )
    }

    declare {
      SearchViewModel(
        fetchMultiInfoSearchUseCase = fetchMultiInfoSearchUseCase.mock,
        markAsFavoriteUseCase = markAsFavoriteUseCase,
        searchStateManager = searchStateManager,
      )
    }

    setContentWithTheme {
      val state = rememberScenePeekAppState(
        networkMonitor = networkMonitor,
        onboardingManager = onboardingManager,
        navigationProvider = navigationProvider,
        preferencesRepository = preferencesRepository,
      )

      ScenePeekApp(
        state = state,
        uiState = uiState,
        uiEvent = uiEvent,
        onConsumeEvent = {},
      )
    }

    onNodeWithText(homeTab).assertExists()

    onNodeWithContentDescription(
      getString(R.string.top_level_navigation_content_description_selected, homeTab),
      useUnmergedTree = true,
    ).assertIsDisplayed()

    onNodeWithContentDescription(
      getString(R.string.top_level_navigation_content_description_unselected, searchTab),
      useUnmergedTree = true,
    ).assertIsDisplayed()

    onNodeWithTag(TestTags.Components.SearchBar.CLICKABLE_SEARCH_BAR).performClick()

    onNodeWithContentDescription(
      getString(R.string.top_level_navigation_content_description_selected, searchTab),
      useUnmergedTree = true,
    ).assertExists()

    onNodeWithContentDescription(
      getString(R.string.top_level_navigation_content_description_unselected, homeTab),
      useUnmergedTree = true,
    ).assertExists()

    onNodeWithTag(
      TestTags.Components.SearchBar.SEARCH_BAR.format(ToolbarState.Focused),
    ).assertIsDisplayed()

    onNodeWithTag(TestTags.Components.SearchBar.CLOSE_SEARCH).performClick()

    onNodeWithTag(
      TestTags.Components.SearchBar.SEARCH_BAR.format(ToolbarState.Unfocused),
    ).assertIsDisplayed()

    onNodeWithContentDescription(
      getString(R.string.top_level_navigation_content_description_unselected, homeTab),
      useUnmergedTree = true,
    ).performClick()

    onNodeWithContentDescription(
      getString(R.string.top_level_navigation_content_description_unselected, searchTab),
      useUnmergedTree = true,
    ).performClick()

    onNodeWithTag(
      TestTags.Components.SearchBar.SEARCH_BAR.format(ToolbarState.Unfocused),
    ).assertIsDisplayed()
  }

  @Test
  fun `test navigate to search`() = uiTest {
    popularMoviesUseCase.mockFetchPopularMovies(
      response = Result.success(MediaItemFactory.MoviesList()),
    )

    declare {
      HomeViewModel(
        getPopularMoviesUseCase = popularMoviesUseCase.mock,
        markAsFavoriteUseCase = markAsFavoriteUseCase,
        getFavoriteMoviesUseCase = getFavoriteMoviesUseCase.mock,
        searchStateManager = searchStateManager,
      )
    }

    declare {
      SearchViewModel(
        fetchMultiInfoSearchUseCase = fetchMultiInfoSearchUseCase.mock,
        markAsFavoriteUseCase = markAsFavoriteUseCase,
        searchStateManager = searchStateManager,
      )
    }

    setContentWithTheme {
      val state = rememberScenePeekAppState(
        networkMonitor = networkMonitor,
        onboardingManager = onboardingManager,
        navigationProvider = navigationProvider,
        preferencesRepository = preferencesRepository,
      )

      ScenePeekApp(
        state = state,
        uiState = uiState,
        uiEvent = uiEvent,
        onConsumeEvent = {},
      )
    }

    onNodeWithText(homeTab).assertExists()

    onNodeWithContentDescription(
      getString(R.string.top_level_navigation_content_description_selected, homeTab),
      useUnmergedTree = true,
    ).assertExists()

    onNodeWithContentDescription(
      getString(R.string.top_level_navigation_content_description_unselected, searchTab),
      useUnmergedTree = true,
    )
      .assertExists()
      .performClick()

    onNodeWithContentDescription(
      getString(R.string.top_level_navigation_content_description_selected, searchTab),
      useUnmergedTree = true,
    ).assertExists()

    onNodeWithContentDescription(
      getString(R.string.top_level_navigation_content_description_unselected, homeTab),
      useUnmergedTree = true,
    ).assertExists()
  }

  @Test
  fun `test re-selecting search tab focuses search bar`() = uiTest {
    popularMoviesUseCase.mockFetchPopularMovies(
      response = Result.success(MediaItemFactory.MoviesList()),
    )

    declare {
      HomeViewModel(
        getPopularMoviesUseCase = popularMoviesUseCase.mock,
        markAsFavoriteUseCase = markAsFavoriteUseCase,
        getFavoriteMoviesUseCase = getFavoriteMoviesUseCase.mock,
        searchStateManager = searchStateManager,
      )
    }

    declare {
      SearchViewModel(
        fetchMultiInfoSearchUseCase = fetchMultiInfoSearchUseCase.mock,
        markAsFavoriteUseCase = markAsFavoriteUseCase,
        searchStateManager = searchStateManager,
      )
    }

    setContentWithTheme {
      val state = rememberScenePeekAppState(
        networkMonitor = networkMonitor,
        onboardingManager = onboardingManager,
        navigationProvider = navigationProvider,
        preferencesRepository = preferencesRepository,
      )

      ScenePeekApp(
        state = state,
        uiState = uiState,
        uiEvent = uiEvent,
        onConsumeEvent = {},
      )
    }

    onNodeWithText(homeTab).assertExists()

    onNodeWithContentDescription(
      getString(R.string.top_level_navigation_content_description_selected, homeTab),
      useUnmergedTree = true,
    ).assertExists()

    onNodeWithContentDescription(
      getString(R.string.top_level_navigation_content_description_unselected, searchTab),
      useUnmergedTree = true,
    )
      .assertExists()
      .performClick()

    onNodeWithContentDescription(
      getString(R.string.top_level_navigation_content_description_selected, searchTab),
      useUnmergedTree = true,
    ).assertExists()

    onNodeWithContentDescription(
      getString(R.string.top_level_navigation_content_description_unselected, homeTab),
      useUnmergedTree = true,
    ).assertExists()

    onNodeWithTag(
      TestTags.Components.SearchBar.SEARCH_BAR.format(ToolbarState.Unfocused),
    ).assertIsDisplayed()

    onNodeWithTag(
      TestTags.Components.SearchBar.SEARCH_BAR.format(ToolbarState.Focused),
    ).assertIsNotDisplayed()

    onNodeWithContentDescription(
      getString(R.string.top_level_navigation_content_description_selected, searchTab),
      useUnmergedTree = true,
    ).performClick()

    onNodeWithTag(
      TestTags.Components.SearchBar.SEARCH_BAR.format(ToolbarState.Focused),
    ).assertIsDisplayed()

    onNodeWithContentDescription(
      getString(UiString.core_ui_toolbar_search_placeholder),
    )
      .performTextInput("test query")

    onNodeWithText("test query").assertIsDisplayed()
  }

  @Test
  fun `test navigate to profile when is on movie details`() = uiTest {
    popularMoviesUseCase.mockFetchPopularMovies(
      response = Result.success(MediaItemFactory.MoviesList()),
    )

    authRepository.mockJellyseerrEnabled(false)

    getAccountDetailsUseCase.mockSuccess(flowOf(Result.success(TMDBAccountFactory.loggedIn())))

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
        markAsFavoriteUseCase = markAsFavoriteUseCase,
        getFavoriteMoviesUseCase = getFavoriteMoviesUseCase.mock,
        searchStateManager = searchStateManager,
      )
    }

    declare {
      ProfileViewModel(
        getAccountDetailsUseCase = getAccountDetailsUseCase.mock,
        authRepository = authRepository.mock,
      )
    }

    declare {
      DetailsViewModel(
        getMediaDetailsUseCase = getMediaDetailsUseCase.mock,
        onMarkAsFavoriteUseCase = markAsFavoriteUseCase,
        submitRatingUseCase = submitRatingUseCase.mock,
        deleteRatingUseCase = deleteRatingUseCase.mock,
        addToWatchlistUseCase = addToWatchlistUseCase.mock,
        spoilersObfuscationUseCase = spoilersObfuscationUseCase,
        fetchAllRatingsUseCase = fetchAllRatingsUseCase.mock,
        deleteRequestUseCase = deleteRequestUseCase.mock,
        deleteMediaUseCase = deleteMediaUseCase.mock,
        authRepository = authRepository.mock,
        savedStateHandle = SavedStateHandle(
          mapOf(
            "id" to 1,
            "isFavorite" to false,
            "mediaType" to MediaType.MOVIE.value,
          ),
        ),
      )
    }

    setContentWithTheme {
      val state = rememberScenePeekAppState(
        networkMonitor = networkMonitor,
        onboardingManager = onboardingManager,
        navigationProvider = navigationProvider,
        preferencesRepository = preferencesRepository,
      )

      ScenePeekApp(
        state = state,
        uiState = uiState,
        uiEvent = uiEvent,
        onConsumeEvent = {},
      )
    }

    onNodeWithText(homeTab).assertIsDisplayed()
    onNodeWithText(profileTab).assertIsDisplayed()

    onNodeWithText(profileTab).performClick()

    onNodeWithTag(TestTags.Profile.CONTENT).assertIsDisplayed()

    onNodeWithText("Watchlist").assertIsDisplayed()
  }

  @Test
  fun `test loading content is visible when uiState is loading`() = uiTest {
//    runTest(
//      MainDispatcherRule().testDispatcher.unconfined,
//    ) {
    uiState = MainUiState.Loading

    setContentWithTheme {
      val navController = rememberTestNavController()
      val scope = rememberCoroutineScope()
      ScenePeekApp(
        state = rememberScenePeekAppState(
          navController = navController,
          scope = scope,
          onboardingManager = onboardingManager,
          networkMonitor = networkMonitor,
          navigationProvider = navigationProvider,
          preferencesRepository = preferencesRepository,
        ),
        uiState = uiState,
        uiEvent = uiEvent,
        onConsumeEvent = {},
      )
    }

    onNodeWithTag(TestTags.LOADING_CONTENT).assertIsDisplayed()
  }

  @Test
  fun `test network monitor emits correct value`() = uiTest {
    runTest(
      MainDispatcherRule().testDispatcher.unconfined,
    ) {
      setContentWithTheme {
        state = rememberScenePeekAppState(
          scope = backgroundScope,
          networkMonitor = networkMonitor,
          onboardingManager = onboardingManager,
          navigationProvider = navigationProvider,
          preferencesRepository = preferencesRepository,
        )
      }

      state.isOffline.test {
        assertThat(awaitItem()).isFalse()

        networkMonitor.setConnected(false)

        assertThat(awaitItem()).isTrue()
      }
    }
  }

  @Test
  fun `test when state is offline not connected snackbar is visible`() = uiTest {
    runTest(
      MainDispatcherRule().testDispatcher.unconfined,
    ) {
      popularMoviesUseCase.mockFetchPopularMovies(
        response = Result.failure(Exception("")),
      )

      declare {
        HomeViewModel(
          getPopularMoviesUseCase = popularMoviesUseCase.mock,
          markAsFavoriteUseCase = markAsFavoriteUseCase,
          getFavoriteMoviesUseCase = getFavoriteMoviesUseCase.mock,
          searchStateManager = searchStateManager,
        )
      }

      setContentWithTheme {
        state = rememberScenePeekAppState(
          scope = backgroundScope,
          networkMonitor = networkMonitor,
          onboardingManager = onboardingManager,
          navigationProvider = navigationProvider,
          preferencesRepository = preferencesRepository,
        )

        ScenePeekApp(
          state = state,
          uiState = uiState,
          uiEvent = uiEvent,
          onConsumeEvent = {},
        )
      }

      networkMonitor.setConnected(false)

      onNodeWithText(getString(UiString.core_ui_not_connected)).assertIsDisplayed()

      networkMonitor.setConnected(true)

      onNodeWithText(getString(UiString.core_ui_connected)).assertIsDisplayed()
      onNodeWithText(getString(UiString.core_ui_not_connected)).assertIsNotDisplayed()

      advanceTimeBy(5000)

      onNodeWithText(getString(UiString.core_ui_connected)).assertIsNotDisplayed()
    }
  }

  fun `test currentDestination`() = uiTest {
    runTest {
      var currentDestination: String? = null

      setContentWithTheme {
        val navController = rememberTestNavController()
        state = rememberScenePeekAppState(
          scope = backgroundScope,
          networkMonitor = networkMonitor,
          onboardingManager = onboardingManager,
          navigationProvider = navigationProvider,
          preferencesRepository = preferencesRepository,
        )

        currentDestination = state.currentDestination?.route

        LaunchedEffect(Unit) {
          navController.setCurrentDestination("Screen B")
        }
      }

      assertThat(currentDestination).isEqualTo("Screen B")
    }
  }

  @Test
  fun `test TopLevelDestinations`() = runTest {
    uiTest {
      lateinit var state: ScenePeekAppState

      setContentWithTheme {
        state = rememberScenePeekAppState(
          scope = backgroundScope,
          networkMonitor = networkMonitor,
          onboardingManager = onboardingManager,
          navigationProvider = navigationProvider,
          preferencesRepository = preferencesRepository,
        )
      }

      assertThat(state.topLevelDestinations.size).isEqualTo(3)
      assertThat(state.topLevelDestinations[0].name).contains(TopLevelDestination.HOME.name)
      assertThat(state.topLevelDestinations[1].name).contains(TopLevelDestination.SEARCH.name)
      assertThat(state.topLevelDestinations[2].name).contains(TopLevelDestination.PROFILE.name)
    }
  }

  @Test
  fun `test when onboarding is visible and initial full screen onboarding is shown`() = uiTest {
    popularMoviesUseCase.mockFetchPopularMovies(
      response = Result.success(MediaItemFactory.MoviesList()),
    )

    declare {
      HomeViewModel(
        getPopularMoviesUseCase = popularMoviesUseCase.mock,
        markAsFavoriteUseCase = markAsFavoriteUseCase,
        getFavoriteMoviesUseCase = getFavoriteMoviesUseCase.mock,
        searchStateManager = searchStateManager,
      )
    }

    declare {
      IntroViewModel(
        markOnboardingCompleteUseCase = markOnboardingCompleteUseCase.mock,
        getAccountDetailsUseCase = getAccountDetailsUseCase.mock,
        getJellyseerrProfileUseCase = getJellyseerrAccountDetailsUseCase.mock,
        onboardingManager = onboardingManager,
      )
    }

    onboardingManager.setIsInitialOnboarding(true)
    onboardingManager.setShowIntro(true)
    onboardingManager.setSections(IntroSections.onboardingSections)

    setContentWithTheme {
      state = rememberScenePeekAppState(
        networkMonitor = networkMonitor,
        onboardingManager = onboardingManager,
        navigationProvider = navigationProvider,
        preferencesRepository = preferencesRepository,
      )

      ScenePeekApp(
        state = state,
        uiState = uiState,
        uiEvent = uiEvent,
        onConsumeEvent = {},
      )
    }

    onNodeWithTag(TestTags.Onboarding.FULLSCREEN).assertIsDisplayed()
    onNodeWithTag(TestTags.Components.NAVIGATION_BAR).assertIsNotDisplayed()

    // Try to dismiss onboarding by swiping down
    onNodeWithTag(TestTags.Onboarding.FULLSCREEN).performTouchInput {
      swipeDown()
    }

    // Still displayed because it's not dismissable through swipe
    onNodeWithTag(TestTags.Onboarding.FULLSCREEN).assertIsDisplayed()

    onNodeWithTag(TestTags.Components.SCROLLABLE_CONTENT).performScrollToNode(
      matcher = hasText("Get started"),
    )
    onNodeWithText("Get started").performClick()
    onNodeWithTag(TestTags.Onboarding.FULLSCREEN).assertIsNotDisplayed()

    onNodeWithText("Fight club 1").assertIsDisplayed()
  }

  @Test
  fun `test when intro is visible and is not firstLaunch modal intro is shown`() = uiTest {
    popularMoviesUseCase.mockFetchPopularMovies(
      response = Result.success(MediaItemFactory.MoviesList()),
    )

    declare {
      HomeViewModel(
        getPopularMoviesUseCase = popularMoviesUseCase.mock,
        markAsFavoriteUseCase = markAsFavoriteUseCase,
        getFavoriteMoviesUseCase = getFavoriteMoviesUseCase.mock,
        searchStateManager = searchStateManager,
      )
    }

    declare {
      IntroViewModel(
        markOnboardingCompleteUseCase = markOnboardingCompleteUseCase.mock,
        getAccountDetailsUseCase = getAccountDetailsUseCase.mock,
        getJellyseerrProfileUseCase = getJellyseerrAccountDetailsUseCase.mock,
        onboardingManager = onboardingManager,
      )
    }

    onboardingManager.setIsInitialOnboarding(false)
    onboardingManager.setShowIntro(true)

    setContentWithTheme {
      state = rememberScenePeekAppState(
        networkMonitor = networkMonitor,
        onboardingManager = onboardingManager,
        navigationProvider = navigationProvider,
        preferencesRepository = preferencesRepository,
      )

      ScenePeekApp(
        state = state,
        uiState = uiState,
        uiEvent = uiEvent,
        onConsumeEvent = {},
      )
    }

    onNodeWithTag(TestTags.Onboarding.FULLSCREEN).assertIsNotDisplayed()
    onNodeWithTag(TestTags.Onboarding.MODAL).assertIsDisplayed()

    // Modal is dismissable through swipe
    onNodeWithTag(TestTags.Onboarding.MODAL).performTouchInput {
      swipeDown()
    }

    onNodeWithTag(TestTags.Onboarding.MODAL).assertIsNotDisplayed()
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
