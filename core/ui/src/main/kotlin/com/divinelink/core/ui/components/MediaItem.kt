package com.divinelink.core.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.PreviewParameter
import com.divinelink.core.designsystem.theme.AppTheme
import com.divinelink.core.designsystem.theme.dimensions
import com.divinelink.core.designsystem.theme.shape
import com.divinelink.core.model.media.MediaItem
import com.divinelink.core.ui.FavoriteButton
import com.divinelink.core.ui.MovieImage
import com.divinelink.core.ui.Previews
import com.divinelink.core.ui.provider.MediaItemParameterProvider
import com.divinelink.core.ui.rating.TMDBRatingItem

const val MOVIE_CARD_ITEM_TAG = "MOVIE_CARD_ITEM_TAG"

@Composable
fun MediaItem(
  modifier: Modifier = Modifier,
  media: MediaItem.Media,
  subtitle: String? = null,
  fullDate: Boolean = true,
  onMediaItemClick: (MediaItem.Media) -> Unit,
  onLikeMediaClick: () -> Unit = {},
) {
  val offset = MaterialTheme.dimensions.keyline_28

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
    Box(
      contentAlignment = Alignment.Center,
      modifier = Modifier
        .fillMaxWidth()
        .wrapContentHeight(),
    ) {
      MovieImage(
        path = media.posterPath,
      )
      media.isFavorite?.let { isFavorite ->
        FavoriteButton(
          modifier = Modifier.align(Alignment.TopStart),
          isFavorite = isFavorite,
          onClick = onLikeMediaClick,
        )
      }

      TMDBRatingItem(
        modifier = Modifier
          .align(Alignment.BottomStart)
          .offset(y = offset)
          .padding(start = MaterialTheme.dimensions.keyline_8),
        rating = media.voteAverage,
        voteCount = media.voteCount,
      )
    }

    Spacer(modifier = Modifier.height(MaterialTheme.dimensions.keyline_4))

    Text(
      modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = MaterialTheme.dimensions.keyline_8)
        .offset(y = offset)
        .height(MaterialTheme.dimensions.keyline_58),
      text = media.name,
      maxLines = 3,
      overflow = TextOverflow.Ellipsis,
      style = MaterialTheme.typography.titleSmall,
      color = MaterialTheme.colorScheme.onSurface,
    )

    subtitle?.let {
      Text(
        modifier = Modifier
          .fillMaxWidth()
          .offset(y = offset)
          .padding(top = MaterialTheme.dimensions.keyline_4)
          .padding(horizontal = MaterialTheme.dimensions.keyline_8),
        text = subtitle,
        maxLines = 1,
        style = MaterialTheme.typography.bodySmall,
        overflow = TextOverflow.Ellipsis,
        color = MaterialTheme.colorScheme.primary,
      )
    }

    Text(
      modifier = Modifier
        .offset(y = offset)
        .padding(
          vertical = MaterialTheme.dimensions.keyline_4,
          horizontal = MaterialTheme.dimensions.keyline_8,
        ),
      text = if (fullDate) {
        media.releaseDate
      } else {
        media.releaseDate.take(4)
      },
      style = MaterialTheme.typography.labelMedium,
      color = MaterialTheme.colorScheme.onSurfaceVariant,
    )

    Spacer(modifier = Modifier.height(offset))
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
        onLikeMediaClick = {},
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
        onLikeMediaClick = {},
      )
    }
  }
}
