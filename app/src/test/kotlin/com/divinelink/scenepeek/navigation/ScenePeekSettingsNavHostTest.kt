package com.divinelink.scenepeek.navigation

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollToNode
import androidx.compose.ui.test.performTouchInput
import androidx.compose.ui.test.swipeUp
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.compose.ComposeNavigator
import androidx.navigation.testing.TestNavHostController
import com.divinelink.core.domain.search.SearchStateManager
import com.divinelink.core.domain.settings.MediaRatingPreferenceUseCase
import com.divinelink.core.fixtures.core.data.network.TestNetworkMonitor
import com.divinelink.core.fixtures.data.preferences.TestPreferencesRepository
import com.divinelink.core.fixtures.manager.TestOnboardingManager
import com.divinelink.core.navigation.route.Navigation.AboutSettingsRoute
import com.divinelink.core.navigation.route.Navigation.AccountSettingsRoute
import com.divinelink.core.navigation.route.Navigation.AppearanceSettingsRoute
import com.divinelink.core.navigation.route.Navigation.DetailsPreferencesSettingsRoute
import com.divinelink.core.navigation.route.Navigation.JellyseerrSettingsRoute
import com.divinelink.core.navigation.route.Navigation.LinkHandlingSettingsRoute
import com.divinelink.core.navigation.route.Navigation.SettingsRoute
import com.divinelink.core.navigation.route.Navigation.TMDBAuthRoute
import com.divinelink.core.scaffold.NavGraphExtension
import com.divinelink.core.scaffold.ProvideScenePeekAppState
import com.divinelink.core.scaffold.ScenePeekNavHost
import com.divinelink.core.scaffold.rememberScaffoldState
import com.divinelink.core.scaffold.rememberScenePeekAppState
import com.divinelink.core.testing.ComposeTest
import com.divinelink.core.testing.MainDispatcherRule
import com.divinelink.core.testing.getString
import com.divinelink.core.testing.storage.FakePreferenceStorage
import com.divinelink.core.testing.usecase.FakeFetchMultiInfoSearchUseCase
import com.divinelink.core.testing.usecase.FakeGetAccountDetailsUseCase
import com.divinelink.core.testing.usecase.FakeGetJellyseerrDetailsUseCase
import com.divinelink.core.testing.usecase.FakeLoginJellyseerrUseCase
import com.divinelink.core.testing.usecase.FakeLogoutJellyseerrUseCase
import com.divinelink.core.testing.usecase.TestMarkAsFavoriteUseCase
import com.divinelink.core.testing.usecase.session.FakeCreateRequestTokenUseCase
import com.divinelink.core.testing.usecase.session.FakeLogoutUseCase
import com.divinelink.core.ui.SharedTransitionScopeProvider
import com.divinelink.core.ui.TestTags
import com.divinelink.core.ui.snackbar.controller.ProvideSnackbarController
import com.divinelink.feature.settings.R
import com.divinelink.feature.settings.app.account.AccountSettingsViewModel
import com.divinelink.feature.settings.app.account.jellyseerr.JellyseerrSettingsViewModel
import com.divinelink.feature.settings.app.appearance.AppearanceSettingsViewModel
import com.divinelink.feature.settings.app.appearance.usecase.GetAvailableThemesUseCase
import com.divinelink.feature.settings.app.appearance.usecase.GetThemeUseCase
import com.divinelink.feature.settings.app.appearance.usecase.SetThemeUseCase
import com.divinelink.feature.settings.app.appearance.usecase.black.backgrounds.GetBlackBackgroundsUseCase
import com.divinelink.feature.settings.app.appearance.usecase.black.backgrounds.SetBlackBackgroundsUseCase
import com.divinelink.feature.settings.app.appearance.usecase.material.you.GetMaterialYouUseCase
import com.divinelink.feature.settings.app.appearance.usecase.material.you.SetMaterialYouUseCase
import com.divinelink.feature.settings.app.details.DetailsPreferencesViewModel
import com.divinelink.feature.tmdb.auth.TMDBAuthViewModel
import com.divinelink.scenepeek.base.di.navigationModule
import com.divinelink.scenepeek.fakes.usecase.FakeGetFavoriteMoviesUseCase
import com.divinelink.scenepeek.fakes.usecase.FakeGetPopularMoviesUseCase
import com.divinelink.scenepeek.home.ui.HomeViewModel
import com.divinelink.scenepeek.settings.appearance.usecase.material.you.FakeGetMaterialYouVisibleUseCase
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.test.get
import org.koin.test.mock.declare
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test

