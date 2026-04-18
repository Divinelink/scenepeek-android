package com.divinelink.scenepeek.navigation

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
import com.divinelink.core.domain.search.SearchStateManager
import com.divinelink.core.domain.settings.MediaRatingPreferenceUseCase
import com.divinelink.core.domain.theme.GetAvailableColorSystemsUseCase
import com.divinelink.core.domain.theme.GetAvailableThemesUseCase
import com.divinelink.core.fixtures.core.commons.ClockFactory
import com.divinelink.core.fixtures.core.data.network.TestNetworkMonitor
import com.divinelink.core.fixtures.data.app.TestAppInfoRepository
import com.divinelink.core.fixtures.data.preferences.TestPreferencesRepository
import com.divinelink.core.fixtures.manager.TestOnboardingManager
import com.divinelink.core.navigation.Navigator
import com.divinelink.core.navigation.route.Navigation
import com.divinelink.core.navigation.route.Navigation.AboutSettingsRoute
import com.divinelink.core.navigation.route.Navigation.AccountSettingsRoute
import com.divinelink.core.navigation.route.Navigation.AppearanceSettingsRoute
import com.divinelink.core.navigation.route.Navigation.DetailsPreferencesSettingsRoute
import com.divinelink.core.navigation.route.Navigation.JellyseerrSettingsRoute
import com.divinelink.core.navigation.route.Navigation.LinkHandlingSettingsRoute
import com.divinelink.core.navigation.route.Navigation.SettingsRoute
import com.divinelink.core.navigation.route.Navigation.TMDBAuthRoute
import com.divinelink.core.scaffold.ProvideScenePeekAppState
import com.divinelink.core.scaffold.ScenePeekAppState
import com.divinelink.core.scaffold.ScenePeekNavHost
import com.divinelink.core.scaffold.rememberScenePeekAppState
import com.divinelink.core.testing.ComposeTest
import com.divinelink.core.testing.MainDispatcherRule
import com.divinelink.core.testing.domain.SystemThemeProviderFactory
import com.divinelink.core.testing.domain.theme.material.you.MaterialYouProviderFactory
import com.divinelink.core.testing.repository.TestMediaRepository
import com.divinelink.core.testing.setContentWithTheme
import com.divinelink.core.testing.storage.FakePreferenceStorage
import com.divinelink.core.testing.uiTest
import com.divinelink.core.testing.usecase.FakeCreateRequestTokenUseCase
import com.divinelink.core.testing.usecase.FakeFetchMultiInfoSearchUseCase
import com.divinelink.core.testing.usecase.FakeGetAccountDetailsUseCase
import com.divinelink.core.testing.usecase.FakeGetJellyseerrDetailsUseCase
import com.divinelink.core.testing.usecase.FakeLoginJellyseerrUseCase
import com.divinelink.core.testing.usecase.FakeLogoutJellyseerrUseCase
import com.divinelink.core.testing.usecase.FakeLogoutUseCase
import com.divinelink.core.testing.usecase.TestCreateSessionUseCase
import com.divinelink.core.testing.usecase.TestMarkAsFavoriteUseCase
import com.divinelink.core.ui.SharedTransitionScopeProvider
import com.divinelink.core.ui.TestTags
import com.divinelink.core.ui.composition.PreviewLocalProvider
import com.divinelink.feature.home.HomeViewModel
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
import com.google.common.truth.Truth.assertThat
import org.jetbrains.compose.resources.getString
import org.junit.Rule
import org.koin.core.component.get
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.test.mock.declare
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.time.Clock
import com.divinelink.feature.settings.resources.Res as R

class ScenePeekSettingsNavHostTest : ComposeTest() {

  private val clock: Clock = ClockFactory.decemberFirst2021()

  private lateinit var appState: ScenePeekAppState
  private lateinit var fakePreferenceStorage: FakePreferenceStorage
  private lateinit var preferencesRepository: TestPreferencesRepository
  private lateinit var mediaRepository: TestMediaRepository
  private lateinit var navigator: Navigator

