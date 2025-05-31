package com.divinelink.core.scaffold

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition

import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationRail
import androidx.compose.material3.NavigationRailItem
import androidx.compose.material3.NavigationRailItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.divinelink.core.designsystem.component.ScenePeekNavigationDefaults

@Composable
fun ScaffoldState.PersistentNavigationRail(
  modifier: Modifier = Modifier,
  enterTransition: EnterTransition = slideInVertically(initialOffsetY = { it }),
  exitTransition: ExitTransition = slideOutVertically(targetOffsetY = { it }),
) {
  AnimatedVisibility(
    modifier = modifier
      .sharedElement(
        sharedContentState = rememberSharedContentState(
          NavRailSharedElementKey,
        ),
        animatedVisibilityScope = this,
      ),
    visible = canShowNavRail,
    enter = enterTransition,
    exit = exitTransition,
    content = {
      val state = LocalScenePeekAppState.current
      NavigationRail {
        state.topLevelDestinations.forEach { destination ->
          val selected = state.currentDestination.isRouteInHierarchy(destination.route::class)

          NavigationRailItem(
            selected = selected,
            onClick = { state.navigateToTopLevelDestination(destination) },
            colors = NavigationRailItemDefaults.colors(
              selectedIconColor = ScenePeekNavigationDefaults.selectedItemColor(),
              unselectedIconColor = ScenePeekNavigationDefaults.contentColor(),
              selectedTextColor = ScenePeekNavigationDefaults.selectedTextColor(),
              unselectedTextColor = ScenePeekNavigationDefaults.contentColor(),
              indicatorColor = ScenePeekNavigationDefaults.indicatorColor(),
            ),
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

private data object NavRailSharedElementKey
