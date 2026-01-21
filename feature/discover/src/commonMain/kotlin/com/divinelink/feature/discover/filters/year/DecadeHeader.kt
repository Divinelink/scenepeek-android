package com.divinelink.feature.discover.filters.year

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import com.divinelink.core.designsystem.theme.dimensions
import com.divinelink.core.model.Decade

@Composable
fun DecadeHeader(
  startYear: Int,
  modifier: Modifier = Modifier,
) {
  Surface(
    modifier = modifier.fillMaxSize(),
    shape = MaterialTheme.shapes.medium,
    color = MaterialTheme.colorScheme.primaryContainer,
  ) {
    Box(
      modifier = Modifier.padding(
        horizontal = MaterialTheme.dimensions.keyline_16,
        vertical = MaterialTheme.dimensions.keyline_8,
      ),
      contentAlignment = Alignment.Center,
    ) {
      Text(
        text = Decade.getDecadeLabel(startYear),
        color = MaterialTheme.colorScheme.onPrimaryContainer,
        style = MaterialTheme.typography.titleSmall.copy(
          fontWeight = FontWeight.SemiBold,
        ),
      )
    }
  }
}
