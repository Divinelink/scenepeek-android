package com.divinelink.core.ui.list

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.layout
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import kotlin.math.roundToInt

@Composable
fun LazyColumnWithOffset(
  paddingOffset: Dp,
  stickyIndex: Int,
  state: LazyListState,
  onScrollUpdate: (Float) -> Unit,
  content: LazyListScope.() -> Unit,
) {
  val offsetFraction by remember {
    derivedStateOf {
      when {
        state.firstVisibleItemIndex >= stickyIndex -> 1f
        state.firstVisibleItemIndex == stickyIndex - 1 -> {
          val itemInfo = state.layoutInfo.visibleItemsInfo
            .firstOrNull { it.index == stickyIndex - 1 }
          if (itemInfo != null && itemInfo.size > 0) {
            val scrolled = -itemInfo.offset.toFloat()
            (scrolled / itemInfo.size).coerceIn(0f, 1f)
          } else {
            0f
          }
        }
        else -> 0f
      }
    }
  }

  val progress by remember {
    derivedStateOf {
      if (state.firstVisibleItemIndex > 0) {
        1f
      } else {
        val offset = state.firstVisibleItemScrollOffset.toFloat()
        val firstItemHeight = state.layoutInfo.visibleItemsInfo
          .firstOrNull()?.size?.toFloat() ?: 1f
        (offset / firstItemHeight).coerceIn(0f, 1f)
      }
    }
  }

  LaunchedEffect(progress) {
    onScrollUpdate(progress)
  }

  LazyColumn(
    state = state,
    contentPadding = PaddingValues(bottom = paddingOffset),
    modifier = Modifier
      .fillMaxSize()
      .offset {
        IntOffset(0, (paddingOffset.roundToPx() * offsetFraction).roundToInt())
      }
      .layout { measurable, constraints ->
        val topBarHeightPx = paddingOffset.roundToPx()
        val placeable = measurable.measure(
          constraints.copy(maxHeight = constraints.maxHeight + topBarHeightPx),
        )
        layout(placeable.width, constraints.maxHeight) {
          placeable.place(0, -topBarHeightPx)
        }
      }
      .background(MaterialTheme.colorScheme.background),
  ) {
    content()
  }
}
