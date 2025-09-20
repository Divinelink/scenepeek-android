package com.divinelink.feature.request.media.components

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.divinelink.core.designsystem.theme.colors

@Composable
fun JellyseerrGradientText(
  text: String,
  modifier: Modifier = Modifier,
) {
  Text(
    text = text,
    modifier = modifier,
    style = MaterialTheme.typography.headlineSmall.copy(
      brush = MaterialTheme.colors.jellyseerrGradientBrush,
      fontWeight = MaterialTheme.typography.titleSmall.fontWeight,
    ),
  )
}
