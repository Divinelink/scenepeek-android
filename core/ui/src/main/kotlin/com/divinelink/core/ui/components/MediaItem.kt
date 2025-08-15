package com.divinelink.core.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.PreviewParameter
import com.divinelink.core.designsystem.theme.AppTheme
import com.divinelink.core.designsystem.theme.dimensions
import com.divinelink.core.designsystem.theme.shape
import com.divinelink.core.model.media.MediaItem
import com.divinelink.core.ui.Previews
import com.divinelink.core.ui.media.MediaImage
import com.divinelink.core.ui.provider.MediaItemParameterProvider

const val MOVIE_CARD_ITEM_TAG = "MOVIE_CARD_ITEM_TAG"

@Composable
fun MediaItem(
  modifier: Modifier = Modifier,
  media: MediaItem.Media,
  subtitle: String? = null,
  showDate: Boolean = false,
  onMediaItemClick: (MediaItem.Media) -> Unit,
) {
  Card(
    shape = MaterialTheme.shape.medium,
    modifier = modifier
      .testTag(MOVIE_CARD_ITEM_TAG)
      .widthIn(max = MaterialTheme.dimensions.shortMediaCard)
      .clip(MaterialTheme.shape.medium)
      .clipToBounds()
      .clickable {
        onMediaItemClick(media)
      },
    colors = CardDefaults.cardColors(containerColor = Color.Transparent),
  ) {
    MediaImage(media = media)

    Spacer(modifier = Modifier.height(MaterialTheme.dimensions.keyline_4))

    Text(
      modifier = Modifier
        .fillMaxWidth()
        .padding(bottom = MaterialTheme.dimensions.keyline_4)
        .padding(horizontal = MaterialTheme.dimensions.keyline_8),
      text = media.name,
      maxLines = 3,
      overflow = TextOverflow.Ellipsis,
      style = MaterialTheme.typography.bodySmall,
      fontWeight = MaterialTheme.typography.titleSmall.fontWeight,
      color = MaterialTheme.colorScheme.onSurface,
      textAlign = TextAlign.Center,
    )

    if (!subtitle.isNullOrBlank()) {
      Text(
        modifier = Modifier
          .fillMaxWidth()
          .padding(horizontal = MaterialTheme.dimensions.keyline_8),
        text = subtitle,
        maxLines = 1,
        style = MaterialTheme.typography.labelSmall,
        overflow = TextOverflow.Ellipsis,
        color = MaterialTheme.colorScheme.primary,
        textAlign = TextAlign.Center,
      )
    }

    if (showDate) {
      Text(
        modifier = Modifier
          .fillMaxWidth()
          .padding(vertical = MaterialTheme.dimensions.keyline_4)
          .padding(horizontal = MaterialTheme.dimensions.keyline_8),
        text = media.releaseDate.take(4),
        style = MaterialTheme.typography.labelSmall,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
        textAlign = TextAlign.Center,
      )
    }
  }
}

@Composable
@Previews
fun MediaItemPreview(
  @PreviewParameter(MediaItemParameterProvider::class) mediaItem: MediaItem.Media,
) {
  AppTheme {
    Surface {
      MediaItem(
        modifier = Modifier,
        media = mediaItem,
        onMediaItemClick = {},
      )
    }
  }
}

@Composable
@Previews
fun MediaItemWithSubtitlePreview(
  @PreviewParameter(MediaItemParameterProvider::class) mediaItem: MediaItem.Media,
) {
  AppTheme {
    Surface {
      MediaItem(
        modifier = Modifier,
        media = mediaItem,
        subtitle = "Matthew Walkers",
        onMediaItemClick = {},
      )
    }
  }
}
