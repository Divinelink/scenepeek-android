@file:Suppress("MagicNumber")
@file:OptIn(ExperimentalLayoutApi::class)

package com.divinelink.core.ui.components

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
import androidx.compose.foundation.layout.navigationBarsIgnoringVisibility
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.windowInsetsBottomHeight
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.divinelink.core.designsystem.theme.AppTheme
import com.divinelink.core.designsystem.theme.dimensions
import com.divinelink.core.model.media.MediaItem
import com.divinelink.core.ui.MediaDetailsContent
import com.divinelink.core.ui.Previews
import com.divinelink.core.ui.R

const val MOVIE_BOTTOM_SHEET_TAG = "MOVIE_DETAILS_BOTTOM_SHEET_TAG"
const val DETAILS_BUTTON_TAG = "DETAILS_AND_MORE_BUTTON_TAG"

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
    onDismissRequest = onDismissRequest,
  ) {
    MovieBottomSheetContent(
      media = movie,
      onContentClicked = onContentClicked,
      onMarkAsFavoriteClicked = onMarkAsFavoriteClicked,
    )
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
        .navigationBarsPadding(),
    ) {
      item {
        MediaDetailsContent(
          modifier = Modifier.wrapContentHeight(),
          media = media,
          onMarkAsFavoriteClicked = onMarkAsFavoriteClicked,
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
        onContentClicked = onContentClicked,
      )

      Spacer(Modifier.windowInsetsBottomHeight(WindowInsets.navigationBarsIgnoringVisibility))
    }
  }
}

@Composable
private fun DetailsButton(
  modifier: Modifier = Modifier,
  movie: MediaItem.Media,
  onContentClicked: (MediaItem.Media) -> Unit,
) {
  Surface(
    modifier = modifier
      .testTag(DETAILS_BUTTON_TAG)
      .clickable { onContentClicked(movie) }
      .fillMaxWidth()
      .padding(
        horizontal = MaterialTheme.dimensions.keyline_16,
      ),
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
@Previews
private fun HomeContentPreview() {
  AppTheme {
    Surface {
      MovieBottomSheetContent(
        media = MediaItem.Media.TV(
          id = 0,
          posterPath = "",
          releaseDate = "2023",
          name = "Puss in Boots: The Last Wish",
          voteAverage = 4.6,
          voteCount = 1000,
          isFavorite = true,
          overview = "Puss in Boots discovers that his passion for adventure has taken its toll: " +
            "He has burned through eight of his nine lives, leaving him with only one life left." +
            " Puss sets out on an epic journey to find the mythical Last Wish and restore " +
            "his nine lives.",
        ),
        onContentClicked = {},
        onMarkAsFavoriteClicked = {},
      )
    }
  }
}
