package com.divinelink.core.ui.collapsing

import androidx.compose.foundation.gestures.FlingBehavior
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.ScrollScope
import androidx.compose.foundation.gestures.rememberScrollableState
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.runtime.Stable
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.unit.Dp

class CollapsingToolBarLayoutToolBarScope(
  private val toolBarState: CollapsingToolBarState,
  val collapsedInfo: ToolBarCollapsedInfo,
) {
  fun Modifier.toolBarScrollable(): Modifier = this.composed {
    scrollable(
      orientation = Orientation.Vertical,
      state = rememberScrollableState { it },
      flingBehavior = object : FlingBehavior {
        override suspend fun ScrollScope.performFling(initialVelocity: Float): Float =
          if (toolBarState.collapsingOption.isAutoSnap) {
            val centerPx = (toolBarState.toolBarMaxHeightPx + toolBarState.toolBarMinHeightPx) / 2
            val toolBarHeightPx = toolBarState.toolBarHeightPx
            toolBarState.snapToolBar(toolBarHeightPx >= centerPx)
            0f
          } else {
            if (initialVelocity < 0) {
              toolBarState.flingY(initialVelocity)
              0f
            } else {
              initialVelocity
            }
          }
      },
      enabled = (!toolBarState.collapsingOption.collapsingWhenTop || collapsedInfo.progress < 1f),
    )
  }

  fun Modifier.requiredToolBarMaxHeight(maxHeight: Dp = toolBarState.toolBarMaxHeight): Modifier =
    this
      .fillMaxHeight()
      .requiredHeight(maxHeight)
      .offset(y = -(toolBarState.toolBarMaxHeight - collapsedInfo.toolBarHeight) / 2)
}

@Stable
data class ToolBarCollapsedInfo(
  val progress: Float,
  val toolBarHeight: Dp,
)
