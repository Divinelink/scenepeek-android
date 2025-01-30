package com.divinelink.core.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.offset
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import com.divinelink.core.designsystem.theme.AppTheme
import com.divinelink.core.designsystem.theme.dimensions
import com.divinelink.core.model.media.MediaItem
import com.divinelink.core.ui.provider.MediaItemParameterProvider
import com.divinelink.core.ui.rating.RatingSize
import com.divinelink.core.ui.rating.TMDBRatingItem

@Composable
fun CreditMediaItem(
  modifier: Modifier = Modifier,
  mediaItem: MediaItem.Media,
  subtitle: String? = null,
  onClick: (MediaItem.Media) -> Unit,
) {
  val offset = MaterialTheme.dimensions.keyline_28
  Card(
    modifier = modifier.testTag(TestTags.Person.CREDIT_MEDIA_ITEM.format(mediaItem.name)),
    onClick = { onClick(mediaItem) },
    colors = CardDefaults.cardColors(containerColor = Color.Transparent),
  ) {
    Row(
      modifier = Modifier
        .padding(MaterialTheme.dimensions.keyline_8)
        .heightIn(max = 200.dp)
        .wrapContentSize()
        .fillMaxWidth(),
    ) {
      Column {
        Box(
          modifier = Modifier.widthIn(max = 80.dp),
          contentAlignment = Alignment.Center,
        ) {
          MovieImage(
            path = mediaItem.posterPath,
          )
          if (mediaItem.voteAverage > 0 && mediaItem.voteCount > 0) {
            TMDBRatingItem(
              modifier = Modifier
                .align(Alignment.BottomStart)
                .offset(y = offset)
                .padding(start = MaterialTheme.dimensions.keyline_8),
              rating = mediaItem.voteAverage,
              voteCount = mediaItem.voteCount,
              size = RatingSize.MEDIUM,
            )
          }
        }
        Spacer(modifier = Modifier.height(offset))
      }

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
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.80f),
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
private fun DetailedMediaItemPreview(
  @PreviewParameter(MediaItemParameterProvider::class) mediaItem: MediaItem.Media,
) {
  AppTheme {
    Surface {
      CreditMediaItem(
        mediaItem = mediaItem,
        subtitle = "Joy (voice)",
        onClick = {},
      )
    }
  }
}
