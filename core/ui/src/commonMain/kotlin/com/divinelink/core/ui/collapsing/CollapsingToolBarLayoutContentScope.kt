package com.divinelink.core.ui.collapsing

import androidx.compose.animation.core.AnimationSpec
import androidx.compose.animation.core.VectorConverter
import androidx.compose.animation.core.spring
import androidx.compose.foundation.gestures.ScrollableState
import androidx.compose.foundation.gestures.scrollBy
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.util.fastSumBy
import kotlinx.coroutines.delay

class CollapsingToolBarLayoutContentScope(private val state: CollapsingToolBarState) {
  suspend fun ScrollableState.scrollWithToolBarBy(value: Float) {
    val consumedOffset = state.onPreScroll(Offset(0f, -value))
    val consumed = scrollBy(value + consumedOffset.y)
    state.onPostScroll(Offset(0f, -consumed), Offset(0f, -(value - consumed)))
  }

  suspend fun ScrollableState.animateScrollWithToolBarBy(
    value: Float,
    animationSpec: AnimationSpec<Float> = spring(),
  ) {
    val millisToNanos = 1_000_000L
    val duration = 8L // 8ms
    val vectorConverter = Float.VectorConverter
    val vectorAnimationSpec = animationSpec.vectorize(vectorConverter)
    vectorConverter.convertToVector(0f)
    val durationNanos = vectorAnimationSpec.getDurationNanos(
      vectorConverter.convertToVector(0f),
      vectorConverter.convertToVector(value),
      vectorConverter.convertToVector(0f),
    )
    var playTimeNanos = 0L
    var prevValue = 0f
    while (playTimeNanos < durationNanos) {
      val valueVector = vectorAnimationSpec.getValueFromNanos(
        playTimeNanos,
        vectorConverter.convertToVector(0f),
        vectorConverter.convertToVector(value),
        vectorConverter.convertToVector(0f),
      )
      val newValue = vectorConverter.convertFromVector(valueVector)
      val currentOffset = newValue - prevValue
      scrollWithToolBarBy(currentOffset)
      delay(duration)
      playTimeNanos += duration * millisToNanos
      prevValue = newValue
    }
  }

  @Suppress("LoopWithTooManyJumpStatements")
  suspend fun LazyListState.animateScrollWithToolBarToItem(
    index: Int,
    scrollOffset: Int = 0,
  ) {
    var expectedCount = ANIMATE_SCROLL_TIME / ANIMATE_SCROLL_DURATION
    while (expectedCount > 0) {
      if (firstVisibleItemIndex == index && firstVisibleItemScrollOffset == scrollOffset) break
      val expectedDistance = expectedDistanceTo(index, scrollOffset)
      if (expectedDistance == 0) break
      val expectedFrameDistance = expectedDistance.toFloat() / expectedCount
      expectedCount -= 1
      scrollWithToolBarBy(expectedFrameDistance)
      delay(ANIMATE_SCROLL_DURATION)
    }
  }

  private fun LazyListState.expectedDistanceTo(
    index: Int,
    targetScrollOffset: Int,
  ): Int {
    val visibleItems = layoutInfo.visibleItemsInfo
    val averageSize = visibleItems.fastSumBy { it.size } / visibleItems.size
    val indexesDiff = index - firstVisibleItemIndex
    return (averageSize * indexesDiff) + targetScrollOffset - firstVisibleItemScrollOffset
  }

  companion object {
    private const val ANIMATE_SCROLL_DURATION = 4L
    private const val ANIMATE_SCROLL_TIME = 100L
  }
}
