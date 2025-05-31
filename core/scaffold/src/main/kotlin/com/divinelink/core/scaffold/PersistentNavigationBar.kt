package com.divinelink.core.scaffold

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBarDefaults
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.divinelink.core.designsystem.theme.dimensions
import com.divinelink.core.ui.navigation.ScenePeekNavigationBar

@Composable
fun ScaffoldState.PersistentNavigationBar(
  modifier: Modifier = Modifier,
  enterTransition: EnterTransition = slideInVertically(initialOffsetY = { it }),
  exitTransition: ExitTransition = slideOutVertically(targetOffsetY = { it }),
) {
  val isOffline by state.isOffline.collectAsStateWithLifecycle()

  AnimatedVisibility(
    modifier = modifier
      .sharedElement(
        sharedContentState = rememberSharedContentState(
          BottomNavSharedElementKey,
        ),
        animatedVisibilityScope = this,
      ),
    visible = canShowBottomNavigation,
    enter = enterTransition,
    exit = exitTransition,
    content = {
      val state = LocalScenePeekAppState.current

      ScenePeekNavigationBar(
        windowInsets = if (isOffline) {
          WindowInsets(bottom = MaterialTheme.dimensions.keyline_8)
        } else {
          NavigationBarDefaults.windowInsets
        },
      ) {
        state.topLevelDestinations.forEach { destination ->
          val selected = state.currentDestination.isRouteInHierarchy(destination.route::class)

          NavigationBarItem(
            selected = selected,
            onClick = { state.navigateToTopLevelDestination(destination) },
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
  )
}

private data object BottomNavSharedElementKey
