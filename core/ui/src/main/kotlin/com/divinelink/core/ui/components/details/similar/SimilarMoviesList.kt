package com.divinelink.core.ui.components.details.similar

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
import com.divinelink.core.designsystem.theme.ListPaddingValues
import com.divinelink.core.designsystem.theme.dimensions
import com.divinelink.core.model.media.MediaItem
import com.divinelink.core.ui.R
import com.divinelink.core.ui.TestTags
import com.divinelink.core.ui.components.MediaItem

@Composable
fun SimilarMoviesList(
  movies: List<MediaItem.Media>,
  onSimilarMovieClicked: (MediaItem.Media) -> Unit,
) {
  Column(
    modifier = Modifier
      .padding(vertical = MaterialTheme.dimensions.keyline_16)
      .fillMaxWidth(),
  ) {
    Text(
      modifier = Modifier
        .padding(horizontal = MaterialTheme.dimensions.keyline_12),
      style = MaterialTheme.typography.titleLarge,
      fontWeight = FontWeight.Bold,
      text = stringResource(id = R.string.details__more_like_this),
    )

    LazyRow(
      modifier = Modifier.testTag(TestTags.Details.SIMILAR_MOVIES_LIST),
      horizontalArrangement = Arrangement.spacedBy(MaterialTheme.dimensions.keyline_8),
      contentPadding = ListPaddingValues,
    ) {
      items(
        items = movies,
        key = { it.id },
      ) { similarMovie ->
        MediaItem(
          media = similarMovie,
          onMediaItemClick = onSimilarMovieClicked,
          onLikeMediaClick = {},
        )
      }
    }
  }
}
