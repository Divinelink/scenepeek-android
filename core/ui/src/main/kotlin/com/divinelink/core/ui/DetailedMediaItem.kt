package com.divinelink.core.ui

import android.content.res.Configuration
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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import com.divinelink.core.designsystem.theme.AppTheme
import com.divinelink.core.designsystem.theme.dimensions
import com.divinelink.core.model.media.MediaItem
import com.divinelink.core.ui.provider.MediaItemPreviewParameterProvider

@Composable
fun DetailedMediaItem(
  modifier: Modifier = Modifier,
  mediaItem: MediaItem.Media,
  onClick: (MediaItem.Media) -> Unit,
) {
  val offset = MaterialTheme.dimensions.keyline_28
  Card(
    modifier = modifier,
    onClick = { onClick(mediaItem) },
    colors = CardDefaults.cardColors(containerColor = Color.Transparent)
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
            path = mediaItem.posterPath
          )
          MediaRatingItem(
            modifier = Modifier
              .align(Alignment.BottomStart)
              .offset(y = offset)
              .padding(start = MaterialTheme.dimensions.keyline_8),
            rating = mediaItem.rating,
            size = RatingSize.MEDIUM
          )
        }
        Spacer(modifier = Modifier.height(offset))
      }

      Column(
        Modifier
          .fillMaxWidth()
          .padding(start = MaterialTheme.dimensions.keyline_12),
      ) {
        Text(
          text = mediaItem.name,
          style = MaterialTheme.typography.titleMedium,
        )
        if (mediaItem.releaseDate.isNotEmpty()) {
          Text(
            modifier = Modifier.padding(top = MaterialTheme.dimensions.keyline_4),
            text = mediaItem.releaseDate,
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.80f),
          )
        }

        Text(
          modifier = Modifier.padding(top = MaterialTheme.dimensions.keyline_8),
          text = mediaItem.overview,
          style = MaterialTheme.typography.bodyMedium,
          maxLines = 5,
          overflow = TextOverflow.Ellipsis
        )
      }
    }
  }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_NO)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun DetailedMediaItemPreview(
  @PreviewParameter(MediaItemPreviewParameterProvider::class)
  mediaItem: MediaItem.Media
) {
  AppTheme {
    Surface {
      DetailedMediaItem(
        mediaItem = mediaItem,
        onClick = {}
      )
    }
  }
}
