package com.divinelink.feature.details.media.ui.actions.button

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
import androidx.compose.ui.text.style.TextOverflow
import com.divinelink.core.designsystem.theme.dimensions
import com.divinelink.core.ui.IconWrapper
import com.divinelink.core.ui.components.action.MediaActionButtonItem
import com.divinelink.core.ui.getString
import org.jetbrains.compose.resources.painterResource

@Composable
fun MediaMainAction(
  modifier: Modifier = Modifier,
  action: MediaActionButtonItem,
  containerColor: Color = Color.Unspecified,
) {
  Card(
    modifier = modifier
      .padding(MaterialTheme.dimensions.keyline_8),
    onClick = action.onClick,
    colors = CardDefaults.cardColors(
      containerColor = containerColor,
    ),
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
          contentDescription = action.label.getString(),
        )
        is IconWrapper.Vector -> Icon(
          imageVector = (action.icon as IconWrapper.Vector).vector,
          contentDescription = action.label.getString(),
        )
      }

      Text(
        text = action.label.getString(),
        textAlign = TextAlign.Center,
        overflow = TextOverflow.Ellipsis,
        maxLines = 2,
        minLines = 2,
        style = MaterialTheme.typography.bodySmall,
      )
    }
  }
}