  // HOME use cases
  private lateinit var fetchMultiInfoSearchUseCase: FakeFetchMultiInfoSearchUseCase
  private lateinit var markAsFavoriteUseCase: TestMarkAsFavoriteUseCase

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

    navigator = get<Navigator>()

    fakePreferenceStorage = FakePreferenceStorage()
    preferencesRepository = TestPreferencesRepository()
    mediaRepository = TestMediaRepository()

    fetchMultiInfoSearchUseCase = FakeFetchMultiInfoSearchUseCase()
    markAsFavoriteUseCase = TestMarkAsFavoriteUseCase()

    declare {
      HomeViewModel(
        clock = clock,
        repository = mediaRepository.mock,
        markAsFavoriteUseCase = markAsFavoriteUseCase,
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
      val state = rememberScenePeekAppState(
        networkMonitor = TestNetworkMonitor(),
        onboardingManager = TestOnboardingManager(),
        preferencesRepository = preferencesRepository,
        appInfoRepository = TestAppInfoRepository(),
        navigator = navigator,
      )
      appState = state

      ProvideScenePeekAppState(appState = state) {
        PreviewLocalProvider {
          SharedTransitionScopeProvider {
            state.sharedTransitionScope = it

            ScenePeekNavHost()
          }
        }
      }
    }
  }

  @Test
  fun `test navigate to AccountSettingsScreen`() = uiTest {
    setupContent()

    appState.navigate(Navigation.SettingsRoute)

    declare {
      AccountSettingsViewModel(
        getAccountDetailsUseCase = FakeGetAccountDetailsUseCase().mock,
        getJellyseerrProfileUseCase = FakeGetJellyseerrDetailsUseCase().mock,
        logoutUseCase = FakeLogoutUseCase().mock,
      )
    }

    onNodeWithText(getString(R.string.settings)).assertIsDisplayed()
    assertThat(appState.backStack.last()).isEqualTo(SettingsRoute)

    onNodeWithText(getString(R.string.feature_settings_account)).assertExists().performClick()

    onNodeWithText(getString(R.string.settings)).assertDoesNotExist()
    assertThat(appState.backStack.last()).isEqualTo(AccountSettingsRoute)

    onNodeWithTag(TestTags.Settings.NAVIGATION_ICON).performClick()
    onNodeWithText(getString(R.string.settings)).assertIsDisplayed()
    assertThat(appState.backStack.last()).isEqualTo(SettingsRoute)
  }

  @Test
  fun `test navigate to JellyseerrAccountSettings`() = uiTest {
    setupContent()

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

    appState.navigate(SettingsRoute)
    appState.navigate(AccountSettingsRoute)

    onNodeWithText(getString(R.string.feature_settings_account)).assertIsDisplayed()
    assertThat(appState.backStack.last()).isEqualTo(AccountSettingsRoute)

    onNodeWithText(getString(R.string.feature_settings_jellyseerr_integration)).assertExists()
      .performClick()

    onNodeWithText(getString(R.string.feature_settings_account)).assertDoesNotExist()
    assertThat(appState.backStack.last()).isInstanceOf(JellyseerrSettingsRoute::class.java)

    onNodeWithTag(TestTags.Settings.NAVIGATION_ICON).performClick()
    onNodeWithText(getString(R.string.feature_settings_account)).assertIsDisplayed()
    assertThat(appState.backStack.last()).isEqualTo(AccountSettingsRoute)
  }

