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
import androidx.compose.ui.text.style.TextOverflow
import com.divinelink.core.designsystem.theme.colors
import com.divinelink.core.designsystem.theme.dimensions
import com.divinelink.core.designsystem.theme.shape
import com.divinelink.core.model.jellyseerr.media.JellyseerrStatus

@Composable
fun JellyseerrStatusPill(
  modifier: Modifier = Modifier,
  status: JellyseerrStatus,
) {
  val color = when (status) {
    JellyseerrStatus.Media.UNKNOWN -> return
    JellyseerrStatus.Request.UNKNOWN -> return
    JellyseerrStatus.Media.PENDING -> MaterialTheme.colors.vibrantPurple
    JellyseerrStatus.Media.PROCESSING -> MaterialTheme.colors.vibrantPurple
    JellyseerrStatus.Media.PARTIALLY_AVAILABLE -> MaterialTheme.colors.emeraldGreen
    JellyseerrStatus.Media.AVAILABLE -> MaterialTheme.colors.emeraldGreen
    JellyseerrStatus.Media.DELETED -> MaterialTheme.colors.crimsonRed
    JellyseerrStatus.Request.PENDING -> MaterialTheme.colors.vibrantPurple
    JellyseerrStatus.Request.APPROVED -> MaterialTheme.colors.emeraldGreen
    JellyseerrStatus.Request.DECLINED -> MaterialTheme.colors.crimsonRed
    JellyseerrStatus.Request.FAILED -> MaterialTheme.colors.crimsonRed
  }

  Text(
    text = stringResource(status.resourceId),
    color = Color.White,
    style = MaterialTheme.typography.labelSmall,
    maxLines = 1,
    overflow = TextOverflow.Ellipsis,
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
