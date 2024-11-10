@file:Suppress("MagicNumber")

package com.divinelink.scenepeek.ui.composables.transitionspec

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.ContentTransform
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith

fun fadeTransitionSpec(): AnimatedContentTransitionScope<Any>.() -> ContentTransform = {
  fadeIn(animationSpec = tween(300, delayMillis = 100)) togetherWith
    fadeOut(animationSpec = tween(100))
}
