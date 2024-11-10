package com.divinelink.scenepeek.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.navigation.NavBackStackEntry
import com.ramcosta.composedestinations.animations.NavHostAnimatedDestinationStyle

class DestinationDefaultTransitions(
  override val enterTransition: AnimatedContentTransitionScope<NavBackStackEntry>.() ->
  EnterTransition = {
    slideInHorizontally()
  },
  override val exitTransition: AnimatedContentTransitionScope<NavBackStackEntry>.() ->
  ExitTransition = {
    fadeOut()
  },
) : NavHostAnimatedDestinationStyle()