class ScenePeekSettingsNavHostTest : ComposeTest() {

  private lateinit var navController: TestNavHostController
  private lateinit var fakePreferenceStorage: FakePreferenceStorage

  // HOME use cases
  private lateinit var popularMoviesUseCase: FakeGetPopularMoviesUseCase
  private lateinit var fetchMultiInfoSearchUseCase: FakeFetchMultiInfoSearchUseCase
  private lateinit var markAsFavoriteUseCase: TestMarkAsFavoriteUseCase
  private lateinit var getFavoriteMoviesUseCase: FakeGetFavoriteMoviesUseCase

  @get:Rule
  val mainDispatcherRule = MainDispatcherRule()
  private val testDispatcher = mainDispatcherRule.testDispatcher

  @BeforeTest
  fun setup() {
    startKoin {
      androidContext(composeTestRule.activity)
      modules(
        navigationModule,
      )
    }

    fakePreferenceStorage = FakePreferenceStorage()

    popularMoviesUseCase = FakeGetPopularMoviesUseCase()
    fetchMultiInfoSearchUseCase = FakeFetchMultiInfoSearchUseCase()
    markAsFavoriteUseCase = TestMarkAsFavoriteUseCase()
    getFavoriteMoviesUseCase = FakeGetFavoriteMoviesUseCase()

    popularMoviesUseCase.mockFetchPopularMovies(Result.success(emptyList()))

    declare {
      HomeViewModel(
        getPopularMoviesUseCase = popularMoviesUseCase.mock,
        markAsFavoriteUseCase = markAsFavoriteUseCase,
        getFavoriteMoviesUseCase = getFavoriteMoviesUseCase.mock,
        searchStateManager = SearchStateManager(),
      )
    }

    composeTestRule.setContent {
      navController = TestNavHostController(LocalContext.current)
      navController.navigatorProvider.addNavigator(ComposeNavigator())
      val snackbarHostState = remember { SnackbarHostState() }
      val coroutineScope = rememberCoroutineScope()

      val state = rememberScenePeekAppState(
        networkMonitor = TestNetworkMonitor(),
        onboardingManager = TestOnboardingManager(),
        navController = navController,
        preferencesRepository = TestPreferencesRepository(),
        navigationProvider = get<List<NavGraphExtension>>(),
      )

      ProvideScenePeekAppState(appState = state) {
        ProvideSnackbarController(
          snackbarHostState = snackbarHostState,
          coroutineScope = coroutineScope,
        ) {
          SharedTransitionScopeProvider {
            state.sharedTransitionScope = it

            rememberScaffoldState(
              animatedVisibilityScope = this,
            ).ScenePeekNavHost()
          }
        }
      }
    }
  }

  @AfterTest
  fun tearDown() {
    stopKoin()
  }

  @Test
  fun `test navigate to AccountSettingsScreen`() = runTest {
    navController.navigate(SettingsRoute)

    declare {
      AccountSettingsViewModel(
        getAccountDetailsUseCase = FakeGetAccountDetailsUseCase().mock,
        getJellyseerrDetailsUseCase = FakeGetJellyseerrDetailsUseCase().mock,
        logoutUseCase = FakeLogoutUseCase().mock,
      )
    }

    with(composeTestRule) {
      onNodeWithText(getString(R.string.settings)).assertIsDisplayed()
      assertThat(navController.currentDestination?.hasRoute(SettingsRoute::class)).isTrue()
      assertThat(navController.currentDestination?.hasRoute(AccountSettingsRoute::class)).isFalse()

      onNodeWithText(getString(R.string.feature_settings_account)).assertExists().performClick()

      onNodeWithText(getString(R.string.settings)).assertDoesNotExist()
      assertThat(navController.currentDestination?.hasRoute(AccountSettingsRoute::class)).isTrue()

      onNodeWithTag(TestTags.Settings.NAVIGATION_ICON).performClick()
      onNodeWithText(getString(R.string.settings)).assertIsDisplayed()
      assertThat(navController.currentDestination?.hasRoute(AccountSettingsRoute::class)).isFalse()
    }
  }

