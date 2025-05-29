package com.divinelink.core.scaffold

import androidx.compose.animation.animateBounds
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.divinelink.core.designsystem.theme.LocalBottomNavigationPadding

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
    modifier = modifier,
    navigationRail = navigationRail,
    content = {
      Scaffold(
        modifier = modifier
          .animateBounds(lookaheadScope = this),
        topBar = {
          topBar()
        },
        floatingActionButton = {
          floatingActionButton()
        },
        bottomBar = {
          navigationBar()
        },
        snackbarHost = {
          PersistentSnackbarHost()
        },
        content = { paddingValues ->
          CompositionLocalProvider(
            LocalBottomNavigationPadding provides paddingValues.calculateBottomPadding(),
          ) {
            content(paddingValues)
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
          .widthIn(max = 80.dp)
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
