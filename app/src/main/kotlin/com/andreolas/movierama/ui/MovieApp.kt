package com.andreolas.movierama.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.andreolas.movierama.MainUiEvent
import com.andreolas.movierama.MainUiState
import com.andreolas.movierama.R
import com.andreolas.movierama.navigation.AppNavHost
import com.andreolas.movierama.navigation.TopLevelDestination
import com.divinelink.core.ui.components.LoadingContent
import com.divinelink.core.ui.snackbar.controller.ProvideSnackbarController
import com.divinelink.feature.details.screens.destinations.DetailsScreenDestination
import com.divinelink.feature.details.screens.destinations.PersonScreenDestination
import com.divinelink.ui.screens.destinations.HomeScreenDestination
import com.ramcosta.composedestinations.utils.navGraph
import com.ramcosta.composedestinations.utils.rememberDestinationsNavigator

@Composable
fun MovieApp(
  uiState: MainUiState,
  uiEvent: MainUiEvent,
  onConsumeEvent: () -> Unit,
) {
  val navController: NavHostController = rememberNavController()
  val navigator = navController.rememberDestinationsNavigator()

  val snackbarHostState = remember { SnackbarHostState() }
  val coroutineScope = rememberCoroutineScope()

  LaunchedEffect(uiEvent) {
    when (uiEvent) {
      is MainUiEvent.NavigateToDetails -> {
        navigator.navigate(direction = DetailsScreenDestination(uiEvent.navArgs))
        onConsumeEvent()
      }
      is MainUiEvent.NavigateToPersonDetails -> {
        navigator.navigate(direction = PersonScreenDestination(uiEvent.navArgs))
        onConsumeEvent()
      }
      MainUiEvent.None -> {
        // Do nothing
      }
    }
  }

  ProvideSnackbarController(
    snackbarHostState = snackbarHostState,
    coroutineScope = coroutineScope,
  ) {
    Scaffold(
      containerColor = Color.Transparent,
      snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
      bottomBar = {
        AppNavigationBar {
          val currentDestination: NavDestination? =
            navController.currentBackStackEntryAsState().value?.destination

          TopLevelDestination.entries.forEach { destination ->
            val selected = currentDestination.isTopLevelDestinationInHierarchy(destination)

            NavigationBarItem(
              selected = selected,
              onClick = {
                navigator.navigate(
                  direction = destination.direction,
                  builder = {
                    // Pop up to the start destination of the nav graph when navigating up.
                    // This is to avoid building up a large stack of destinations.
                    popUpTo(navController.navGraph.startRoute) {
                      saveState = true
                    }
                    // Avoid multiple copies of the same destination when re-selecting the same item
                    launchSingleTop = true
                    // Restore state when re-selecting a previously selected item
                    restoreState = true
                  },
                )
                navigator.popBackStack(
                  route = destination.direction,
                  inclusive = false,
                )
              },
              label = {
                Text(text = stringResource(id = destination.titleTextId))
              },
              icon = {
                if (selected) {
                  Icon(
                    imageVector = destination.selectedIcon,
                    contentDescription = stringResource(
                      id = R.string.top_level_navigation_content_description_selected,
                      stringResource(id = destination.titleTextId),
                    ),
                  )
                } else {
                  Icon(
                    imageVector = destination.unselectedIcon,
                    contentDescription = stringResource(
                      id = R.string.top_level_navigation_content_description_unselected,
                      stringResource(id = destination.titleTextId),
                    ),
                  )
                }
              },
            )
          }
        }
      },
    ) {
      Surface(
        modifier = Modifier
          .fillMaxSize()
          .padding(it),
      ) {
        when (uiState) {
          is MainUiState.Completed -> AppNavHost(
            navController = navController,
            startRoute = HomeScreenDestination,
          )
          MainUiState.Loading -> LoadingContent()
        }
      }
    }
  }
}

fun NavDestination?.isTopLevelDestinationInHierarchy(destination: TopLevelDestination) =
  this?.hierarchy?.any {
    it.route?.contains(destination.direction.route, true) ?: false
  } ?: false
