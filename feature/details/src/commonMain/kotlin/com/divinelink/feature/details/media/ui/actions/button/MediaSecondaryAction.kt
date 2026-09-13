package com.divinelink.feature.details.media.ui.actions.button

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
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
import com.divinelink.core.designsystem.theme.colors
import com.divinelink.core.designsystem.theme.dimensions
import com.divinelink.core.ui.IconWrapper
import com.divinelink.core.ui.components.expandablefab.FloatingActionButtonItem
import com.divinelink.core.ui.getString
import org.jetbrains.compose.resources.painterResource

@Composable
fun MediaSecondaryAction(
  modifier: Modifier = Modifier,
  containerColor: Color = Color.Unspecified,
  action: FloatingActionButtonItem,
) {
  Card(
    modifier = modifier
      .fillMaxWidth()
      .padding(
        vertical = MaterialTheme.dimensions.keyline_4,
        horizontal = MaterialTheme.dimensions.keyline_16,
      ),
    colors = CardDefaults.cardColors(
      containerColor = containerColor,
    ),
    onClick = action.onClick,
    border = if (action.seerrMode) {
      BorderStroke(
        width = MaterialTheme.dimensions.keyline_2,
        brush = MaterialTheme.colors.jellyseerrGradientBrush,
      )
    } else {
      null
    },
  ) {
    Row(
      modifier = Modifier.padding(vertical = MaterialTheme.dimensions.keyline_8),
      horizontalArrangement = Arrangement.spacedBy(MaterialTheme.dimensions.keyline_16),
      verticalAlignment = Alignment.CenterVertically,
    ) {
      Box(
        modifier = Modifier.padding(start = MaterialTheme.dimensions.keyline_16),
      ) {
        when (action.icon) {
          is IconWrapper.Image -> Image(
            painter = painterResource((action.icon as IconWrapper.Image).resourceId),
            contentDescription = action.label.getString(),
          )
          is IconWrapper.Icon -> Icon(
            painter = painterResource((action.icon as IconWrapper.Icon).resourceId),
            contentDescription = action.label.getString(),
          )
          is IconWrapper.Vector -> Icon(
            imageVector = (action.icon as IconWrapper.Vector).vector,
            contentDescription = action.label.getString(),
          )
        }
      }

      Text(
        modifier = Modifier.padding(vertical = MaterialTheme.dimensions.keyline_8),
        text = action.label.getString(),
        style = MaterialTheme.typography.bodyMedium,
        fontWeight = MaterialTheme.typography.titleSmall.fontWeight,
      )
    }
  }
}