  @Test
  fun `test navigate to AppearanceSettingsScreen`() = uiTest {
    setupContent()

    fakePreferenceStorage = FakePreferenceStorage()

    declare {
      AppearanceSettingsViewModel(
        getAvailableThemesUseCase = GetAvailableThemesUseCase(
          systemThemeProvider = SystemThemeProviderFactory.available,
          dispatcher = testDispatcher,
        ),
        preferencesRepository = preferencesRepository,
        getAvailableColorPreferences = GetAvailableColorSystemsUseCase(
          materialYouProvider = MaterialYouProviderFactory.available,
          dispatcher = testDispatcher,
        ),
      )
    }

    appState.navigate(SettingsRoute)

    onNodeWithText(getString(R.string.settings)).assertIsDisplayed()
    assertThat(appState.backStack.last()).isEqualTo(SettingsRoute)

    onNodeWithText(getString(R.string.preferences__appearance)).assertExists().performClick()

    assertThat(appState.backStack.last()).isEqualTo(AppearanceSettingsRoute)

    onNodeWithTag(TestTags.Settings.NAVIGATION_ICON).performClick()
    onNodeWithText(getString(R.string.settings)).assertIsDisplayed()
    assertThat(appState.backStack.last()).isEqualTo(SettingsRoute)
  }

  @Test
  fun `test navigate to Details Preferences`() = uiTest {
    setupContent()

    fakePreferenceStorage = FakePreferenceStorage()

    declare {
      DetailsPreferencesViewModel(
        preferencesRepository = preferencesRepository,
        mediaRatingPreferenceUseCase = MediaRatingPreferenceUseCase(
          fakePreferenceStorage,
          testDispatcher,
        ),
      )
    }

    appState.navigate(SettingsRoute)

    onNodeWithText(getString(R.string.settings)).assertIsDisplayed()
    assertThat(appState.backStack.last()).isEqualTo(SettingsRoute)

    onNodeWithText(
      getString(R.string.feature_settings_details_preferences),
    ).assertExists().performClick()

    onNodeWithText(getString(R.string.settings)).assertDoesNotExist()
    assertThat(appState.backStack.last()).isEqualTo(DetailsPreferencesSettingsRoute)

    onNodeWithTag(TestTags.Settings.NAVIGATION_ICON).performClick()
    onNodeWithText(getString(R.string.settings)).assertIsDisplayed()
    assertThat(appState.backStack.last()).isEqualTo(SettingsRoute)
  }

  @Test
  fun `test navigate to Link Handling`() = uiTest {
    setupContent()

    appState.navigate(SettingsRoute)

    onNodeWithText(getString(R.string.settings)).assertIsDisplayed()
    assertThat(appState.backStack.last()).isEqualTo(SettingsRoute)

    onNodeWithText(
      getString(R.string.feature_settings_link_handling),
    ).assertExists().performClick()

    onNodeWithText(getString(R.string.settings)).assertDoesNotExist()
    assertThat(appState.backStack.last()).isEqualTo(LinkHandlingSettingsRoute)

    onNodeWithTag(TestTags.Settings.NAVIGATION_ICON).performClick()
    onNodeWithText(getString(R.string.settings)).assertIsDisplayed()
    assertThat(appState.backStack.last()).isEqualTo(SettingsRoute)
  }

  @Test
  fun `test navigate to AboutSettingsScreen`() = uiTest {
    setupContent()

    appState.navigate(SettingsRoute)

    onNodeWithText(getString(R.string.settings)).assertIsDisplayed()
    assertThat(appState.backStack.last()).isEqualTo(SettingsRoute)

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
    assertThat(appState.backStack.last()).isEqualTo(AboutSettingsRoute)

    onNodeWithTag(TestTags.Settings.NAVIGATION_ICON).performClick()
    onNodeWithText(getString(R.string.settings)).assertIsDisplayed()
    assertThat(appState.backStack.last()).isEqualTo(SettingsRoute)
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

    appState.navigate(AccountSettingsRoute)

    assertThat(appState.backStack.last()).isEqualTo(AccountSettingsRoute)

    onNodeWithText(getString(R.string.login)).assertExists().performClick()

    onNodeWithText(getString(R.string.settings)).assertIsNotDisplayed()
    assertThat(appState.backStack.last()).isEqualTo(TMDBAuthRoute)
  }
}
