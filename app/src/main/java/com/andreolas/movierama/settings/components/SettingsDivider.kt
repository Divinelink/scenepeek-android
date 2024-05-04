package com.andreolas.movierama.settings.components

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.andreolas.movierama.ui.theme.dimensions

@Composable
fun SettingsDivider() {
  Divider(
    color = MaterialTheme.colorScheme.surfaceVariant,
    modifier = Modifier.padding(vertical = MaterialTheme.dimensions.keyline_16)
  )
}
