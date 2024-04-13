package com.andreolas.movierama.ui.components.details.similar

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.andreolas.movierama.R
import com.andreolas.movierama.home.domain.model.MediaItem
import com.andreolas.movierama.ui.components.MediaItem
import com.andreolas.movierama.ui.theme.ListPaddingValues

const val SIMILAR_MOVIES_SCROLLABLE_LIST = "SIMILAR_MOVIES_LAZY_ROW_TAG"

@Composable
fun SimilarMoviesList(
  movies: List<MediaItem.Media>,
  onSimilarMovieClicked: (MediaItem.Media) -> Unit,
) {
  Column(
    modifier = Modifier
      .padding(top = 16.dp, bottom = 16.dp)
      .fillMaxWidth(),
  ) {
    Text(
      modifier = Modifier
        .padding(start = 12.dp, end = 12.dp),
      style = MaterialTheme.typography.titleLarge,
      fontWeight = FontWeight.Bold,
      text = stringResource(id = R.string.details__more_like_this),
    )

    LazyRow(
      modifier = Modifier.testTag(SIMILAR_MOVIES_SCROLLABLE_LIST),
      horizontalArrangement = Arrangement.spacedBy(8.dp),
      contentPadding = ListPaddingValues,
    ) {
      items(
        items = movies,
        key = {
          it.id
        }
      ) { similarMovie ->

        MediaItem(
          movie = similarMovie,
          onMovieItemClick = onSimilarMovieClicked,
          onLikeMovieClick = {},
        )
      }
    }
  }
}
