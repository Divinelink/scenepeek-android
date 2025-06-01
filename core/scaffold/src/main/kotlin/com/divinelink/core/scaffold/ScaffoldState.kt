package com.divinelink.core.scaffold

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Density
import androidx.window.core.layout.WindowWidthSizeClass

class ScaffoldState internal constructor(
  density: Density,
  animatedVisibilityScope: AnimatedVisibilityScope,
  internal val isMediumScreenWidthOrWider: State<Boolean>,
  internal val state: ScenePeekAppState,
) : AnimatedVisibilityScope by animatedVisibilityScope,
  SharedTransitionScope by state.sharedTransitionScope {
  val canShowBottomNavigation get() = !isMediumScreenWidthOrWider.value

  val canShowNavRail get() = isMediumScreenWidthOrWider.value

  internal val canShowFab = true

  internal var density by mutableStateOf(density)
}

@Composable
fun rememberScaffoldState(animatedVisibilityScope: AnimatedVisibilityScope): ScaffoldState {
  val density = LocalDensity.current
  val state = LocalScenePeekAppState.current
  val isMediumScreenWidthOrWider = isMediumScreenWidthOrWider()

  return remember {
    ScaffoldState(
      animatedVisibilityScope = animatedVisibilityScope,
      isMediumScreenWidthOrWider = isMediumScreenWidthOrWider,
      state = state,
      density = density,
    )
  }
}

@Composable
private fun isMediumScreenWidthOrWider(): State<Boolean> {
  val widthSizeClass = currentWindowAdaptiveInfo()
    .windowSizeClass
    .windowWidthSizeClass

  val mediumOrHigherClasses = listOf(
    WindowWidthSizeClass.MEDIUM,
    WindowWidthSizeClass.EXPANDED,
  )

  val isMediumScreenWidthOrWider: Boolean = (widthSizeClass in mediumOrHigherClasses)

  return rememberUpdatedState(isMediumScreenWidthOrWider)
}

@Composable
fun ProvideScenePeekAppState(
  appState: ScenePeekAppState,
  content: @Composable () -> Unit,
) {
  CompositionLocalProvider(
    LocalScenePeekAppState provides appState,
    content = content,
  )
}

internal val LocalScenePeekAppState = staticCompositionLocalOf<ScenePeekAppState> {
  throw IllegalArgumentException("AppState must be provided in the app scaffolding.")
}
