package com.divinelink.feature.settings.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import com.divinelink.core.designsystem.theme.AppTheme
import com.divinelink.core.designsystem.theme.dimensions
import com.divinelink.core.ui.IconWrapper
import com.divinelink.core.ui.Previews
import com.divinelink.feature.settings.R

@Composable
fun SettingsTextItem(
  icon: IconWrapper? = null,
  title: String,
  summary: String?,
) {
  Row(
    verticalAlignment = Alignment.CenterVertically,
    horizontalArrangement = Arrangement.spacedBy(MaterialTheme.dimensions.keyline_16),
    modifier = Modifier
      .padding(MaterialTheme.dimensions.keyline_16)
      .fillMaxWidth(),
  ) {
    icon?.let {
      when (icon) {
        is IconWrapper.Image -> Image(
          painter = painterResource(id = icon.resourceId),
          contentDescription = null,
        )
        is IconWrapper.Icon -> Icon(
          painter = painterResource(id = icon.resourceId),
          contentDescription = null,
        )
        is IconWrapper.Vector -> Icon(
          imageVector = icon.vector,
          contentDescription = null,
        )
      }
    }

    Column(
      verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimensions.keyline_4),
    ) {
      Text(
        text = title,
        style = MaterialTheme.typography.bodyLarge,
      )
      summary?.let {
        Text(
          text = summary,
          style = MaterialTheme.typography.bodyMedium,
          color = MaterialTheme.colorScheme.secondary,
        )
      }
    }
  }
}

@Composable
@Previews
private fun SettingsScreenPreview() {
  AppTheme {
    Surface {
      Column {
        SettingsTextItem(
          icon = IconWrapper.Icon(R.drawable.feature_settings_ic_appearance_24),
          title = "Version",
          summary = "1.0.0 Debug",
        )

        SettingsTextItem(
          icon = IconWrapper.Icon(R.drawable.feature_settings_ic_appearance_24),
          title = "Version",
          summary = null,
        )
      }
    }
  }
}
