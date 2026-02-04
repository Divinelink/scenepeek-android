package com.divinelink.core.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.divinelink.core.designsystem.theme.dimensions

@Composable
fun SimpleInformationRow(
  title: String,
  data: String,
) {
  Row(
    modifier = Modifier.fillMaxWidth(),
    horizontalArrangement = Arrangement.spacedBy(MaterialTheme.dimensions.keyline_8),
  ) {
    Text(
      modifier = Modifier.weight(0.35f),
      text = title,
      style = MaterialTheme.typography.bodyMedium,
    )

    Text(
      modifier = Modifier.weight(0.65f),
      text = data,
      style = MaterialTheme.typography.bodyMedium,
    )
  }
}
