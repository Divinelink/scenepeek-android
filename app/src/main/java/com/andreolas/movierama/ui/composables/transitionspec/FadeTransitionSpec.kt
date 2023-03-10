package com.andreolas.movierama.ui.composables.transitionspec

import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.ContentTransform
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.with

@OptIn(ExperimentalAnimationApi::class)
fun fadeTransitionSpec(): AnimatedContentScope<*>.() -> ContentTransform = {
    fadeIn(animationSpec = tween(300, delayMillis = 100)) with
        fadeOut(animationSpec = tween(100))
}
