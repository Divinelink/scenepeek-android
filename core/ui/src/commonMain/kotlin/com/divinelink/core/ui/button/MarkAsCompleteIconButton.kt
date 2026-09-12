package com.divinelink.core.ui.button

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.outlined.Circle
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun MarkAsCompleteIconButton(
  modifier: Modifier = Modifier,
  onClick: () -> Unit,
  isWatched: Boolean,
) {
  IconButton(
    modifier = modifier,
    onClick = onClick,
  ) {
    Icon(
      imageVector = if (isWatched) {
        Icons.Filled.CheckCircle
      } else {
        Icons.Outlined.Circle
      },
      tint = MaterialTheme.colorScheme.primary,
      contentDescription = null,
    )
  }
}