  @Test
  fun `test navigate to JellyseerrAccountSettings`() = runTest {
    navController.navigate(SettingsRoute)
    navController.navigate(AccountSettingsRoute)

    declare {
      AccountSettingsViewModel(
        getAccountDetailsUseCase = FakeGetAccountDetailsUseCase().mock,
        getJellyseerrDetailsUseCase = FakeGetJellyseerrDetailsUseCase().mock,
        logoutUseCase = FakeLogoutUseCase().mock,
      )
    }

    declare {
      JellyseerrSettingsViewModel(
        logoutJellyseerrUseCase = FakeLogoutJellyseerrUseCase().mock,
        getJellyseerrDetailsUseCase = FakeGetJellyseerrDetailsUseCase().mock,
        loginJellyseerrUseCase = FakeLoginJellyseerrUseCase().mock,
      )
    }

    with(composeTestRule) {
      onNodeWithText(getString(R.string.feature_settings_account)).assertIsDisplayed()
      assertThat(navController.currentDestination?.hasRoute(AccountSettingsRoute::class)).isTrue()
      assertThat(
        navController.currentDestination?.hasRoute(JellyseerrSettingsRoute::class),
      ).isFalse()

      onNodeWithText(getString(R.string.feature_settings_jellyseerr_integration))
        .assertExists()
        .performClick()

      onNodeWithText(getString(R.string.feature_settings_account)).assertDoesNotExist()
      assertThat(
        navController.currentDestination?.hasRoute(JellyseerrSettingsRoute::class),
      ).isTrue()

      onNodeWithTag(TestTags.Settings.NAVIGATION_ICON).performClick()
      onNodeWithText(getString(R.string.feature_settings_account)).assertIsDisplayed()
      assertThat(
        navController.currentDestination?.hasRoute(JellyseerrSettingsRoute::class),
      ).isFalse()
    }
  }

  @Test
  fun `test navigate to AppearanceSettingsScreen`() {
    navController.navigate(SettingsRoute)

    fakePreferenceStorage = FakePreferenceStorage()
    val fakeGetMaterialYouVisibleUseCase = FakeGetMaterialYouVisibleUseCase(testDispatcher)

    fakeGetMaterialYouVisibleUseCase.mockMaterialYouVisible(true)

    declare {
      AppearanceSettingsViewModel(
        setThemeUseCase = SetThemeUseCase(fakePreferenceStorage, testDispatcher),
        getThemeUseCase = GetThemeUseCase(fakePreferenceStorage, testDispatcher),
        getAvailableThemesUseCase = GetAvailableThemesUseCase(testDispatcher),
        setMaterialYouUseCase = SetMaterialYouUseCase(fakePreferenceStorage, testDispatcher),
        getMaterialYouUseCase = GetMaterialYouUseCase(fakePreferenceStorage, testDispatcher),
        setBlackBackgroundsUseCase = SetBlackBackgroundsUseCase(
          preferenceStorage = fakePreferenceStorage,
          dispatcher = testDispatcher,
        ),
        getBlackBackgroundsUseCase = GetBlackBackgroundsUseCase(
          preferenceStorage = fakePreferenceStorage,
          dispatcher = testDispatcher,
        ),
        getMaterialYouVisibleUseCase = fakeGetMaterialYouVisibleUseCase,
      )
    }

    with(composeTestRule) {
      onNodeWithText(getString(R.string.settings)).assertIsDisplayed()
      assertThat(navController.currentDestination?.hasRoute(SettingsRoute::class)).isTrue()
      assertThat(
        navController.currentDestination?.hasRoute(AppearanceSettingsRoute::class),
      ).isFalse()

      onNodeWithText(getString(R.string.preferences__appearance)).assertExists().performClick()

      assertThat(
        navController.currentDestination?.hasRoute(AppearanceSettingsRoute::class),
      ).isTrue()

      onNodeWithTag(TestTags.Settings.NAVIGATION_ICON).performClick()
      onNodeWithText(getString(R.string.settings)).assertIsDisplayed()
      assertThat(
        navController.currentDestination?.hasRoute(AppearanceSettingsRoute::class),
      ).isFalse()
    }
  }

