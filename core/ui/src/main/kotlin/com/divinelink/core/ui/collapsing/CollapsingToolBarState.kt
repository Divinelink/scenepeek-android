package com.divinelink.core.ui.collapsing

import androidx.compose.animation.SplineBasedFloatDecayAnimationSpec
import androidx.compose.animation.core.animate
import androidx.compose.animation.core.animateDecay
import androidx.compose.animation.core.spring
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.listSaver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun rememberCollapsingToolBarState(
  toolBarMaxHeight: Dp = 56.dp,
  toolBarMinHeight: Dp = 0.dp,
  collapsingOption: CollapsingOption = CollapsingOption.EnterAlwaysCollapsed,
): CollapsingToolBarState {
  val density = LocalDensity.current

  return rememberSaveable(saver = CollapsingToolBarState.Saver) {
    CollapsingToolBarState(density, toolBarMaxHeight, toolBarMinHeight, collapsingOption)
  }
}

@Stable
class CollapsingToolBarState(
  private val density: Density,
  val toolBarMaxHeight: Dp,
  val toolBarMinHeight: Dp,
  val collapsingOption: CollapsingOption,
) {
  var progress: Float by mutableFloatStateOf(0f)
    internal set
  var contentOffset: Float by mutableFloatStateOf(0f)
    internal set
  internal var toolbarOffsetHeightPx: Float by mutableFloatStateOf(0f)

  internal val toolBarMaxHeightPx: Int = with(density) { toolBarMaxHeight.roundToPx() }
  internal val toolBarMinHeightPx: Int = with(density) { toolBarMinHeight.roundToPx() }
  val toolBarHeight by derivedStateOf {
    toolBarMaxHeight - ((toolBarMaxHeight - toolBarMinHeight) * progress)
  }
  internal val toolBarHeightPx by derivedStateOf {
    with(density) { toolBarHeight.roundToPx() }
  }
  private val toolbarHeightRangePx by derivedStateOf { toolBarMaxHeightPx - toolBarMinHeightPx }

  suspend fun snapToolBar(isExpand: Boolean) {
    val beginValue = toolBarHeightPx.toFloat()
    val finishValue = if (isExpand) toolBarMaxHeightPx.toFloat() else toolBarMinHeightPx.toFloat()
    var prevValue = beginValue
    animate(beginValue, finishValue, animationSpec = spring()) { currentValue, _ ->
      val diff = -(prevValue - currentValue)
      if (collapsingOption.collapsingWhenTop) {
        contentOffset -= diff
        if (contentOffset < 0f) {
          contentOffset = 0f
        }
      }
      toolbarOffsetHeightPx =
        (toolbarOffsetHeightPx - diff).coerceIn(0f, toolbarHeightRangePx.toFloat())
      progress =
        if (toolbarHeightRangePx >
          0
        ) {
          1f - ((toolbarHeightRangePx - toolbarOffsetHeightPx) / toolbarHeightRangePx)
        } else {
          0f
        }
      prevValue = currentValue
    }
  }

  private fun consumeScrollHeight(availableY: Float): Float {
    val nextToolbarHeightPx =
      (toolbarOffsetHeightPx - availableY).coerceIn(0f, toolbarHeightRangePx.toFloat())
    val consumedY = toolbarOffsetHeightPx - nextToolbarHeightPx
    toolbarOffsetHeightPx = nextToolbarHeightPx
    progress =
      if (toolbarHeightRangePx >
        0
      ) {
        1f - ((toolbarHeightRangePx - toolbarOffsetHeightPx) / toolbarHeightRangePx)
      } else {
        0f
      }
    return consumedY
  }

  internal fun onPreScroll(available: Offset): Offset {
    val directionDown = available.y < 0
    return if (directionDown) {
      Offset(
        0f,
        if (toolbarOffsetHeightPx < toolbarHeightRangePx) consumeScrollHeight(available.y) else 0f,
      )
    } else {
      if (collapsingOption.collapsingWhenTop) {
        Offset(0f, if (contentOffset <= 0f) consumeScrollHeight(available.y) else 0f)
      } else {
        Offset(0f, if (toolbarOffsetHeightPx > 0) consumeScrollHeight(available.y) else 0f)
      }
    }
  }

  internal fun onPostScroll(
    consumed: Offset,
    available: Offset,
  ) {
    if (collapsingOption.collapsingWhenTop) {
      contentOffset -= consumed.y
      // Correction Logic. same as Google code.
      // Reset the total content offset to zero when scrolling all the way down. This
      // will eliminate some float precision inaccuracies.
      if (consumed.y == 0f && available.y > 0f || contentOffset < 0f) {
        contentOffset = 0f
      }
    }
  }

  internal suspend fun flingY(velocityY: Float) {
    var isDone = false
    contentOffset = 0f
    var prevValue = 0f
    animateDecay(0f, velocityY, SplineBasedFloatDecayAnimationSpec(density)) { value, _ ->
      if (!isDone) {
        val diff = value - prevValue
        prevValue = value
        val consumedOffset = onPreScroll(Offset(0f, diff))
        onPostScroll(consumedOffset, Offset.Zero)
        if (consumedOffset.y == 0f) isDone = true
      }
    }
  }

  companion object {
    val Saver: Saver<CollapsingToolBarState, *> = listSaver(
      save = {
        listOf(
          it.density.density,
          it.density.fontScale,
          it.toolBarMaxHeight.value,
          it.toolBarMinHeight.value,
          CollapsingOption.toIndex(it.collapsingOption),
          it.progress,
          it.contentOffset,
          it.toolbarOffsetHeightPx,
        )
      },
      restore = {
        CollapsingToolBarState(
          Density(it[0] as Float, it[1] as Float),
          Dp(it[2] as Float),
          Dp(it[3] as Float),
          CollapsingOption.toOption(it[4] as Int),
        ).apply {
          progress = it[5] as Float
          contentOffset = it[6] as Float
          toolbarOffsetHeightPx = it[7] as Float
        }
      },
    )
  }
}
