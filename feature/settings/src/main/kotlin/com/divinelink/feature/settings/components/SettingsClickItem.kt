package com.divinelink.feature.settings.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import com.divinelink.core.designsystem.theme.dimensions
import com.divinelink.feature.settings.R

@Composable
fun SettingsClickItem(
  modifier: Modifier = Modifier,
  icon: Painter? = null,
  text: String,
  onClick: () -> Unit,
) {
  Row(
    verticalAlignment = Alignment.CenterVertically,
    horizontalArrangement = Arrangement.spacedBy(MaterialTheme.dimensions.keyline_16),
    modifier = modifier
      .clickable { onClick() }
      .padding(MaterialTheme.dimensions.keyline_16)
      .fillMaxWidth(),
  ) {
    icon?.let {
      Icon(
        painter = icon,
        contentDescription = null,
      )
    }

    Text(
      text = text,
      style = MaterialTheme.typography.bodyLarge,
    )
  }
}

@Composable
@Preview(showBackground = true)
private fun SettingsScreenPreview() {
  MaterialTheme {
    SettingsClickItem(
      icon = painterResource(id = R.drawable.ic_appearance_24),
      text = "Appearance",
      onClick = {},
    )
  }
}
