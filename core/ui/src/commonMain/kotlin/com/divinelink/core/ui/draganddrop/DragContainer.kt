package com.divinelink.core.ui.draganddrop

import androidx.compose.foundation.gestures.detectDragGesturesAfterLongPress
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput

fun Modifier.dragContainer(dragDropState: DragDropState): Modifier = pointerInput(dragDropState) {
  detectDragGesturesAfterLongPress(
    onDrag = { change, offset ->
      change.consume()
      dragDropState.onDrag(offset = offset)
    },
    onDragStart = { offset -> dragDropState.onDragStart(offset) },
    onDragEnd = { dragDropState.onDragInterrupted() },
    onDragCancel = { dragDropState.onDragInterrupted() },
  )
}
