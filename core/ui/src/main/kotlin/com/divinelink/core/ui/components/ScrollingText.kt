package com.divinelink.core.ui.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ContentTransform
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun ScrollingText(
  modifier: Modifier = Modifier,
  text: String,
) {
  AnimatedContent(
    targetState = text,
    transitionSpec = { textChangeAnimation() },
    label = "ScrollingText",
  ) { state ->
    Text(
      text = state,
      style = MaterialTheme.typography.titleMedium,
      modifier = modifier,
    )
  }
}

private fun textChangeAnimation(durationMillis: Int = 500): ContentTransform {
  val enterTransition = slideInVertically(
    animationSpec = tween(durationMillis),
  ) { height ->
    height
  } + fadeIn(
    animationSpec = tween(durationMillis),
  )
  val exitTransition = slideOutVertically(
    animationSpec = tween(durationMillis),
  ) { height ->
    -height
  } + fadeOut(
    animationSpec = tween(durationMillis),
  )
  return enterTransition.togetherWith(exitTransition)
}
