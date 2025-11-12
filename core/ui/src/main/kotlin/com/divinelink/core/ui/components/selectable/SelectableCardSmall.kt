package com.divinelink.core.ui.components.selectable

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.CheckCircle
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.divinelink.core.designsystem.theme.LocalDarkThemeProvider
import com.divinelink.core.designsystem.theme.dimensions

@Composable
fun SelectableCardSmall(
  modifier: Modifier = Modifier,
  isSelected: Boolean = false,
  isSelectionMode: Boolean = false,
  onLongClick: () -> Unit = {},
  onClick: () -> Unit = {},
  content: @Composable (onClick: () -> Unit, onLongClick: () -> Unit) -> Unit,
) {
  val backgroundColor = if (LocalDarkThemeProvider.current) {
    MaterialTheme.colorScheme.surface.copy(alpha = 0.7f)
  } else {
    MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
  }

  Box(
    modifier = modifier.animateContentSize(),
  ) {
    Box(
      modifier = Modifier
        .fillMaxSize()
        .align(Alignment.Center),
    ) {
      content(
        onClick,
        onLongClick,
      )
    }

    AnimatedVisibility(
      modifier = Modifier
        .padding(MaterialTheme.dimensions.keyline_4)
        .align(Alignment.TopStart),
      visible = isSelectionMode && isSelected,
      enter = fadeIn(animationSpec = tween(300)) +
        slideInVertically(animationSpec = tween(300)) { -it / 2 },
      exit = fadeOut(animationSpec = tween(300)) +
        slideOutVertically(animationSpec = tween(300)) { -it / 2 },
    ) {
      Icon(
        modifier = Modifier
          .background(
            color = backgroundColor,
            shape = CircleShape,
          )
          .size(MaterialTheme.dimensions.keyline_28)
          .padding(MaterialTheme.dimensions.keyline_4),
        imageVector = Icons.Rounded.CheckCircle,
        contentDescription = null,
        tint = Color.White,
      )
    }
  }
}
