package com.andreolas.movierama

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.andreolas.movierama.destinations.HomeScreenDestination
import com.andreolas.movierama.home.ui.HomeScreen
import com.andreolas.movierama.home.ui.LoadingContent
import com.andreolas.movierama.navigation.TopLevelDestination
import com.andreolas.movierama.ui.AppNavigationBar
import com.andreolas.movierama.ui.components.snackbar.controller.ProvideSnackbarController
import com.divinelink.core.designsystem.theme.AppTheme
import com.divinelink.core.designsystem.theme.Theme
import com.google.accompanist.navigation.material.ExperimentalMaterialNavigationApi
import com.ramcosta.composedestinations.DestinationsNavHost
import com.ramcosta.composedestinations.animations.defaults.RootNavGraphDefaultAnimations
import com.ramcosta.composedestinations.animations.rememberAnimatedNavHostEngine
import com.ramcosta.composedestinations.manualcomposablecalls.composable
import com.ramcosta.composedestinations.rememberNavHostEngine
import com.ramcosta.composedestinations.spec.NavHostEngine
import com.ramcosta.composedestinations.spec.Route
import dagger.hilt.android.AndroidEntryPoint

@ExperimentalAnimationApi
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@AndroidEntryPoint
class MainActivity : ComponentActivity() {

  private val viewModel: MainViewModel by viewModels()

  @OptIn(ExperimentalMaterialNavigationApi::class)
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    setContent {
      val snackbarHostState = remember { SnackbarHostState() }
      val coroutineScope = rememberCoroutineScope()

      val engine = rememberAnimatedNavHostEngine(
        rootDefaultAnimations = RootNavGraphDefaultAnimations(
          enterTransition = {
            slideInHorizontally()
          },
          exitTransition = {
            fadeOut()
          },
        ),
      )
      val navController: NavHostController = engine.rememberNavController()

      val darkTheme = shouldUseDarkTheme(
        uiState = viewModel.viewState.collectAsState().value,
        selectedTheme = viewModel.theme.collectAsState().value
      )

      AppTheme(
        useDarkTheme = darkTheme,
        dynamicColor = viewModel.materialYou.collectAsState().value,
        blackBackground = viewModel.blackBackgrounds.collectAsState().value,
      ) {
        ProvideSnackbarController(
          snackbarHostState = snackbarHostState,
          coroutineScope = coroutineScope
        ) {
          Scaffold(
            bottomBar = {
              AppNavigationBar {
                val currentDestination: NavDestination? =
                  navController.currentBackStackEntryAsState().value?.destination

                TopLevelDestination.entries.forEach { destination ->
                  val selected = currentDestination.isTopLevelDestinationInHierarchy(destination)

                  NavigationBarItem(
                    selected = selected,
                    onClick = {
                      navController.navigate(destination.destination.route)
                    },
                    label = {
                      Text(text = stringResource(id = destination.titleTextId))
                    },
                    icon = {
                      if (selected) {
                        Icon(
                          imageVector = destination.selectedIcon,
                          contentDescription = null
                        )
                      } else {
                        Icon(
                          imageVector = destination.unselectedIcon,
                          contentDescription = null
                        )
                      }
                    }
                  )
                }
              }
            },
            snackbarHost = { SnackbarHost(hostState = snackbarHostState) }) {
            val mainState = viewModel.viewState.collectAsState()

            when (mainState.value) {
              is MainViewState.Completed -> AppNavHost(
                navController = navController,
                startRoute = HomeScreenDestination,
                engine = engine
              )
              MainViewState.Loading -> LoadingContent()
            }
          }
        }
      }
    }
  }
}

@Composable
private fun shouldUseDarkTheme(
  uiState: MainViewState,
  selectedTheme: Theme,
): Boolean = when (uiState) {
  is MainViewState.Loading -> isSystemInDarkTheme()
  is MainViewState.Completed -> when (selectedTheme) {
    Theme.SYSTEM -> isSystemInDarkTheme()
    Theme.LIGHT -> false
    Theme.DARK -> true
  }
}

@Composable
private fun AppNavHost(
  startRoute: Route,
  navController: NavHostController,
  engine: NavHostEngine = rememberNavHostEngine(),
) {
  DestinationsNavHost(
    startRoute = startRoute,
    navController = navController,
    navGraph = NavGraphs.root,
    engine = engine,
    manualComposableCallsBuilder = {
      composable(HomeScreenDestination) {
        HomeScreen(
          navigator = destinationsNavigator,
        )
      }
    }
  )
}

private fun NavDestination?.isTopLevelDestinationInHierarchy(
  destination: TopLevelDestination
) = this?.hierarchy?.any {
  it.route?.contains(destination.destination.route, true) ?: false
} ?: false
