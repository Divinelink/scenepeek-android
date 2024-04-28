package com.andreolas.movierama.settings.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import com.andreolas.movierama.R
import com.andreolas.movierama.ui.theme.dimensions

@Composable
fun SettingsTextItem(
  icon: Painter? = null,
  title: String,
  summary: String
) {
  Row(
    verticalAlignment = androidx.compose.ui.Alignment.CenterVertically,
    horizontalArrangement = Arrangement.spacedBy(MaterialTheme.dimensions.keyline_16),
    modifier = Modifier
      .padding(MaterialTheme.dimensions.keyline_16)
      .fillMaxWidth()
  ) {
    icon?.let {
      Icon(
        painter = icon,
        contentDescription = null
      )
    }

    Column {

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
  }
}

@Composable
@Preview(showBackground = true)
private fun SettingsScreenPreview() {
  MaterialTheme {
    SettingsTextItem(
      icon = painterResource(id = R.drawable.ic_appearance_24),
      title = "Version",
      summary = "1.0.0 Debug",
    )
  }
}
