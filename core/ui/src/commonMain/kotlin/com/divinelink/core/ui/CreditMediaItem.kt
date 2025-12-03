package com.divinelink.core.ui

import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.divinelink.core.designsystem.theme.AppTheme
import com.divinelink.core.designsystem.theme.dimensions
import com.divinelink.core.model.media.MediaItem
import com.divinelink.core.ui.extension.format
import com.divinelink.core.ui.media.MediaImage
import com.divinelink.core.ui.provider.MediaItemParameterProvider
import org.jetbrains.compose.ui.tooling.preview.PreviewParameter

@Composable
fun CreditMediaItem(
  modifier: Modifier = Modifier,
  mediaItem: MediaItem.Media,
  subtitle: String? = null,
  onClick: (MediaItem.Media) -> Unit,
  onLongClick: (MediaItem.Media) -> Unit,
) {
  Card(
    modifier = modifier
      .clip(CardDefaults.shape)
      .combinedClickable(
        onClick = { onClick(mediaItem) },
        onLongClick = { onLongClick(mediaItem) },
      )
      .testTag(TestTags.Person.CREDIT_MEDIA_ITEM.format(mediaItem.name)),
    colors = CardDefaults.cardColors(containerColor = Color.Transparent),
  ) {
    Row(
      modifier = Modifier
        .padding(MaterialTheme.dimensions.keyline_8)
        .heightIn(max = 200.dp)
        .wrapContentSize()
        .fillMaxWidth(),
    ) {
      MediaImage(
        media = mediaItem,
        modifier = Modifier.widthIn(max = MaterialTheme.dimensions.keyline_96),
      )

      Column(
        modifier = Modifier
          .fillMaxWidth()
          .padding(start = MaterialTheme.dimensions.keyline_12),
        verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimensions.keyline_8),
      ) {
        if (mediaItem.releaseDate.isNotEmpty()) {
          Text(
            text = mediaItem.releaseDate.take(4),
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
          )
        }
        Text(
          text = mediaItem.name,
          style = MaterialTheme.typography.titleMedium,
        )

        subtitle?.let { subtitle ->
          Text(
            text = subtitle,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.primary,
            maxLines = 5,
            overflow = TextOverflow.Ellipsis,
          )
        }
      }
    }
  }
}

@Previews
@Composable
fun CreditMediaItemPreview(
  @PreviewParameter(MediaItemParameterProvider::class) mediaItem: MediaItem.Media,
) {
  AppTheme {
    Surface {
      CreditMediaItem(
        mediaItem = mediaItem,
        subtitle = "Joy (voice)",
        onClick = {},
        onLongClick = {},
      )
    }
  }
}
