package com.andreolas.movierama.settings.components

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.andreolas.movierama.ui.theme.dimensions

@Composable
fun SettingsDivider() {
  HorizontalDivider(
    modifier = Modifier.padding(vertical = MaterialTheme.dimensions.keyline_16),
    color = MaterialTheme.colorScheme.surfaceVariant
  )
}
