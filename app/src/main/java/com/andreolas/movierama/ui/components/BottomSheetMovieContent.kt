@file:Suppress("MagicNumber")
@file:OptIn(ExperimentalLayoutApi::class)

package com.andreolas.movierama.ui.components

import android.content.res.Configuration
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.navigationBarsIgnoringVisibility
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.windowInsetsBottomHeight
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
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
import com.andreolas.core.designsystem.theme.AppTheme
import com.andreolas.core.designsystem.theme.dimensions
import com.andreolas.core.designsystem.theme.shape
import com.andreolas.movierama.ExcludeFromKoverReport
import com.andreolas.movierama.R
import com.andreolas.movierama.base.communication.ApiConstants
import com.andreolas.movierama.ui.components.media.MediaRatingItem
import com.divinelink.core.model.media.MediaItem

const val MOVIE_BOTTOM_SHEET_TAG = "MOVIE_DETAILS_BOTTOM_SHEET_TAG"
const val DETAILS_BUTTON_TAG = "DETAILS_AND_MORE_BUTTON_TAG"
const val BOTTOM_SHEET_MARK_AS_FAVORITE = "MARK_AS_FAVORITE_BUTTON"

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ModalBottomSheetMovieContent(
  sheetState: SheetState = rememberModalBottomSheetState(),
  movie: MediaItem,
  onContentClicked: (MediaItem.Media) -> Unit,
  onMarkAsFavoriteClicked: (MediaItem.Media) -> Unit,
  onDismissRequest: () -> Unit,
) {
  ModalBottomSheet(
    sheetState = sheetState,
    onDismissRequest = onDismissRequest
  ) {
    MovieBottomSheetContent(
      media = movie,
      onContentClicked = onContentClicked,
      onMarkAsFavoriteClicked = onMarkAsFavoriteClicked,
    )
  }
}

@Composable
private fun MediaDetailsContent(
  modifier: Modifier,
  movie: MediaItem.Media,
  onMarkAsFavoriteClicked: (MediaItem.Media) -> Unit,
) {
  Row(
    modifier = modifier
      .testTag(MOVIE_BOTTOM_SHEET_TAG)
      .fillMaxWidth(),
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
        .padding(MaterialTheme.dimensions.keyline_12),
    ) {
      Row(
        modifier = Modifier.fillMaxWidth()
      ) {
        Text(
          modifier = Modifier
            .padding(end = MaterialTheme.dimensions.keyline_8)
            .weight(4f),
          text = movie.name,
          style = MaterialTheme.typography.titleLarge,
        )

        // FIXME null check for isFavorite
        movie.isFavorite?.let {
          LikeButton(
            modifier = Modifier
              .testTag(BOTTOM_SHEET_MARK_AS_FAVORITE)
              .clip(MaterialTheme.shape.roundedShape),
            isFavorite = it,
            transparentBackground = true,
            onClick = { onMarkAsFavoriteClicked(movie) },
          )
        }
      }

      Column {
        Row(
          modifier = modifier.padding(top = MaterialTheme.dimensions.keyline_4),
          horizontalArrangement = Arrangement.Center,
          verticalAlignment = Alignment.CenterVertically,
        ) {
          MediaRatingItem(rating = movie.rating)
          Spacer(modifier = Modifier.width(MaterialTheme.dimensions.keyline_16))
          Text(
            text = movie.releaseDate,
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.80f),
          )
        }

        Text(
          modifier = Modifier.padding(
            horizontal = MaterialTheme.dimensions.keyline_8
          ),
          text = movie.overview,
          style = MaterialTheme.typography.bodyMedium,
        )
      }
    }
  }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun MovieBottomSheetContent(
  media: MediaItem,
  onContentClicked: (MediaItem.Media) -> Unit,
  onMarkAsFavoriteClicked: (MediaItem.Media) -> Unit,
) {
  if (media !is MediaItem.Media) return

  val detailsButtonSize = MaterialTheme.dimensions.keyline_56

  Box {
    LazyColumn(
      modifier = Modifier
        .padding(bottom = detailsButtonSize)
        .navigationBarsPadding()
    ) {

      item {
        MediaDetailsContent(
          modifier = Modifier.wrapContentHeight(),
          movie = media,
          onMarkAsFavoriteClicked = onMarkAsFavoriteClicked
        )
      }

      item {
        Spacer(Modifier.windowInsetsBottomHeight(WindowInsets.navigationBarsIgnoringVisibility))
      }
    }

    Column(
      modifier = Modifier.align(Alignment.BottomCenter),
    ) {
      DetailsButton(
        modifier = Modifier.height(detailsButtonSize),
        movie = media,
        onContentClicked = onContentClicked
      )

      Spacer(Modifier.windowInsetsBottomHeight(WindowInsets.navigationBarsIgnoringVisibility))
    }
  }
}

@Composable
private fun DetailsButton(
  modifier: Modifier = Modifier,
  movie: MediaItem.Media,
  onContentClicked: (MediaItem.Media) -> Unit
) {
  Surface(
    modifier = modifier
      .testTag(DETAILS_BUTTON_TAG)
      .clickable { onContentClicked(movie) }
      .fillMaxWidth()
      .padding(
        horizontal = MaterialTheme.dimensions.keyline_16,
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
          .padding(start = MaterialTheme.dimensions.keyline_8),
        text = stringResource(id = R.string.movie_extra_details),
        style = MaterialTheme.typography.titleSmall,
        color = MaterialTheme.colorScheme.onSurface,
      )

      Spacer(modifier = Modifier.weight(1f))

      Icon(
        painter = painterResource(id = R.drawable.ic_chevron_right),
        tint = MaterialTheme.colorScheme.onSurface,
        contentDescription = stringResource(id = R.string.popular_movie__rating),
      )
    }
  }
}

@Composable
@ExcludeFromKoverReport
@Preview(uiMode = Configuration.UI_MODE_NIGHT_NO)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
private fun HomeContentPreview() {
  AppTheme {
    Surface {
      MovieBottomSheetContent(
        media = MediaItem.Media.TV(
          id = 0,
          posterPath = "",
          releaseDate = "2023",
          name = "Puss in Boots: The Last Wish",
          rating = "4.6",
          isFavorite = true,
          overview = "Puss in Boots discovers that his passion for adventure has taken its toll: " +
            "He has burned through eight of his nine lives, leaving him with only one life left." +
            " Puss sets out on an epic journey to find the mythical Last Wish and restore " +
            "his nine lives."
        ),
        onContentClicked = {},
        onMarkAsFavoriteClicked = {},
      )
    }
  }
}
