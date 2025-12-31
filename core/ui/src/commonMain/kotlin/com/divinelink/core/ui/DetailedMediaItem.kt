package com.divinelink.core.ui

import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import com.divinelink.core.designsystem.theme.AppTheme
import com.divinelink.core.designsystem.theme.dimensions
import com.divinelink.core.model.media.MediaItem
import com.divinelink.core.ui.media.MediaImage
import com.divinelink.core.ui.provider.MediaItemParameterProvider

@Composable
fun DetailedMediaItem(
  modifier: Modifier = Modifier,
  mediaItem: MediaItem.Media,
  onClick: (MediaItem.Media) -> Unit,
  onLongClick: (MediaItem.Media) -> Unit,
) {
  Card(
    modifier = modifier
      .clip(MaterialTheme.shapes.large)
      .combinedClickable(
        onClick = { onClick(mediaItem) },
        onLongClick = { onLongClick(mediaItem) },
      ),
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
        Modifier
          .fillMaxWidth()
          .padding(start = MaterialTheme.dimensions.keyline_12),
      ) {
        FlowRow(
          horizontalArrangement = Arrangement.spacedBy(MaterialTheme.dimensions.keyline_4),
          itemVerticalAlignment = Alignment.CenterVertically,
        ) {
          Text(
            text = mediaItem.name,
            style = MaterialTheme.typography.titleMedium,
          )
        }
        if (mediaItem.releaseDate.isNotEmpty()) {
          Text(
            modifier = Modifier.padding(top = MaterialTheme.dimensions.keyline_4),
            text = mediaItem.releaseDate,
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
          )
        }

        Text(
          modifier = Modifier.padding(top = MaterialTheme.dimensions.keyline_8),
          text = mediaItem.overview,
          style = MaterialTheme.typography.bodyMedium,
          maxLines = 5,
          overflow = TextOverflow.Ellipsis,
        )
      }
    }
  }
}

@Previews
@Composable
fun DetailedMediaItemPreview(
  @PreviewParameter(MediaItemParameterProvider::class) mediaItem: MediaItem.Media,
) {
  AppTheme {
    Surface {
      DetailedMediaItem(
        mediaItem = mediaItem,
        onClick = {},
        onLongClick = {},
      )
    }
  }
}
