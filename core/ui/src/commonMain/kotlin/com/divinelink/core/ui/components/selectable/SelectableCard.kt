package com.divinelink.core.ui.components.selectable

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.CheckCircle
import androidx.compose.material.icons.rounded.RadioButtonUnchecked
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.clipToBounds
import com.divinelink.core.designsystem.theme.dimensions

@Composable
fun SelectableCard(
  modifier: Modifier = Modifier,
  isSelected: Boolean = false,
  isSelectionMode: Boolean = false,
  onLongClick: () -> Unit = {},
  onClick: () -> Unit = {},
  content: @Composable RowScope.(onClick: () -> Unit, onLongClick: () -> Unit) -> Unit,
) {
  val iconWidth = MaterialTheme.dimensions.keyline_48
  val reservedWidth by animateDpAsState(
    targetValue = if (isSelectionMode) iconWidth else MaterialTheme.dimensions.keyline_0,
    animationSpec = tween(300),
    label = "ReservedWidthAnimation",
  )

  Row(
    modifier = modifier
      .clip(MaterialTheme.shapes.large)
      .combinedClickable(
        onClick = onClick,
        onLongClick = onLongClick,
      )
      .animateContentSize()
      .fillMaxWidth(),
    verticalAlignment = Alignment.CenterVertically,
  ) {
    Box(
      modifier = Modifier
        .width(reservedWidth)
        .clipToBounds(),
    ) {
      this@Row.AnimatedVisibility(
        visible = isSelectionMode,
        enter = fadeIn(animationSpec = tween(300)) +
          slideInHorizontally(animationSpec = tween(300)) { -it / 2 },
        exit = fadeOut(animationSpec = tween(300)) +
          slideOutHorizontally(animationSpec = tween(300)) { -it / 2 },
      ) {
        IconButton(onClick) {
          if (isSelected) {
            Icon(
              imageVector = Icons.Rounded.CheckCircle,
              contentDescription = null,
              tint = MaterialTheme.colorScheme.primary,
            )
          } else {
            Icon(
              imageVector = Icons.Rounded.RadioButtonUnchecked,
              contentDescription = null,
              tint = MaterialTheme.colorScheme.outline,
            )
          }
        }
      }
    }

    Box(
      modifier = Modifier
        .weight(1f)
        .clip(MaterialTheme.shapes.large)
        .animateContentSize(),
    ) {
      this@Row.content(
        onClick,
        onLongClick,
      )
    }
  }
}
