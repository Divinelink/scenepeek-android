package com.divinelink.core.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import coil3.request.CachePolicy
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.divinelink.core.commons.ApiConstants
import com.divinelink.core.designsystem.theme.dimensions
import com.divinelink.core.designsystem.theme.shape
import com.divinelink.core.model.media.MediaItem
import com.divinelink.core.ui.rating.TMDBRatingItem

const val BOTTOM_SHEET_MARK_AS_FAVORITE = "MARK_AS_FAVORITE_BUTTON"

@Composable
fun MediaDetailsContent(
  modifier: Modifier,
  media: MediaItem.Media,
  onMarkAsFavoriteClicked: (MediaItem.Media) -> Unit,
) {
  Row(
    modifier = modifier.fillMaxWidth(),
  ) {
    AsyncImage(
      modifier = Modifier
        .widthIn(max = 160.dp)
        .heightIn(max = 180.dp)
        .padding(horizontal = MaterialTheme.dimensions.keyline_12)
        .padding(top = MaterialTheme.dimensions.keyline_12)
        .weight(1f)
        .align(Alignment.Top)
        .clip(RoundedCornerShape(MaterialTheme.dimensions.keyline_4))
        .clipToBounds(),
      model = ImageRequest.Builder(LocalContext.current)
        .memoryCachePolicy(CachePolicy.ENABLED)
        .diskCachePolicy(CachePolicy.ENABLED)
        .data(ApiConstants.TMDB_IMAGE_URL + media.posterPath)
        .crossfade(true)
        .build(),
      placeholder = painterResource(R.drawable.core_ui_ic_movie_placeholder),
      error = painterResource(R.drawable.core_ui_ic_movie_placeholder),
      contentDescription = stringResource(R.string.core_ui_ok),
      contentScale = ContentScale.Fit,
    )

    Column(
      Modifier
        .weight(3f)
        .padding(MaterialTheme.dimensions.keyline_12),
    ) {
      Row(
        modifier = Modifier.fillMaxWidth(),
      ) {
        Text(
          modifier = Modifier
            .padding(end = MaterialTheme.dimensions.keyline_8)
            .weight(4f),
          text = media.name,
          style = MaterialTheme.typography.titleLarge,
        )

        // FIXME null check for isFavorite
        media.isFavorite?.let {
          FavoriteButton(
            modifier = Modifier
              .testTag(BOTTOM_SHEET_MARK_AS_FAVORITE)
              .clip(MaterialTheme.shape.rounded),
            isFavorite = it,
            transparentBackground = true,
            onClick = { onMarkAsFavoriteClicked(media) },
          )
        }
      }

      Column {
        Row(
          modifier = modifier.padding(top = MaterialTheme.dimensions.keyline_4),
          horizontalArrangement = Arrangement.Center,
          verticalAlignment = Alignment.CenterVertically,
        ) {
          TMDBRatingItem(rating = media.voteAverage, voteCount = media.voteCount)
          Spacer(modifier = Modifier.width(MaterialTheme.dimensions.keyline_16))
          Text(
            text = media.releaseDate,
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.80f),
          )
        }

        Text(
          modifier = Modifier.padding(
            horizontal = MaterialTheme.dimensions.keyline_8,
          ),
          text = media.overview,
          style = MaterialTheme.typography.bodyMedium,
        )
      }
    }
  }
}
