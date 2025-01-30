package com.divinelink.core.ui.nestedscroll

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.AnimationVector1D
import androidx.compose.animation.core.VectorConverter
import androidx.compose.animation.splineBasedDecay
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.Velocity
import androidx.compose.ui.unit.dp

@Composable
fun rememberCollapsingContentNestedScrollConnection(
  maxHeight: Dp,
  minHeight: Dp = 0.dp,
  density: Density = LocalDensity.current,
): CollapsingContentNestedScrollConnection {
  val currentSize = rememberSaveable { mutableFloatStateOf(maxHeight.value) }

  return remember {
    CollapsingContentNestedScrollConnection(
      maxHeight = maxHeight,
      minHeight = minHeight,
      density = density,
      initialSize = currentSize.floatValue.dp,
      onSizeChanged = { currentSize.floatValue = it.value },
    )
  }
}

class CollapsingContentNestedScrollConnection(
  val maxHeight: Dp,
  val minHeight: Dp,
  private val density: Density,
  initialSize: Dp,
  private val onSizeChanged: (Dp) -> Unit,
) : NestedScrollConnection {

  var currentSize by mutableStateOf(initialSize)
    private set

  override fun onPreScroll(
    available: Offset,
    source: NestedScrollSource,
  ): Offset {
    // During preScroll, only allow shrinking (negative delta)
    val delta = available.y
    if (delta >= 0) return Offset.Zero // Don't consume positive scroll

    val newImageSize = currentSize + delta.dp
    val previousImageSize = currentSize

    // Constrain the image size within the allowed bounds
    currentSize = newImageSize.coerceIn(minHeight, maxHeight)
    onSizeChanged(currentSize) // Update the saved state

    // Return the consumed scroll amount
    return Offset(0f, (currentSize - previousImageSize).value)
  }

  override fun onPostScroll(
    consumed: Offset,
    available: Offset,
    source: NestedScrollSource,
  ): Offset {
    // During postScroll, only allow expanding (positive delta)
    val delta = available.y
    if (delta <= 0) return Offset.Zero // Don't consume negative scroll

    val newImageSize = currentSize + delta.dp
    val previousImageSize = currentSize

    // Constrain the image size within the allowed bounds
    currentSize = newImageSize.coerceIn(minHeight, maxHeight)
    onSizeChanged(currentSize)

    // Return the consumed scroll amount
    return Offset(0f, (currentSize - previousImageSize).value)
  }

  private var decayAnimation: Animatable<Dp, AnimationVector1D>? = null

  override suspend fun onPostFling(
    consumed: Velocity,
    available: Velocity,
  ): Velocity {
    val velocityY = available.y
    if (velocityY == 0f) return Velocity.Zero

    // Start decay animation for the header
    decayAnimation?.stop()
    decayAnimation = Animatable(currentSize, Dp.VectorConverter)
    val velocityDp = with(density) { velocityY.toDp() }

    decayAnimation?.animateDecay(
      initialVelocity = velocityDp.value.toDp(density),
      animationSpec = splineBasedDecay(density),
    ) {
      currentSize = value.coerceIn(minHeight, maxHeight)
      onSizeChanged(currentSize) // Update the saved state
    }

    return Velocity(0f, velocityY) // Consume the vertical velocity
  }

  private fun Float.toDp(density: Density): Dp = with(density) { this@toDp.toDp() }
}
