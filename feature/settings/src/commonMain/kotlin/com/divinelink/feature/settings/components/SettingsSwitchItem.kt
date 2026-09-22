@file:Suppress("MagicNumber")

package com.divinelink.feature.settings.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.divinelink.core.designsystem.theme.AppTheme
import com.divinelink.core.designsystem.theme.dimensions
import com.divinelink.core.ui.IconWrapper
import com.divinelink.core.ui.Previews
import com.divinelink.core.ui.conditional
import org.jetbrains.compose.resources.painterResource

@Composable
fun SettingsSwitchItem(
  title: String,
  summary: String,
  icon: IconWrapper? = null,
  isChecked: Boolean,
  enabled: Boolean = true,
  onCheckedChange: (Boolean) -> Unit,
) {
  Row(
    verticalAlignment = Alignment.CenterVertically,
    horizontalArrangement = Arrangement.spacedBy(MaterialTheme.dimensions.keyline_16),
    modifier = Modifier
      .conditional(
        condition = enabled,
        ifTrue = { clickable { onCheckedChange(!isChecked) } },
      )
      .padding(MaterialTheme.dimensions.keyline_16)
      .fillMaxWidth(),
  ) {
    icon?.let {
      when (icon) {
        is IconWrapper.Image -> Image(
          modifier = Modifier.size(MaterialTheme.dimensions.keyline_36),
          painter = painterResource(icon.resourceId),
          contentDescription = null,
        )
        is IconWrapper.Icon -> Icon(
          painter = painterResource(icon.resourceId),
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
      modifier = Modifier
        .fillMaxWidth()
        .weight(5f),
    ) {
      Text(
        text = title,
        style = MaterialTheme.typography.bodyLarge,
      )
      Text(
        text = summary,
        style = MaterialTheme.typography.bodyMedium,
        color = MaterialTheme.colorScheme.secondary,
      )
    }

    Switch(
      modifier = Modifier.weight(1f),
      checked = isChecked,
      enabled = enabled,
      onCheckedChange = onCheckedChange,
    )
  }
}

@Previews
@Composable
private fun SettingsSwitchItemPreview() {
  AppTheme {
    SettingsSwitchItem(
      title = "Material You",
      summary = "Change the theme based on your wallpaper",
      isChecked = true,
      onCheckedChange = {},
    )
  }
}
