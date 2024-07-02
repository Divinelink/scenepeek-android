package com.divinelink.feature.settings.components

import androidx.compose.foundation.Image
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
import androidx.compose.ui.res.painterResource
import com.divinelink.core.designsystem.theme.dimensions
import com.divinelink.core.ui.IconWrapper
import com.divinelink.core.ui.Previews
import com.divinelink.feature.settings.R

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

    Text(
      text = text,
      style = MaterialTheme.typography.bodyLarge,
    )
  }
}

@Composable
@Previews
private fun SettingsScreenPreview() {
  MaterialTheme {
    SettingsClickItem(
      icon = IconWrapper.Icon(resourceId = R.drawable.feature_settings_ic_settings),
      text = "Appearance",
      onClick = {},
    )
  }
}
