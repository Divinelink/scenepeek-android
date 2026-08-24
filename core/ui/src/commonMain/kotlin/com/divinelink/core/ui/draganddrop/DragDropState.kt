package com.divinelink.core.ui.draganddrop

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.lazy.LazyListItemInfo
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.geometry.Offset
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class DragDropState internal constructor(
  private val state: LazyListState,
  private val scope: CoroutineScope,
  private val onMove: (LazyListItemInfo, LazyListItemInfo) -> Unit,
  private val onDragEnd: () -> Unit,
) {
  var draggingItemIndex by mutableStateOf<Int?>(null)
    private set

  internal var autoScrollSpeed by mutableFloatStateOf(0f)

  private var draggingItemDraggedDelta by mutableFloatStateOf(0f)
  private var draggingItemInitialOffset by mutableIntStateOf(0)
  internal val draggingItemOffset: Float
    get() = draggingItemLayoutInfo?.let { item ->
      draggingItemInitialOffset + draggingItemDraggedDelta - item.offset
    } ?: 0f

  private val draggingItemLayoutInfo: LazyListItemInfo?
    get() = state.layoutInfo.visibleItemsInfo.firstOrNull { it.index == draggingItemIndex }

  internal var previousIndexOfDraggedItem by mutableStateOf<Int?>(null)
    private set

  internal var previousItemOffset = Animatable(0f)
    private set

  internal fun onDragStart(offset: Offset) {
    state.layoutInfo.visibleItemsInfo
      .firstOrNull { item -> offset.y.toInt() in item.offset..(item.offset + item.size) }
      ?.also {
        draggingItemIndex = it.index
        draggingItemInitialOffset = it.offset
      }
  }

  internal fun onDragInterrupted() {
    if (draggingItemIndex != null) {
      previousIndexOfDraggedItem = draggingItemIndex
      val startOffset = draggingItemOffset
      scope.launch {
        previousItemOffset.snapTo(startOffset)
        previousItemOffset.animateTo(
          0f,
          spring(stiffness = Spring.StiffnessMediumLow, visibilityThreshold = 1f),
        )
        previousIndexOfDraggedItem = null
      }
    }
    autoScrollSpeed = 0f
    draggingItemDraggedDelta = 0f
    draggingItemIndex = null
    draggingItemInitialOffset = 0
    onDragEnd.invoke()
  }

  internal fun onDrag(offset: Offset) {
    draggingItemDraggedDelta += offset.y

    val draggingItem = draggingItemLayoutInfo ?: return

    val startOffset = draggingItem.offset + draggingItemOffset
    val endOffset = startOffset + draggingItem.size
    val middleOffset = startOffset + (endOffset - startOffset) / 2f

    val targetItem = state.layoutInfo.visibleItemsInfo.find { item ->
      middleOffset.toInt() in item.offset..item.offsetEnd &&
        draggingItem.index != item.index
    }
    if (targetItem != null) {
      if (
        draggingItem.index == state.firstVisibleItemIndex ||
        targetItem.index == state.firstVisibleItemIndex
      ) {
        state.requestScrollToItem(
          state.firstVisibleItemIndex,
          state.firstVisibleItemScrollOffset,
        )
      }

      onMove.invoke(draggingItem, targetItem)
      draggingItemIndex = targetItem.index
    }

    val edgeZone = state.layoutInfo.viewportEndOffset * 0.2f
    val maxScrollSpeed = 85f
    autoScrollSpeed = when {
      draggingItemDraggedDelta > 0 -> {
        val penetration = (endOffset - (state.layoutInfo.viewportEndOffset - edgeZone))
          .coerceAtLeast(0f)
        val speed = penetration / edgeZone * maxScrollSpeed
        val maxScroll = draggingItem.offset.toFloat().coerceAtLeast(0f)
        speed.coerceIn(0f, maxScroll)
      }

      draggingItemDraggedDelta < 0 -> {
        val penetration = ((state.layoutInfo.viewportStartOffset + edgeZone) - startOffset)
          .coerceAtLeast(0f)
        val speed = penetration / edgeZone * maxScrollSpeed
        val maxScroll = (state.layoutInfo.viewportEndOffset - draggingItem.offsetEnd)
          .toFloat()
          .coerceAtLeast(0f)
        (-speed).coerceIn(-maxScroll, 0f)
      }

      else -> 0f
    }
  }

  private val LazyListItemInfo.offsetEnd: Int
    get() = this.offset + this.size
}
