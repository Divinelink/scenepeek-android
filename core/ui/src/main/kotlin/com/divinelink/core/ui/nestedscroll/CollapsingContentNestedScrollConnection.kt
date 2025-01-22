package com.divinelink.core.ui.nestedscroll

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun rememberCollapsingContentNestedScrollConnection(
  maxHeight: Dp,
  minHeight: Dp = 0.dp,
): CollapsingContentNestedScrollConnection = remember {
  CollapsingContentNestedScrollConnection(
    maxHeight = maxHeight,
    minHeight = minHeight,
  )
}

class CollapsingContentNestedScrollConnection(
  val maxHeight: Dp,
  val minHeight: Dp,
) : NestedScrollConnection {

  var currentSize by mutableStateOf(maxHeight)

  override fun onPreScroll(
    available: Offset,
    source: NestedScrollSource,
  ): Offset {
    // During preScroll, only allow shrinking (negative delta)
    val delta = available.y
    if (delta >= 0) return Offset.Zero // Don't consume positive scroll

    val newImageSize = currentSize + delta.dp
    val previousImageSize = currentSize

    // Constrain the image size within the allowed bounds
    currentSize = newImageSize.coerceIn(minHeight, maxHeight)
    val consumed = currentSize - previousImageSize

    // Calculate the scale for the image
    // imageScale = currentSize / maxContentSize

    // Return the consumed scroll amount
    return Offset(0f, consumed.value)
  }

  override fun onPostScroll(
    consumed: Offset,
    available: Offset,
    source: NestedScrollSource,
  ): Offset {
    // During postScroll, only allow expanding (positive delta)
    val delta = available.y
    if (delta <= 0) return Offset.Zero // Don't consume negative scroll

    val newImageSize = currentSize + delta.dp
    val previousImageSize = currentSize

    // Constrain the image size within the allowed bounds
    currentSize = newImageSize.coerceIn(minHeight, maxHeight)
    val consumed = currentSize - previousImageSize

    // Calculate the scale for the image
    // imageScale = currentSize / maxContentSize

    // Return the consumed scroll amount
    return Offset(0f, consumed.value)
  }
}
