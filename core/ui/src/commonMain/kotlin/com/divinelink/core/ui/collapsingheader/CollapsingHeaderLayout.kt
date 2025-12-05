package com.divinelink.core.ui.collapsingheader

import androidx.compose.foundation.MutatePriority
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.ScrollScope
import androidx.compose.foundation.gestures.ScrollableState
import androidx.compose.foundation.gestures.animateToWithDecay
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.layout
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.unit.Velocity
import kotlin.math.max
import kotlin.math.roundToInt

@Composable
fun CollapsingHeaderLayout(
  state: CollapsingHeaderState,
  modifier: Modifier = Modifier,
  headerContent: @Composable () -> Unit,
  body: @Composable BoxScope.() -> Unit,
) {
  Box(
    modifier = modifier
      .fillMaxSize()
      .scrollable(
        state = remember(state::scrollableState),
        orientation = Orientation.Vertical,
      )
      .nestedScroll(
        connection = remember(state::nestedScrollConnection),
      ),
    content = {
      Box(
        modifier = Modifier
          .onSizeChanged {
            state.expandedHeight = it.height.toFloat()
          },
      ) {
        headerContent()
      }
      Box(
        modifier = Modifier
          .layout { measurable, constraints ->
            val placeable = measurable.measure(constraints)
            layout(
              width = placeable.width,
              height = max(
                a = 0,
                b = placeable.height - state.collapsedHeight.roundToInt(),
              ),
            ) {
              placeable.place(
                x = 0,
                y = state.anchoredDraggableState.offset.roundToInt(),
              )
            }
          },
      ) {
        body()
      }
    },
  )
}

private fun CollapsingHeaderState.scrollableState() = object : ScrollableState {

  private val isScrollingState = mutableStateOf(false)
  private val isLastScrollForwardState = mutableStateOf(false)
  private val isLastScrollBackwardState = mutableStateOf(false)

  private val scrollScope = object : ScrollScope {
    override fun scrollBy(pixels: Float): Float {
      if (pixels.isNaN()) return 0f
      val delta = anchoredDraggableState.dispatchRawDelta(pixels)

      isLastScrollForwardState.value = delta > 0
      isLastScrollBackwardState.value = delta < 0

      return delta
    }
  }

  override val isScrollInProgress: Boolean
    get() = isScrollingState.value || anchoredDraggableState.isAnimationRunning

  override val lastScrolledForward: Boolean
    get() = isLastScrollForwardState.value

  override val lastScrolledBackward: Boolean
    get() = isLastScrollBackwardState.value

  override val canScrollForward: Boolean
    get() = progress != 0f

  override val canScrollBackward: Boolean
    get() = progress != 1f

  override fun dispatchRawDelta(delta: Float): Float =
    anchoredDraggableState.dispatchRawDelta(delta)

  override suspend fun scroll(
    scrollPriority: MutatePriority,
    block: suspend ScrollScope.() -> Unit,
  ) {
    anchoredDraggableState.anchoredDrag(scrollPriority) {
      isScrollingState.value = true
      try {
        scrollScope.block()
      } finally {
        isScrollingState.value = false
      }
    }
  }
}

private fun CollapsingHeaderState.nestedScrollConnection() = object : NestedScrollConnection {
  override fun onPreScroll(
    available: Offset,
    source: NestedScrollSource,
  ): Offset = when (val delta = available.y) {
    in -Float.MAX_VALUE..-Float.MIN_VALUE -> anchoredDraggableState.dispatchRawDelta(delta)
      .toOffset()

    else -> Offset.Zero
  }

  override fun onPostScroll(
    consumed: Offset,
    available: Offset,
    source: NestedScrollSource,
  ): Offset = anchoredDraggableState.dispatchRawDelta(delta = available.y).toOffset()

  @Suppress("ReturnCount")
  override suspend fun onPostFling(
    consumed: Velocity,
    available: Velocity,
  ): Velocity {
    val currentValue = anchoredDraggableState.currentValue

    if (available.y > 0 && currentValue == CollapsingHeaderStatus.Collapsed) {
      return animateToStatusWithVelocity(
        available = available,
        status = CollapsingHeaderStatus.Expanded,
      )
    }
    if (available.y < 0 && currentValue == CollapsingHeaderStatus.Expanded) {
      return animateToStatusWithVelocity(
        available = available,
        status = CollapsingHeaderStatus.Collapsed,
      )
    }

    val hasNoInertia = available == Velocity.Zero && consumed == Velocity.Zero

    if (hasNoInertia && progress < snapThreshold) {
      return animateToStatusWithVelocity(
        available = available,
        status = CollapsingHeaderStatus.Expanded,
      )
    }
    if (hasNoInertia && progress > 1 - snapThreshold) {
      return animateToStatusWithVelocity(
        available = available,
        status = CollapsingHeaderStatus.Collapsed,
      )
    }

    return super.onPostFling(consumed, available)
  }

  private suspend fun animateToStatusWithVelocity(
    available: Velocity,
    status: CollapsingHeaderStatus,
  ) = Velocity(
    x = 0f,
    y = anchoredDraggableState.animateToWithDecay(
      targetValue = status,
      velocity = available.y,
    ),
  )
}

private fun Float.toOffset() = Offset(0f, this)
