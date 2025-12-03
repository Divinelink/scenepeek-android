package com.divinelink.feature.settings.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AutoAwesome
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.divinelink.core.designsystem.theme.AppTheme
import com.divinelink.core.designsystem.theme.dimensions
import com.divinelink.core.ui.IconWrapper
import com.divinelink.core.ui.Previews
import org.jetbrains.compose.resources.painterResource

@Composable
fun SettingsClickItem(
  modifier: Modifier = Modifier,
  icon: IconWrapper? = null,
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

    Text(
      text = text,
      style = MaterialTheme.typography.bodyLarge,
    )
  }
}

@Composable
@Previews
private fun SettingsScreenPreview() {
  AppTheme {
    Surface {
      SettingsClickItem(
        icon = IconWrapper.Vector(Icons.Outlined.AutoAwesome),
        text = "Appearance",
        onClick = {},
      )
    }
  }
}
