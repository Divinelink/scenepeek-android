package com.andreolas.movierama.ui

import android.annotation.SuppressLint
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.andreolas.movierama.MainViewState
import com.andreolas.movierama.home.ui.LoadingContent
import com.andreolas.movierama.navigation.AppNavHost
import com.andreolas.movierama.navigation.TopLevelDestination
import com.andreolas.movierama.ui.components.snackbar.controller.ProvideSnackbarController
import com.ramcosta.composedestinations.generated.destinations.HomeScreenDestination
import com.ramcosta.composedestinations.utils.rememberDestinationsNavigator

@Composable
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
fun MovieApp(
  uiState: MainViewState
) {
  val navController: NavHostController = rememberNavController()
  val navigator = navController.rememberDestinationsNavigator()

  val snackbarHostState = remember { SnackbarHostState() }
  val coroutineScope = rememberCoroutineScope()

  ProvideSnackbarController(
    snackbarHostState = snackbarHostState,
    coroutineScope = coroutineScope
  ) {
    Scaffold(
      snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
      bottomBar = {
        AppNavigationBar {
          val currentDestination: NavDestination? =
            navController.currentBackStackEntryAsState().value?.destination

          TopLevelDestination.entries.forEach { destination ->
            val selected = currentDestination.isTopLevelDestinationInHierarchy(destination)

            NavigationBarItem(
              selected = selected,
              onClick = { navigator.navigate(destination.destination) },
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
      }
    ) {
      when (uiState) {
        is MainViewState.Completed -> AppNavHost(
          navController = navController,
          startRoute = HomeScreenDestination,
        )
        MainViewState.Loading -> LoadingContent()
      }
    }
  }
}

fun NavDestination?.isTopLevelDestinationInHierarchy(
  destination: TopLevelDestination
) = this?.hierarchy?.any {
  it.route?.contains(destination.destination.route, true) ?: false
} ?: false