  @Test
  fun `test navigate to Details Preferences`() {
    navController.navigate(SettingsRoute)

    fakePreferenceStorage = FakePreferenceStorage()

    declare {
      DetailsPreferencesViewModel(
        mediaRatingPreferenceUseCase = MediaRatingPreferenceUseCase(
          fakePreferenceStorage,
          testDispatcher,
        ),
      )
    }

    with(composeTestRule) {
      onNodeWithText(getString(R.string.settings)).assertIsDisplayed()
      assertThat(navController.currentDestination?.hasRoute(SettingsRoute::class)).isTrue()
      assertThat(
        navController.currentDestination?.hasRoute(
          DetailsPreferencesSettingsRoute::class,
        ),
      ).isFalse()

      onNodeWithText(
        getString(R.string.feature_settings_details_preferences),
      ).assertExists().performClick()

      onNodeWithText(getString(R.string.settings)).assertDoesNotExist()
      assertThat(
        navController.currentDestination?.hasRoute(DetailsPreferencesSettingsRoute::class),
      ).isTrue()

      onNodeWithTag(TestTags.Settings.NAVIGATION_ICON).performClick()
      onNodeWithText(getString(R.string.settings)).assertIsDisplayed()
      assertThat(
        navController.currentDestination?.hasRoute(DetailsPreferencesSettingsRoute::class),
      ).isFalse()
    }
  }

  @Test
  fun `test navigate to Link Handling`() {
    navController.navigate(SettingsRoute)

    with(composeTestRule) {
      onNodeWithText(getString(R.string.settings)).assertIsDisplayed()
      assertThat(navController.currentDestination?.hasRoute(SettingsRoute::class)).isTrue()
      assertThat(
        navController.currentDestination?.hasRoute(
          DetailsPreferencesSettingsRoute::class,
        ),
      ).isFalse()

      onNodeWithText(
        getString(R.string.feature_settings_link_handling),
      ).assertExists().performClick()

      onNodeWithText(getString(R.string.settings)).assertDoesNotExist()
      assertThat(
        navController.currentDestination?.hasRoute(LinkHandlingSettingsRoute::class),
      ).isTrue()

      onNodeWithTag(TestTags.Settings.NAVIGATION_ICON).performClick()
      onNodeWithText(getString(R.string.settings)).assertIsDisplayed()
      assertThat(
        navController.currentDestination?.hasRoute(LinkHandlingSettingsRoute::class),
      ).isFalse()
    }
  }

  @Test
  fun `test navigate to AboutSettingsScreen`() {
    navController.navigate(SettingsRoute)

    with(composeTestRule) {
      onNodeWithText(getString(R.string.settings)).assertIsDisplayed()
      assertThat(navController.currentDestination?.hasRoute(SettingsRoute::class)).isTrue()
      assertThat(navController.currentDestination?.hasRoute(AboutSettingsRoute::class)).isFalse()

      onNodeWithTag(TestTags.Settings.SCREEN_CONTENT).performScrollToNode(
        hasText(getString(R.string.feature_settings_about)),
      )

      onNodeWithTag(TestTags.Settings.SCREEN_CONTENT).performTouchInput {
        swipeUp(
          startY = 100f,
          endY = 50f,
        )
      }

      onNodeWithText(getString(R.string.feature_settings_about)).assertExists().performClick()

      onNodeWithText(getString(R.string.settings)).assertDoesNotExist()
      assertThat(navController.currentDestination?.hasRoute(AboutSettingsRoute::class)).isTrue()

      onNodeWithTag(TestTags.Settings.NAVIGATION_ICON).performClick()
      onNodeWithText(getString(R.string.settings)).assertIsDisplayed()
      assertThat(navController.currentDestination?.hasRoute(AboutSettingsRoute::class)).isFalse()
    }
  }

  @Test
  fun `test navigate to TMDBAuthScreen`() {
    declare {
      AccountSettingsViewModel(
        getAccountDetailsUseCase = FakeGetAccountDetailsUseCase().mock,
        getJellyseerrDetailsUseCase = FakeGetJellyseerrDetailsUseCase().mock,
        logoutUseCase = FakeLogoutUseCase().mock,
      )
    }

    declare {
      TMDBAuthViewModel(
        createRequestTokenUseCase = FakeCreateRequestTokenUseCase().mock,
      )
    }

    navController.navigate(AccountSettingsRoute)

    with(composeTestRule) {
      assertThat(navController.currentDestination?.hasRoute(AccountSettingsRoute::class)).isTrue()
      assertThat(navController.currentDestination?.hasRoute(TMDBAuthRoute::class)).isFalse()

      onNodeWithText(getString(R.string.login)).assertExists().performClick()

      onNodeWithText(getString(R.string.settings)).assertDoesNotExist()
      assertThat(navController.currentDestination?.hasRoute(TMDBAuthRoute::class)).isTrue()
    }
  }
}
