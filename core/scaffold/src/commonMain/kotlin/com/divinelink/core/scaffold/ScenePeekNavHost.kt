package com.divinelink.core.scaffold

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.scene.DialogSceneStrategy
import androidx.navigation3.scene.SinglePaneSceneStrategy
import androidx.navigation3.ui.NavDisplay
import com.divinelink.core.navigation.route.Navigation
import org.koin.compose.navigation3.koinEntryProvider
import org.koin.core.annotation.KoinExperimentalAPI

@OptIn(KoinExperimentalAPI::class)
@Composable
fun ScenePeekNavHost() {
  val state = LocalScenePeekAppState.current
  val twoPaneStrategy = rememberTwoPaneSceneStrategy<Navigation>()

  NavDisplay(
    backStack = state.backStack,
    entryDecorators = listOf(
      rememberSaveableStateHolderNavEntryDecorator(),
      rememberViewModelStoreNavEntryDecorator(),
    ),
    sceneStrategy = DialogSceneStrategy<Navigation>()
      then
      twoPaneStrategy
      then
      SinglePaneSceneStrategy(),
    entryProvider = koinEntryProvider(),
  )
}
