package com.divinelink.core.ui.collapsing

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.unit.Velocity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class CollapsingLayoutNestedScrollConnection(
  private val state: CollapsingToolBarState,
  private val coroutineScope: CoroutineScope,
) : NestedScrollConnection {
  private var snapAnimationJob: Job? = null
  override fun onPreScroll(
    available: Offset,
    source: NestedScrollSource,
  ): Offset {
    snapAnimationJob?.cancel()
    return state.onPreScroll(available)
  }

  override fun onPostScroll(
    consumed: Offset,
    available: Offset,
    source: NestedScrollSource,
  ): Offset {
    state.onPostScroll(consumed, available)
    return super.onPostScroll(consumed, available, source)
  }

  override suspend fun onPostFling(
    consumed: Velocity,
    available: Velocity,
  ): Velocity {
    snapAnimationJob = coroutineScope.launch {
      if (available.y > 0f) {
        state.flingY(available.y)
      } else if (available.y < 0f && state.progress < 1f) {
        state.flingY(available.y)
      }
      if (state.collapsingOption.isAutoSnap) {
        val centerPx = (state.toolBarMaxHeightPx + state.toolBarMinHeightPx) / 2
        val toolBarHeightPx = state.toolBarHeightPx
        state.snapToolBar(toolBarHeightPx >= centerPx)
      }
      snapAnimationJob = null
    }
    return if (available.y > 0f) Velocity(0f, available.y) else Velocity.Zero
  }
}
