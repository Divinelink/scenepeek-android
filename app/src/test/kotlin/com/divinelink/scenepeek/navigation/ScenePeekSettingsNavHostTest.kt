package com.divinelink.scenepeek.navigation

import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.compose.ComposeNavigator
import androidx.navigation.testing.TestNavHostController
import com.divinelink.core.domain.settings.MediaRatingPreferenceUseCase
import com.divinelink.core.testing.ComposeTest
import com.divinelink.core.testing.MainDispatcherRule
import com.divinelink.core.testing.getString
import com.divinelink.core.testing.network.TestNetworkMonitor
import com.divinelink.core.testing.setContentWithTheme
import com.divinelink.core.testing.storage.FakePreferenceStorage
import com.divinelink.core.testing.usecase.FakeGetAccountDetailsUseCase
import com.divinelink.core.testing.usecase.FakeGetJellyseerrDetailsUseCase
import com.divinelink.core.testing.usecase.FakeLoginJellyseerrUseCase
import com.divinelink.core.testing.usecase.FakeLogoutJellyseerrUseCase
import com.divinelink.core.testing.usecase.session.FakeCreateRequestTokenUseCase
import com.divinelink.core.testing.usecase.session.FakeLogoutUseCase
import com.divinelink.core.ui.TestTags
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
import com.divinelink.feature.settings.navigation.about.AboutSettingsRoute
import com.divinelink.feature.settings.navigation.account.AccountSettingsRoute
import com.divinelink.feature.settings.navigation.account.JellyseerrSettingsRoute
import com.divinelink.feature.settings.navigation.appearance.AppearanceSettingsRoute
import com.divinelink.feature.settings.navigation.details.DetailsPreferencesSettingsRoute
import com.divinelink.feature.settings.navigation.links.LinkHandlingSettingsRoute
import com.divinelink.feature.settings.navigation.settings.SettingsRoute
import com.divinelink.feature.tmdb.auth.TMDBAuthRoute
import com.divinelink.feature.tmdb.auth.TMDBAuthViewModel
import com.divinelink.scenepeek.fakes.usecase.FakeFetchMultiInfoSearchUseCase
import com.divinelink.scenepeek.fakes.usecase.FakeGetFavoriteMoviesUseCase
import com.divinelink.scenepeek.fakes.usecase.FakeGetPopularMoviesUseCase
import com.divinelink.scenepeek.fakes.usecase.FakeMarkAsFavoriteUseCase
import com.divinelink.scenepeek.home.ui.HomeViewModel
import com.divinelink.scenepeek.settings.appearance.usecase.material.you.FakeGetMaterialYouVisibleUseCase
import com.divinelink.scenepeek.ui.ScenePeekAppState
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.test.mock.declare
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test

class ScenePeekSettingsNavHostTest : ComposeTest() {

  private lateinit var navController: TestNavHostController
  private lateinit var fakePreferenceStorage: FakePreferenceStorage

  // Home use cases
  private lateinit var popularMoviesUseCase: FakeGetPopularMoviesUseCase
  private lateinit var fetchMultiInfoSearchUseCase: FakeFetchMultiInfoSearchUseCase
  private lateinit var markAsFavoriteUseCase: FakeMarkAsFavoriteUseCase
  private lateinit var getFavoriteMoviesUseCase: FakeGetFavoriteMoviesUseCase

  @get:Rule
  val mainDispatcherRule = MainDispatcherRule()
  private val testDispatcher = mainDispatcherRule.testDispatcher

  @BeforeTest
  fun setup() {
    startKoin {
      androidContext(composeTestRule.activity)
      modules()
    }

    fakePreferenceStorage = FakePreferenceStorage()

    popularMoviesUseCase = FakeGetPopularMoviesUseCase()
    fetchMultiInfoSearchUseCase = FakeFetchMultiInfoSearchUseCase()
    markAsFavoriteUseCase = FakeMarkAsFavoriteUseCase()
    getFavoriteMoviesUseCase = FakeGetFavoriteMoviesUseCase()

    popularMoviesUseCase.mockFetchPopularMovies(Result.success(emptyList()))

    declare {
      HomeViewModel(
        getPopularMoviesUseCase = popularMoviesUseCase.mock,
        fetchMultiInfoSearchUseCase = fetchMultiInfoSearchUseCase.mock,
        markAsFavoriteUseCase = markAsFavoriteUseCase,
        getFavoriteMoviesUseCase = getFavoriteMoviesUseCase.mock,
      )
    }

    setContentWithTheme {
      navController = TestNavHostController(LocalContext.current)
      navController.navigatorProvider.addNavigator(ComposeNavigator())
      ScenePeekNavHost(
        state = ScenePeekAppState(
          navController = navController,
          scope = rememberCoroutineScope(),
          networkMonitor = TestNetworkMonitor(),
        ),
      )
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
