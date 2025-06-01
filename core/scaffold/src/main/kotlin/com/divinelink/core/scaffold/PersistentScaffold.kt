package com.divinelink.core.scaffold

import androidx.compose.animation.animateBounds
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.zIndex
import com.divinelink.core.designsystem.theme.LocalBottomNavigationPadding
import com.divinelink.core.ui.TestTags

/**
 * A persistent [Scaffold] that will render the various slots for any given screen,
 * allowing each screen to hold persistent UI elements such as [PersistentNavigationBar]
 * or [PersistentNavigationRail].
 */
@Composable
fun ScaffoldState.PersistentScaffold(
  modifier: Modifier = Modifier,
  topBar: @Composable ScaffoldState.() -> Unit = {},
  floatingActionButton: @Composable ScaffoldState.() -> Unit = {},
  navigationBar: @Composable ScaffoldState.() -> Unit = {},
  navigationRail: @Composable ScaffoldState.() -> Unit = {},
  content: @Composable ScaffoldState.(PaddingValues) -> Unit,
) {
  NavigationRailScaffold(
    modifier = modifier.testTag(TestTags.Components.PERSISTENT_SCAFFOLD),
    navigationRail = navigationRail,
    content = {
      Scaffold(
        modifier = Modifier
          .animateBounds(lookaheadScope = this),
        topBar = {
          topBar()
        },
        floatingActionButton = {
          floatingActionButton()
        },
        bottomBar = {
          Column(
            modifier = Modifier.fillMaxWidth(),
          ) {
            if (canShowBottomNavigation) {
              PersistentSnackbarHost(
                modifier = Modifier.animateContentSize(),
              )
            }
            navigationBar()
          }
        },
        content = { paddingValues ->
          CompositionLocalProvider(
            LocalBottomNavigationPadding provides paddingValues.calculateBottomPadding(),
          ) {
            Box(modifier = Modifier.fillMaxSize()) {
              content(paddingValues)

              if (canShowNavRail) {
                PersistentSnackbarHost(modifier = Modifier.align(Alignment.BottomCenter))
              }
            }
          }
        },
      )
    },
  )
}

@Composable
private inline fun ScaffoldState.NavigationRailScaffold(
  modifier: Modifier = Modifier,
  navigationRail: @Composable ScaffoldState.() -> Unit,
  content: @Composable () -> Unit,
) {
  Row(
    modifier = modifier,
    content = {
      Box(
        modifier = Modifier
          .zIndex(2F),
      ) {
        navigationRail()
      }
      Box(
        modifier = Modifier
          .fillMaxSize()
          .zIndex(1F),
      ) {
        content()
      }
    },
  )
}
