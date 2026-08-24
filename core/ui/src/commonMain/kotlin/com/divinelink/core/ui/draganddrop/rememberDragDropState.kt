package com.divinelink.core.ui.draganddrop

import androidx.compose.foundation.gestures.scrollBy
import androidx.compose.foundation.lazy.LazyListItemInfo
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.withFrameMillis

@Composable
fun rememberDragDropState(
  lazyListState: LazyListState,
  onMove: (LazyListItemInfo, LazyListItemInfo) -> Unit,
  onDragEnd: () -> Unit,
): DragDropState {
  val scope = rememberCoroutineScope()
  val state = remember(lazyListState) {
    DragDropState(state = lazyListState, onMove = onMove, onDragEnd = onDragEnd, scope = scope)
  }
  LaunchedEffect(state) {
    while (true) {
      withFrameMillis { }
      val speed = state.autoScrollSpeed
      if (speed != 0f) {
        lazyListState.scrollBy(speed)
      }
    }
  }
  return state
}
