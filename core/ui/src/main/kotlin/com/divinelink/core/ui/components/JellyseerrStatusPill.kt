package com.divinelink.core.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import com.divinelink.core.designsystem.theme.colors
import com.divinelink.core.designsystem.theme.dimensions
import com.divinelink.core.designsystem.theme.shape
import com.divinelink.core.model.jellyseerr.media.JellyseerrStatus
import com.divinelink.core.ui.TestTags
import com.divinelink.core.ui.conditional

@Composable
fun JellyseerrStatusPill(
  modifier: Modifier = Modifier,
  status: JellyseerrStatus,
  onClick: (() -> Unit)? = null,
) {
  val color = when (status) {
    JellyseerrStatus.Media.UNKNOWN -> return
    JellyseerrStatus.Request.UNKNOWN -> return
    JellyseerrStatus.Season.UNKNOWN -> return
    JellyseerrStatus.Media.PENDING -> MaterialTheme.colors.limeYellow
    JellyseerrStatus.Media.PROCESSING -> MaterialTheme.colors.vibrantPurple
    JellyseerrStatus.Media.PARTIALLY_AVAILABLE -> MaterialTheme.colors.emeraldGreen
    JellyseerrStatus.Media.AVAILABLE -> MaterialTheme.colors.emeraldGreen
    JellyseerrStatus.Media.DELETED -> MaterialTheme.colors.crimsonRed
    JellyseerrStatus.Request.PENDING -> MaterialTheme.colors.limeYellow
    JellyseerrStatus.Request.APPROVED -> MaterialTheme.colors.emeraldGreen
    JellyseerrStatus.Request.DECLINED -> MaterialTheme.colors.crimsonRed
    JellyseerrStatus.Request.FAILED -> MaterialTheme.colors.crimsonRed
    JellyseerrStatus.Season.PENDING -> MaterialTheme.colors.limeYellow
    JellyseerrStatus.Season.PROCESSING -> MaterialTheme.colors.vibrantPurple
  }

  Text(
    text = stringResource(status.resourceId),
    color = Color.White,
    style = MaterialTheme.typography.labelSmall,
    maxLines = 1,
    overflow = TextOverflow.Ellipsis,
    modifier = modifier
      .testTag(TestTags.Components.STATUS_PILL.format(stringResource(status.resourceId)))
      .background(
        shape = MaterialTheme.shape.rounded,
        color = color.copy(alpha = 0.8f),
      )
      .clip(MaterialTheme.shape.rounded)
      .conditional(
        condition = onClick != null,
        ifTrue = {
          clickable(onClick = onClick!!)
        },
      )
      .border(
        width = MaterialTheme.dimensions.keyline_1,
        color = color,
        shape = MaterialTheme.shape.rounded,
      )
      .padding(
        vertical = if (onClick == null) {
          MaterialTheme.dimensions.keyline_4
        } else {
          MaterialTheme.dimensions.keyline_6
        },
        horizontal = MaterialTheme.dimensions.keyline_8,
      ),
  )
}
