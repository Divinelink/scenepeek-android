package com.divinelink.core.ui.popup

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntRect
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupPositionProvider

/**
 * Author: [https://gist.github.com/habibg1232191
 * https://gist.github.com/habibg1232191/2ca21b6f113a875e05f4494f32f87d4e
 */
@Composable
fun AnimatedPopup(
  modifier: Modifier = Modifier,
  expanded: Boolean,
  popupPositionProvider: PopupPositionProvider,
  onDismissRequest: (() -> Unit)? = null,
  enter: EnterTransition = fadeIn(),
  exit: ExitTransition = fadeOut(),
  content: @Composable AnimatedVisibilityScope.() -> Unit,
) {
  val expandedState = remember { MutableTransitionState(false) }
  expandedState.targetState = expanded

  if (expandedState.currentState || expandedState.targetState || !expandedState.isIdle) {
    Popup(
      popupPositionProvider = popupPositionProvider,
      onDismissRequest = onDismissRequest,
    ) {
      AnimatedVisibility(
        visibleState = expandedState,
        enter = enter,
        exit = exit,
        modifier = modifier,
        content = content,
      )
    }
  }
}

@Composable
fun AnimatedPopup(
  modifier: Modifier = Modifier,
  expanded: Boolean = true,
  alignment: Alignment = Alignment.TopStart,
  offset: IntOffset = IntOffset(0, 0),
  onDismissRequest: (() -> Unit)? = null,
  enter: EnterTransition = fadeIn(),
  exit: ExitTransition = fadeOut(),
  content: @Composable AnimatedVisibilityScope.() -> Unit,
) {
  val popupPositionProvider = remember(alignment, offset) {
    AlignmentOffsetPositionProvider(
      alignment,
      offset,
    )
  }

  AnimatedPopup(
    expanded = expanded,
    popupPositionProvider = popupPositionProvider,
    onDismissRequest = onDismissRequest,
    enter = enter,
    exit = exit,
    modifier = modifier,
    content = content,
  )
}

private class AlignmentOffsetPositionProvider(
  val alignment: Alignment,
  private val offset: IntOffset,
) : PopupPositionProvider {
  override fun calculatePosition(
    anchorBounds: IntRect,
    windowSize: IntSize,
    layoutDirection: LayoutDirection,
    popupContentSize: IntSize,
  ): IntOffset {
    // TODO: Decide which is the best way to round to result without reimplementing Alignment.align
    var popupPosition = IntOffset(0, 0)

    // Get the aligned point inside the parent
    val parentAlignmentPoint = alignment.align(
      IntSize.Zero,
      IntSize(anchorBounds.width, anchorBounds.height),
      layoutDirection,
    )
    // Get the aligned point inside the child
    val relativePopupPos = alignment.align(
      IntSize.Zero,
      IntSize(popupContentSize.width, popupContentSize.height),
      layoutDirection,
    )

    // Add the position of the parent
    popupPosition += IntOffset(anchorBounds.left, anchorBounds.top)

    // Add the distance between the parent's top left corner and the alignment point
    popupPosition += parentAlignmentPoint

    // Subtract the distance between the children's top left corner and the alignment point
    popupPosition -= IntOffset(relativePopupPos.x, relativePopupPos.y)

    // Add the user offset
    val resolvedOffset = IntOffset(
      offset.x * (if (layoutDirection == LayoutDirection.Ltr) 1 else -1),
      offset.y,
    )
    popupPosition += resolvedOffset

    return popupPosition
  }
}
