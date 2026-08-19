package com.divinelink.core.scaffold

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.scene.DialogSceneStrategy
import androidx.navigation3.scene.SinglePaneSceneStrategy
import androidx.navigation3.ui.NavDisplay
import org.koin.compose.navigation3.koinEntryProvider
import org.koin.core.annotation.KoinExperimentalAPI

@OptIn(KoinExperimentalAPI::class)
@Composable
fun ScenePeekNavHost() {
  val state = LocalScenePeekAppState.current
  val twoPaneStrategy = rememberTwoPaneSceneStrategy<NavKey>()

  NavDisplay(
    backStack = state.backStack,
    entryDecorators = listOf(
      rememberSaveableStateHolderNavEntryDecorator(),
      rememberViewModelStoreNavEntryDecorator(),
    ),
    sceneStrategies = listOf(
      DialogSceneStrategy(),
      twoPaneStrategy,
      SinglePaneSceneStrategy(),
    ),
    entryProvider = koinEntryProvider(),
    transitionSpec = { navigationSpec() },
    popTransitionSpec = { navigationSpec() },
    predictivePopTransitionSpec = { navigationSpec() },
  )
}

private fun navigationSpec() = fadeIn(
  animationSpec = tween(durationMillis = 300, easing = LinearEasing),
) togetherWith fadeOut(
  animationSpec = tween(durationMillis = 300, easing = LinearEasing),
)
