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
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.divinelink.core.designsystem.component.ScenePeekNavigationDefaults
import com.divinelink.core.designsystem.theme.dimensions
import com.divinelink.core.scaffold.resources.Res
import com.divinelink.core.scaffold.resources.top_level_navigation_content_description_selected
import com.divinelink.core.scaffold.resources.top_level_navigation_content_description_unselected
import com.divinelink.core.ui.TestTags
import com.divinelink.core.ui.extension.format
import com.divinelink.core.ui.navigation.ScenePeekNavigationBar
import org.jetbrains.compose.resources.stringResource

@Composable
fun ScaffoldState.PersistentNavigationBar(
  modifier: Modifier = Modifier,
  enterTransition: EnterTransition = slideInVertically(initialOffsetY = { it }),
  exitTransition: ExitTransition = slideOutVertically(targetOffsetY = { it }),
  onNavItemReselected: () -> Boolean = { false },
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
            modifier = Modifier.testTag(TestTags.TOP_LEVEL_DESTINATION.format(destination)),
            selected = selected,
            colors = NavigationBarItemDefaults.colors(
              selectedIconColor = ScenePeekNavigationDefaults.selectedItemColor(),
              unselectedIconColor = ScenePeekNavigationDefaults.contentColor(),
              selectedTextColor = ScenePeekNavigationDefaults.selectedTextColor(),
              unselectedTextColor = ScenePeekNavigationDefaults.contentColor(),
              indicatorColor = ScenePeekNavigationDefaults.indicatorColor(),
            ),
            onClick = {
              if (selected && onNavItemReselected()) return@NavigationBarItem

              state.navigateToTopLevelDestination(destination)
            },
            label = {
              Text(
                text = stringResource(destination.titleTextId),
                style = MaterialTheme.typography.labelSmall,
              )
            },
            icon = {
              if (selected) {
                Icon(
                  imageVector = destination.selectedIcon,
                  contentDescription = stringResource(
                    Res.string.top_level_navigation_content_description_selected,
                    stringResource(destination.titleTextId),
                  ),
                )
              } else {
                Icon(
                  imageVector = destination.unselectedIcon,
                  contentDescription = stringResource(
                    Res.string.top_level_navigation_content_description_unselected,
                    stringResource(destination.titleTextId),
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
