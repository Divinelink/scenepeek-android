package com.divinelink.core.scaffold

import androidx.compose.animation.SharedTransitionScope
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.navigation3.runtime.rememberNavBackStack
import com.divinelink.core.data.app.AppInfoRepository
import com.divinelink.core.data.network.NetworkMonitor
import com.divinelink.core.data.preferences.PreferencesRepository
import com.divinelink.core.designsystem.theme.model.ThemePreferences
import com.divinelink.core.domain.onboarding.OnboardingManager
import com.divinelink.core.model.preferences.DetailPreferences
import com.divinelink.core.model.search.SearchEntryPoint
import com.divinelink.core.model.ui.UiPreferences
import com.divinelink.core.navigation.route.Navigation
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
  navigationProvider: List<NavEntryProvider>,
  scope: CoroutineScope = rememberCoroutineScope(),
  snackbarHostState: SnackbarHostState = remember { SnackbarHostState() },
): ScenePeekAppState = remember(networkMonitor, scope) {
  ScenePeekAppState(
    scope = scope,
    navigationExtension = navigationProvider,
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
  val navigationExtension: List<NavEntryProvider>,
  val snackbarHostState: SnackbarHostState,
  preferencesRepository: PreferencesRepository,
  appInfoRepository: AppInfoRepository,
  networkMonitor: NetworkMonitor,
  onboardingManager: OnboardingManager,
) {
  private val homeBackStack = mutableStateListOf<Navigation>(Navigation.HomeRoute)
  private val profileBackStack = mutableStateListOf<Navigation>(Navigation.ProfileRoute)
  private val searchBackStack = mutableStateListOf<Navigation>(
    Navigation.SearchRoute(entryPoint = SearchEntryPoint.SEARCH_TAB),
  )

  var currentTab: TopLevelDestination by mutableStateOf(TopLevelDestination.HOME)
    private set

  val backStack: SnapshotStateList<Navigation>
    get() = when (currentTab) {
      TopLevelDestination.HOME -> homeBackStack
      TopLevelDestination.PROFILE -> profileBackStack
      TopLevelDestination.SEARCH -> searchBackStack
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
      Navigation.Back -> navigateBack()
      Navigation.TwiceBack -> {
        navigateBack()
        navigateBack()
      }
      Navigation.HomeRoute -> navigateToTopLevelDestination(TopLevelDestination.HOME)
      Navigation.ProfileRoute -> navigateToTopLevelDestination(TopLevelDestination.PROFILE)
      is Navigation.SearchRoute -> when (route.entryPoint) {
        SearchEntryPoint.SEARCH_TAB -> navigateToTopLevelDestination(TopLevelDestination.SEARCH)
        SearchEntryPoint.HOME -> backStack.add(route)
      }
      Navigation.ListsRoute -> {
        val idx = backStack.indexOfLast { it == Navigation.ListsRoute }
        if (idx >= 0) {
          repeat(backStack.size - idx - 1) { backStack.removeLast() }
        } else {
          backStack.add(route)
        }
      }
      else -> backStack.add(route)
    }
  }

  fun navigateBack() {
    if (backStack.size > 1) {
      backStack.removeLastOrNull()
    }
  }

  fun navigateToTopLevelDestination(destination: TopLevelDestination) {
    if (currentTab == destination) {
      val stack = backStack
      if (stack.size > 1) {
        val root = stack.first()
        stack.clear()
        stack.add(root)
      }
    } else {
      currentTab = destination
    }
  }

  companion object {
    val SUBSCRIPTION_TIMEOUT = 5.seconds.inWholeMilliseconds
  }
}
