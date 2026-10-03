package com.divinelink.core.ui.media

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.divinelink.core.designsystem.theme.dimensions

@Composable
fun ContentRating(
  modifier: Modifier = Modifier,
  contentRating: String,
) {
  Text(
    modifier = modifier
      .border(
        width = MaterialTheme.dimensions.keyline_1,
        color = MaterialTheme.colorScheme.surfaceVariant,
        shape = MaterialTheme.shapes.small,
      )
      .padding(
        horizontal = MaterialTheme.dimensions.keyline_8,
        vertical = MaterialTheme.dimensions.keyline_2,
      ),
    lineHeight = MaterialTheme.typography.labelSmall.fontSize * 1.7,
    text = contentRating,
    color = MaterialTheme.colorScheme.secondary,
    fontSize = MaterialTheme.typography.bodySmall.fontSize,
    fontWeight = MaterialTheme.typography.titleSmall.fontWeight,
  )
}
