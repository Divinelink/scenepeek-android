@file:Suppress("LongMethod", "MagicNumber")

package com.andreolas.movierama.ui.components

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.CachePolicy
import coil.request.ImageRequest
import com.andreolas.movierama.ExcludeFromJacocoGeneratedReport
import com.andreolas.movierama.R
import com.andreolas.movierama.base.communication.ApiConstants
import com.andreolas.movierama.home.domain.model.MediaItem
import com.andreolas.movierama.ui.theme.AppTheme

const val MOVIE_BOTTOM_SHEET_TAG = "MOVIE_DETAILS_BOTTOM_SHEET_TAG"
const val DETAILS_BUTTON_TAG = "DETAILS_AND_MORE_BUTTON_TAG"
const val BOTTOM_SHEET_MARK_AS_FAVORITE = "MARK_AS_FAVORITE_BUTTON"

@Composable
fun BottomSheetMovieContent(
  modifier: Modifier = Modifier,
  movie: MediaItem,
  onContentClicked: (MediaItem.Media) -> Unit,
  onMarkAsFavoriteClicked: (MediaItem.Media) -> Unit,
) {
  if (movie is MediaItem.Media) {
      Column(modifier = Modifier.windowInsetsPadding(WindowInsets.navigationBars)) {
      Row(
        modifier = modifier
          .testTag(MOVIE_BOTTOM_SHEET_TAG)
          .fillMaxWidth(),
      ) {
        AsyncImage(
          modifier = Modifier
            .widthIn(max = 160.dp)
            .heightIn(max = 180.dp)
            .padding(top = 12.dp, start = 12.dp, bottom = 12.dp)
            .weight(1f)
            .align(Alignment.Top)
            .clip(RoundedCornerShape(4.dp))
            .clipToBounds(),
          model = ImageRequest.Builder(LocalContext.current)
            .memoryCachePolicy(CachePolicy.ENABLED)
            .diskCachePolicy(CachePolicy.ENABLED)
            .data(ApiConstants.TMDB_IMAGE_URL + movie.posterPath)
            .crossfade(true)
            .build(),
          placeholder = painterResource(R.drawable.ic_movie_placeholder),
          error = painterResource(R.drawable.ic_movie_placeholder),
          contentDescription = stringResource(R.string.ok),
          contentScale = ContentScale.Fit,
        )

        Column(
          Modifier
            .weight(3f)
            .padding(12.dp),
        ) {
          Row(
            modifier = Modifier.fillMaxWidth()
          ) {
            Text(
              modifier = Modifier
                .padding(end = 8.dp)
                .weight(4f),
              text = movie.name,
              style = MaterialTheme.typography.titleLarge,
            )

            LikeButton(
              modifier = Modifier
                .testTag(BOTTOM_SHEET_MARK_AS_FAVORITE)
                .clip(RoundedCornerShape(50.dp)),
              isFavorite = movie.isFavorite,
              transparentBackground = true,
              onClick = { onMarkAsFavoriteClicked(movie) },
            )
          }

          Column {
            Row(
              modifier = modifier.padding(top = 4.dp),
              horizontalArrangement = Arrangement.Center,
              verticalAlignment = Alignment.CenterVertically,
            ) {
              Rating(rating = movie.rating)
              Spacer(modifier = Modifier.width(16.dp))
              Text(
                text = movie.releaseDate,
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.80f),
              )
            }

            Text(
              modifier = Modifier.padding(
                top = 8.dp,
                bottom = 8.dp,
              ),
              text = movie.overview,
              style = MaterialTheme.typography.bodyMedium,
            )
          }
        }
      }
      Surface(
        modifier = Modifier
          .clickable { onContentClicked(movie) }
          .fillMaxWidth()
          .padding(
            vertical = 20.dp,
            horizontal = 12.dp,
          )
      ) {
        Row(
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.Start,
        ) {
          Icon(
            imageVector = Icons.Outlined.Info,
            tint = MaterialTheme.colorScheme.onSurface,
            contentDescription = stringResource(id = R.string.popular_movie__rating),
          )

          Text(
            modifier = Modifier
              .testTag(DETAILS_BUTTON_TAG)
              .padding(start = 8.dp),
            text = stringResource(id = R.string.movie_extra_details),
            style = MaterialTheme.typography.titleSmall,
            color = MaterialTheme.colorScheme.onSurface,
          )
        }
        Box(
          modifier = Modifier
            .background(MaterialTheme.colorScheme.onSurface.copy(0f)),
          contentAlignment = Alignment.CenterEnd,
        ) {
          Icon(
            painter = painterResource(id = R.drawable.ic_chevron_right),
            tint = MaterialTheme.colorScheme.onSurface,
            contentDescription = stringResource(id = R.string.popular_movie__rating),
          )
        }
      }
    }
  }
}

@Composable
@ExcludeFromJacocoGeneratedReport
@Preview(uiMode = Configuration.UI_MODE_NIGHT_NO, showBackground = true)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
fun HomeContentPreview() {
  AppTheme {
    Surface {
      BottomSheetMovieContent(
        modifier = Modifier,
        movie = MediaItem.Media.TV(
          id = 0,
          posterPath = "",
          releaseDate = "2023",
          name = "Puss in Boots: The Last Wish",
          rating = "7.66",
          isFavorite = true,
          overview = "Puss in Boots discovers that his passion for adventure has taken its toll: " +
            "He has burned through eight of his nine lives, leaving him with only one life left." +
            " Puss sets out on an epic journey to find the mythical Last Wish and restore his nine lives."
        ),
        onContentClicked = {},
        onMarkAsFavoriteClicked = {},
      )
    }
  }
}
