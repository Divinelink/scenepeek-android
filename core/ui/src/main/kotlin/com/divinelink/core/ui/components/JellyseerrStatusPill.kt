package com.divinelink.core.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import com.divinelink.core.designsystem.theme.colors
import com.divinelink.core.designsystem.theme.dimensions
import com.divinelink.core.designsystem.theme.shape
import com.divinelink.core.model.jellyseerr.media.JellyseerrMediaStatus

@Composable
fun JellyseerrStatusPill(
  modifier: Modifier = Modifier,
  status: JellyseerrMediaStatus,
) {
  val color = when (status) {
    JellyseerrMediaStatus.PENDING -> MaterialTheme.colors.vibrantPurple
    JellyseerrMediaStatus.PROCESSING -> MaterialTheme.colors.vibrantPurple
    JellyseerrMediaStatus.PARTIALLY_AVAILABLE -> MaterialTheme.colors.emeraldGreen
    JellyseerrMediaStatus.AVAILABLE -> MaterialTheme.colors.emeraldGreen
    JellyseerrMediaStatus.DELETED -> MaterialTheme.colors.crimsonRed
    JellyseerrMediaStatus.UNKNOWN -> return
  }

  Text(
    text = stringResource(status.resourceId),
    color = Color.White,
    style = MaterialTheme.typography.labelSmall,
    modifier = modifier
      .background(
        shape = MaterialTheme.shape.rounded,
        color = color.copy(alpha = 0.8f),
      )
      .border(
        width = MaterialTheme.dimensions.keyline_1,
        color = color,
        shape = MaterialTheme.shape.rounded,
      )
      .padding(
        vertical = MaterialTheme.dimensions.keyline_4,
        horizontal = MaterialTheme.dimensions.keyline_8,
      ),
  )
}
