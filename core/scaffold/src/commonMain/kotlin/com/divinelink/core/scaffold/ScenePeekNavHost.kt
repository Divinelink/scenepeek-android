package com.divinelink.core.scaffold

import androidx.compose.animation.SharedTransitionScope
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.NavHost
import com.divinelink.core.model.home.MediaListSection
import com.divinelink.core.navigation.route.Navigation

typealias NavGraphExtension = NavGraphBuilder.(
  navController: NavController,
  sharedTransitionScope: SharedTransitionScope,
) -> Unit

@Composable
fun SharedTransitionScope.ScenePeekNavHost() {
  val state = LocalScenePeekAppState.current
  val navController = state.navController

  NavHost(
    navController = navController,
    startDestination = Navigation.MediaListsRoute(
      section = MediaListSection.Favorites,
    ),
    enterTransition = {
      fadeIn(animationSpec = tween(durationMillis = 300, easing = LinearEasing))
    },
    exitTransition = {
      fadeOut(animationSpec = tween(durationMillis = 300, easing = LinearEasing))
    },
  ) {
    state.navigationExtension.forEach { extension ->
      extension(navController, this@ScenePeekNavHost)
    }
  }
}
