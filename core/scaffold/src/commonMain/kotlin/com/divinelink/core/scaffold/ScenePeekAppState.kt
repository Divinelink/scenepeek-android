package com.divinelink.core.scaffold

import androidx.compose.animation.SharedTransitionScope
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.State
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import com.divinelink.core.data.app.AppInfoRepository
import com.divinelink.core.data.network.NetworkMonitor
import com.divinelink.core.data.preferences.PreferencesRepository
import com.divinelink.core.designsystem.theme.model.ThemePreferences
import com.divinelink.core.domain.onboarding.OnboardingManager
import com.divinelink.core.model.preferences.DetailPreferences
import com.divinelink.core.model.ui.UiPreferences
import com.divinelink.core.navigation.Navigator
import com.divinelink.core.navigation.route.Navigation
import com.divinelink.core.navigation.utilities.isSameDestinationType
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlin.time.Duration.Companion.seconds

@Composable
fun rememberScenePeekAppState(
  networkMonitor: NetworkMonitor,
  onboardingManager: OnboardingManager,
  preferencesRepository: PreferencesRepository,
  appInfoRepository: AppInfoRepository,
  navigator: Navigator,
  scope: CoroutineScope = rememberCoroutineScope(),
  snackbarHostState: SnackbarHostState = remember { SnackbarHostState() },
): ScenePeekAppState = remember(networkMonitor, scope) {
  ScenePeekAppState(
    scope = scope,
    navigator = navigator,
    networkMonitor = networkMonitor,
    onboardingManager = onboardingManager,
    snackbarHostState = snackbarHostState,
    appInfoRepository = appInfoRepository,
    preferencesRepository = preferencesRepository,
  )
}

@Stable
class ScenePeekAppState internal constructor(
  val scope: CoroutineScope,
  val navigator: Navigator,
  val snackbarHostState: SnackbarHostState,
  preferencesRepository: PreferencesRepository,
  appInfoRepository: AppInfoRepository,
  networkMonitor: NetworkMonitor,
  onboardingManager: OnboardingManager,
) {
  val backStack = navigator.backStack
  val currentTab: State<TopLevelDestination?> = derivedStateOf {
    getCurrentTopLevelDestination(backStack)
  }

  val isOffline = networkMonitor.isOnline
    .map(Boolean::not)
    .stateIn(
      scope = scope,
      started = SharingStarted.WhileSubscribed(SUBSCRIPTION_TIMEOUT),
      initialValue = false,
    )

  val shouldShowOnboarding = onboardingManager.showIntro
    .stateIn(
      scope = scope,
      started = SharingStarted.WhileSubscribed(SUBSCRIPTION_TIMEOUT),
      initialValue = false,
    )

  val updateAvailable = appInfoRepository.updateAvailable.stateIn(
    scope = scope,
    started = SharingStarted.WhileSubscribed(SUBSCRIPTION_TIMEOUT),
    initialValue = null,
  )

  val isInitialOnboarding = onboardingManager.isInitialOnboarding
    .stateIn(
      scope = scope,
      started = SharingStarted.WhileSubscribed(SUBSCRIPTION_TIMEOUT),
      initialValue = false,
    )

  val uiPreferences = preferencesRepository.uiPreferences.stateIn(
    scope = scope,
    started = SharingStarted.WhileSubscribed(SUBSCRIPTION_TIMEOUT),
    initialValue = UiPreferences.Initial,
  )

  val themePreferences = preferencesRepository.themePreferences.stateIn(
    scope = scope,
    started = SharingStarted.WhileSubscribed(SUBSCRIPTION_TIMEOUT),
    initialValue = ThemePreferences.initial,
  )

  val detailPreferences = preferencesRepository.detailPreferences.stateIn(
    scope = scope,
    started = SharingStarted.WhileSubscribed(SUBSCRIPTION_TIMEOUT),
    initialValue = DetailPreferences.initial,
  )

  val topLevelDestinations: List<TopLevelDestination> = TopLevelDestination.entries

  lateinit var sharedTransitionScope: SharedTransitionScope

  fun navigate(route: Navigation) {
    when (route) {
      Navigation.Back -> navigator.goBack()
      Navigation.TwiceBack -> {
        navigator.goBack()
        navigator.goBack()
      }
      Navigation.HomeRoute -> navigateToTopLevelDestination(TopLevelDestination.HOME)
      Navigation.ProfileRoute -> navigateToTopLevelDestination(TopLevelDestination.PROFILE)
      is Navigation.SearchRoute -> navigateToTopLevelDestination(TopLevelDestination.SEARCH)
      Navigation.ListsRoute -> {
        val idx = navigator.backStack.indexOfLast { it == Navigation.ListsRoute }
        if (idx >= 0) {
          repeat(navigator.backStack.size - idx - 1) { navigator.backStack.removeLast() }
        } else {
          navigator.navigate(route)
        }
      }
      else -> navigator.navigate(route)
    }
  }

  fun navigateToTopLevelDestination(destination: TopLevelDestination) {
    if (currentTab.value?.route?.isSameDestinationType(destination.route) == true) return

    if (backStack.size > 1) {
      navigator.clear()
    }

    navigator.navigate(destination.route)
  }

  private fun getCurrentTopLevelDestination(backStack: List<Navigation>): TopLevelDestination? {
    return backStack.lastOrNull()?.let { entry ->
      when (entry) {
        is Navigation.HomeRoute -> TopLevelDestination.HOME
        is Navigation.SearchRoute -> TopLevelDestination.SEARCH
        is Navigation.ProfileRoute -> TopLevelDestination.PROFILE
        else -> null
      }
    }
  }

  companion object {
    val SUBSCRIPTION_TIMEOUT = 5.seconds.inWholeMilliseconds
  }
}
