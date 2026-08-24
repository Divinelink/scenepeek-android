package com.divinelink.core.ui.draganddrop

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.zIndex

@Composable
fun LazyItemScope.DraggableItem(
  state: DragDropState,
  index: Int,
  modifier: Modifier = Modifier,
  content: @Composable (isDragging: Boolean) -> Unit,
) {
  val isDragging = index == state.draggingItemIndex

  val itemModifier = if (isDragging) {
    Modifier
      .zIndex(1f)
      .graphicsLayer { translationY = state.draggingItemOffset }
  } else {
    Modifier.animateItem()
  }
  Box(modifier.then(itemModifier)) { content(isDragging) }
}
