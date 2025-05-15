package com.divinelink.feature.details.media.ui.forms.seasons

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.text.style.TextOverflow
import com.divinelink.core.designsystem.component.ScenePeekLazyColumn
import com.divinelink.core.designsystem.theme.dimensions
import com.divinelink.core.model.UIText
import com.divinelink.core.model.details.Season
import com.divinelink.core.ui.MovieImage
import com.divinelink.core.ui.TestTags
import com.divinelink.feature.details.R
import com.divinelink.feature.details.media.DetailsData
import com.divinelink.feature.details.media.ui.forms.FormEmptyContent

@Composable
fun SeasonsFormContent(
  modifier: Modifier = Modifier,
  title: String,
  reviews: DetailsData.Seasons,
) {
  if (reviews.items.isEmpty()) {
    FormEmptyContent(
      modifier = modifier,
      title = UIText.ResourceText(R.string.feature_details_no_seasons_available),
      description = UIText.ResourceText(R.string.feature_details_no_seasons_available_desc, title),
    )
  } else {
    ScenePeekLazyColumn(
      modifier = modifier.testTag(TestTags.Watchlist.WATCHLIST_CONTENT),
      contentPadding = PaddingValues(
        top = MaterialTheme.dimensions.keyline_16,
        start = MaterialTheme.dimensions.keyline_16,
        end = MaterialTheme.dimensions.keyline_16,
      ),
      verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimensions.keyline_16),
    ) {
      items(items = reviews.items) { item ->
        SeasonItem(
          modifier = Modifier,
          season = item,
        )
      }
    }
  }
}

@Composable
fun SeasonItem(
  modifier: Modifier = Modifier,
  season: Season,
) {
  Row(
    horizontalArrangement = Arrangement.spacedBy(MaterialTheme.dimensions.keyline_16),
  ) {
    MovieImage(
      modifier = Modifier.weight(1.35f),
      path = season.posterPath,
    )

    Column(
      modifier = modifier.weight(5f),
      verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimensions.keyline_8),
    ) {
      Text(
        text = season.name,
        style = MaterialTheme.typography.titleMedium,
      )

      Text(
        text = pluralStringResource(
          id = com.divinelink.core.ui.R.plurals.core_ui_episode_count,
          count = season.episodeCount,
          season.episodeCount,
        ),
        style = MaterialTheme.typography.bodySmall,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
      )

      Text(
        text = season.overview,
        style = MaterialTheme.typography.bodyMedium,
        maxLines = 6,
        overflow = TextOverflow.Ellipsis,
      )
    }
  }
}
