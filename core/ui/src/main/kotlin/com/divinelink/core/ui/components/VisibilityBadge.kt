package com.divinelink.core.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.stringResource
import com.divinelink.core.designsystem.theme.dimensions
import com.divinelink.core.ui.Previews
import com.divinelink.core.ui.R

@Composable
@Previews
fun VisibilityBadge(
  modifier: Modifier = Modifier,
  isPublic: Boolean = false,
) {
  Box(
    modifier = modifier
      .wrapContentHeight()
      .graphicsLayer { alpha = 0.8f }
      .background(
        color = MaterialTheme.colorScheme.surfaceContainer.copy(alpha = 0.95f),
        shape = MaterialTheme.shapes.extraLarge,
      )
      .padding(
        vertical = MaterialTheme.dimensions.keyline_2,
        horizontal = MaterialTheme.dimensions.keyline_12,
      ),
    contentAlignment = Alignment.Center,
  ) {
    Text(
      text = if (isPublic) {
        stringResource(R.string.core_ui_public)
      } else {
        stringResource(R.string.core_ui_private)
      },
      style = MaterialTheme.typography.labelSmall,
      color = MaterialTheme.colorScheme.primary,
    )
  }
}
