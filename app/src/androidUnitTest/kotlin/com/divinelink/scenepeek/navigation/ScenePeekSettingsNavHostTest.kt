package com.divinelink.scenepeek.navigation

import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.test.ComposeUiTest
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsNotDisplayed
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollToNode
import androidx.compose.ui.test.performTouchInput
import androidx.compose.ui.test.swipeUp
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.compose.ComposeNavigator
import androidx.navigation.compose.DialogNavigator
import androidx.navigation.testing.TestNavHostController
import com.divinelink.core.domain.search.SearchStateManager
import com.divinelink.core.domain.settings.MediaRatingPreferenceUseCase
import com.divinelink.core.domain.theme.GetAvailableThemesUseCase
import com.divinelink.core.domain.theme.GetThemeUseCase
import com.divinelink.core.domain.theme.SetThemeUseCase
import com.divinelink.core.domain.theme.black.backgrounds.GetBlackBackgroundsUseCase
import com.divinelink.core.domain.theme.black.backgrounds.SetBlackBackgroundsUseCase
import com.divinelink.core.domain.theme.material.you.GetMaterialYouUseCase
import com.divinelink.core.domain.theme.material.you.SetMaterialYouUseCase
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
import com.divinelink.core.testing.domain.SystemThemeProviderFactory
import com.divinelink.core.testing.domain.theme.material.you.MaterialYouProviderFactory
import com.divinelink.core.testing.setContentWithTheme
import com.divinelink.core.testing.storage.FakePreferenceStorage
import com.divinelink.core.testing.uiTest
import com.divinelink.core.testing.usecase.FakeCreateRequestTokenUseCase
import com.divinelink.core.testing.usecase.FakeFetchMultiInfoSearchUseCase
import com.divinelink.core.testing.usecase.FakeGetAccountDetailsUseCase
import com.divinelink.core.testing.usecase.FakeGetFavoriteMoviesUseCase
import com.divinelink.core.testing.usecase.FakeGetJellyseerrDetailsUseCase
import com.divinelink.core.testing.usecase.FakeGetPopularMoviesUseCase
import com.divinelink.core.testing.usecase.FakeLoginJellyseerrUseCase
import com.divinelink.core.testing.usecase.FakeLogoutJellyseerrUseCase
import com.divinelink.core.testing.usecase.FakeLogoutUseCase
import com.divinelink.core.testing.usecase.TestCreateSessionUseCase
import com.divinelink.core.testing.usecase.TestMarkAsFavoriteUseCase
import com.divinelink.core.ui.SharedTransitionScopeProvider
import com.divinelink.core.ui.TestTags
import com.divinelink.core.ui.composition.PreviewLocalProvider
import com.divinelink.feature.settings.app.account.AccountSettingsViewModel
import com.divinelink.feature.settings.app.account.jellyseerr.JellyseerrSettingsViewModel
import com.divinelink.feature.settings.app.appearance.AppearanceSettingsViewModel
import com.divinelink.feature.settings.app.details.DetailsPreferencesViewModel
import com.divinelink.feature.settings.resources.feature_settings_about
import com.divinelink.feature.settings.resources.feature_settings_account
import com.divinelink.feature.settings.resources.feature_settings_details_preferences
import com.divinelink.feature.settings.resources.feature_settings_jellyseerr_integration
import com.divinelink.feature.settings.resources.feature_settings_link_handling
import com.divinelink.feature.settings.resources.login
import com.divinelink.feature.settings.resources.preferences__appearance
import com.divinelink.feature.settings.resources.settings
import com.divinelink.feature.tmdb.auth.TMDBAuthViewModel
import com.divinelink.scenepeek.di.appModule
import com.divinelink.scenepeek.di.navigationModule
import com.divinelink.scenepeek.home.ui.HomeViewModel
import com.divinelink.scenepeek.settings.appearance.usecase.material.you.FakeGetMaterialYouVisibleUseCase
import com.google.common.truth.Truth.assertThat
import io.kotest.matchers.shouldBe
import org.jetbrains.compose.resources.getString
import org.junit.Rule
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.test.get
import org.koin.test.mock.declare
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import com.divinelink.feature.settings.resources.Res as R

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
  override fun setUp() {
    startKoin {
      modules(
        appModule,
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

    declare {
      TMDBAuthViewModel(
        createRequestTokenUseCase = FakeCreateRequestTokenUseCase().mock,
        createSessionUseCase = TestCreateSessionUseCase().mock,
      )
    }
  }

  @AfterTest
  fun tearDown() {
    stopKoin()
  }

  private fun ComposeUiTest.setupContent() {
    setContentWithTheme {
      navController = TestNavHostController(LocalContext.current)
      navController.navigatorProvider.addNavigator(ComposeNavigator())
      navController.navigatorProvider.addNavigator(DialogNavigator())
      val state = rememberScenePeekAppState(
        networkMonitor = TestNetworkMonitor(),
        onboardingManager = TestOnboardingManager(),
        navController = navController,
        preferencesRepository = TestPreferencesRepository(),
        navigationProvider = get<List<NavGraphExtension>>(),
      )

      ProvideScenePeekAppState(appState = state) {
        PreviewLocalProvider {
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

  @Test
  fun `test navigate to AccountSettingsScreen`() = uiTest {
    setupContent()

    navController.navigate(SettingsRoute)

    declare {
      AccountSettingsViewModel(
        getAccountDetailsUseCase = FakeGetAccountDetailsUseCase().mock,
        getJellyseerrProfileUseCase = FakeGetJellyseerrDetailsUseCase().mock,
        logoutUseCase = FakeLogoutUseCase().mock,
      )
    }

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

  @Test
  fun `test navigate to JellyseerrAccountSettings`() = uiTest {
    setupContent()

    navController.navigate(SettingsRoute)
    navController.navigate(AccountSettingsRoute)

    declare {
      AccountSettingsViewModel(
        getAccountDetailsUseCase = FakeGetAccountDetailsUseCase().mock,
        getJellyseerrProfileUseCase = FakeGetJellyseerrDetailsUseCase().mock,
        logoutUseCase = FakeLogoutUseCase().mock,
      )
    }

    declare {
      JellyseerrSettingsViewModel(
        logoutJellyseerrUseCase = FakeLogoutJellyseerrUseCase().mock,
        getJellyseerrProfileUseCase = FakeGetJellyseerrDetailsUseCase().mock,
        loginJellyseerrUseCase = FakeLoginJellyseerrUseCase().mock,
      )
    }

    onNodeWithText(getString(R.string.feature_settings_account)).assertIsDisplayed()
    assertThat(navController.currentDestination?.hasRoute(AccountSettingsRoute::class)).isTrue()
    assertThat(
      navController.currentDestination?.hasRoute(JellyseerrSettingsRoute::class),
    ).isFalse()

    onNodeWithText(getString(R.string.feature_settings_jellyseerr_integration)).assertExists()
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

  @Test
  fun `test navigate to AppearanceSettingsScreen`() = uiTest {
    setupContent()

    navController.navigate(SettingsRoute)

    fakePreferenceStorage = FakePreferenceStorage()
    val fakeGetMaterialYouVisibleUseCase = FakeGetMaterialYouVisibleUseCase(
      materialYouProvider = MaterialYouProviderFactory.available,
      dispatcher = testDispatcher,
    )

    fakeGetMaterialYouVisibleUseCase.mockMaterialYouVisible(true)

    declare {
      AppearanceSettingsViewModel(
        setThemeUseCase = SetThemeUseCase(fakePreferenceStorage, testDispatcher),
        getThemeUseCase = GetThemeUseCase(fakePreferenceStorage, testDispatcher),
        getAvailableThemesUseCase = GetAvailableThemesUseCase(
          systemThemeProvider = SystemThemeProviderFactory.available,
          dispatcher = testDispatcher,
        ),
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

  @Test
  fun `test navigate to Details Preferences`() = uiTest {
    setupContent()

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

  @Test
  fun `test navigate to Link Handling`() = uiTest {
    setupContent()

    navController.navigate(SettingsRoute)

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

  @Test
  fun `test navigate to AboutSettingsScreen`() = uiTest {
    setupContent()

    navController.navigate(SettingsRoute)

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

  @Test
  fun `test navigate to TMDBAuthScreen`() = uiTest {
    setupContent()

    declare {
      AccountSettingsViewModel(
        getAccountDetailsUseCase = FakeGetAccountDetailsUseCase().mock,
        getJellyseerrProfileUseCase = FakeGetJellyseerrDetailsUseCase().mock,
        logoutUseCase = FakeLogoutUseCase().mock,
      )
    }

    navController.navigate(AccountSettingsRoute)

    navController.currentDestination?.hasRoute(AccountSettingsRoute::class) shouldBe true
    navController.currentDestination?.hasRoute(TMDBAuthRoute::class) shouldBe false

    onNodeWithText(getString(R.string.login)).assertExists().performClick()

    onNodeWithText(getString(R.string.settings)).assertIsNotDisplayed()
    navController.currentDestination?.hasRoute(TMDBAuthRoute::class) shouldBe true
  }
}
