package com.divinelink.feature.details.media.ui.actions.button

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import com.divinelink.core.designsystem.theme.dimensions
import com.divinelink.core.ui.IconWrapper
import com.divinelink.core.ui.components.expandablefab.FloatingActionButtonItem
import com.divinelink.core.ui.getString
import org.jetbrains.compose.resources.painterResource

@Composable
fun MediaMainAction(
  modifier: Modifier = Modifier,
  action: FloatingActionButtonItem,
  containerColor: Color = Color.Unspecified,
  iconTint: Color = MaterialTheme.colorScheme.onSurface,
  border: BorderStroke? = null,
) {
  Card(
    modifier = modifier
      .padding(MaterialTheme.dimensions.keyline_8),
    onClick = action.onClick,
    colors = CardDefaults.cardColors(
      containerColor = containerColor,
    ),
    border = border,
  ) {
    Column(
      modifier = Modifier
        .fillMaxWidth()
        .padding(MaterialTheme.dimensions.keyline_8),
      verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimensions.keyline_8),
      horizontalAlignment = Alignment.CenterHorizontally,
    ) {
      when (action.icon) {
        is IconWrapper.Image -> Image(
          painter = painterResource((action.icon as IconWrapper.Image).resourceId),
          contentDescription = action.label.getString(),
        )
        is IconWrapper.Icon -> Icon(
          painter = painterResource((action.icon as IconWrapper.Icon).resourceId),
          tint = iconTint,
          contentDescription = action.label.getString(),
        )
        is IconWrapper.Vector -> Icon(
          imageVector = (action.icon as IconWrapper.Vector).vector,
          tint = iconTint,
          contentDescription = action.label.getString(),
        )
      }

      Text(
        text = action.label.getString(),
        textAlign = TextAlign.Center,
        style = MaterialTheme.typography.bodySmall,
      )
    }
  }
}
