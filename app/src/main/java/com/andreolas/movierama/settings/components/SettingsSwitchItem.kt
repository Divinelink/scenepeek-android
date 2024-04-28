package com.andreolas.movierama.settings.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.andreolas.movierama.ui.theme.AppTheme
import com.andreolas.movierama.ui.theme.dimensions

@Composable
fun SettingsSwitchItem(
  title: String,
  summary: String,
  isChecked: Boolean,
  onCheckedChange: (Boolean) -> Unit
) {
  Row(
    verticalAlignment = Alignment.CenterVertically,
    horizontalArrangement = Arrangement.spacedBy(MaterialTheme.dimensions.keyline_16),
    modifier = Modifier
      .clickable { onCheckedChange(!isChecked) }
      .padding(MaterialTheme.dimensions.keyline_16)
      .fillMaxWidth()
  ) {
    Column(
      verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimensions.keyline_4),
      modifier = Modifier
        .fillMaxWidth()
        .weight(5f)
    ) {
      Text(
        text = title,
        style = MaterialTheme.typography.bodyLarge
      )
      Text(
        text = summary,
        style = MaterialTheme.typography.bodyMedium,
        color = MaterialTheme.colorScheme.secondary
      )
    }

    Switch(
      modifier = Modifier.weight(1f),
      checked = isChecked,
      onCheckedChange = onCheckedChange
    )
  }
}

@Preview
@Composable
private fun SettingsSwitchItemPreview() {
  AppTheme {
    SettingsSwitchItem(
      title = "Material You",
      summary = "Change the theme based on your wallpaper",
      isChecked = true,
      onCheckedChange = {}
    )
  }
}
