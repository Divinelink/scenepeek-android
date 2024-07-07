package com.divinelink.feature.settings.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.navigation.NavBackStackEntry
import com.ramcosta.composedestinations.spec.DestinationStyle

object SlideTransition : DestinationStyle.Animated() {
  override val enterTransition: AnimatedContentTransitionScope<NavBackStackEntry>.() ->
  EnterTransition = {
    slideInVertically()
  }
  override val exitTransition: AnimatedContentTransitionScope<NavBackStackEntry>.() ->
  ExitTransition = {
    slideOutVertically() + fadeOut()
  }
}
