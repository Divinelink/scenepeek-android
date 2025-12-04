package com.divinelink.feature.details.media.ui.ratings

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PeopleAlt
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.testTag
import com.divinelink.core.commons.extensions.round
import com.divinelink.core.commons.extensions.toShortString
import com.divinelink.core.designsystem.theme.AppTheme
import com.divinelink.core.designsystem.theme.dimensions
import com.divinelink.core.model.details.rating.RatingCount
import com.divinelink.core.model.details.rating.RatingDetails
import com.divinelink.core.model.details.rating.RatingSource
import com.divinelink.core.ui.Previews
import com.divinelink.core.ui.TestTags
import com.divinelink.core.ui.UiString
import com.divinelink.core.ui.conditional
import com.divinelink.core.ui.extension.format
import com.divinelink.core.ui.provider.RatingCountParameterProvider
import com.divinelink.core.ui.resources.core_ui_percentage
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.PreviewParameter

@Composable
fun AllRatingsContent(
  onClick: (RatingSource) -> Unit,
  ratingCount: RatingCount,
) {
  FlowRow(
    modifier = Modifier
      .padding(vertical = MaterialTheme.dimensions.keyline_16)
      .fillMaxWidth(),
    horizontalArrangement = Arrangement.SpaceEvenly,
    verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimensions.keyline_8),
    maxItemsInEachRow = 3,
  ) {
    ratingCount.ratings.forEach {
      RatingItem(
        onClick = { onClick(it.key) },
        item = it.key to it.value,
      )
    }
  }
}

@Composable
private fun RatingItem(
  onClick: () -> Unit,
  item: Pair<RatingSource, RatingDetails>,
) {
  Column(
    verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimensions.keyline_8),
    horizontalAlignment = Alignment.CenterHorizontally,
  ) {
    Image(
      modifier = Modifier
        .clip(MaterialTheme.shapes.medium)
        .conditional(
          condition = item.first == RatingSource.TMDB ||
            item.first == RatingSource.IMDB ||
            item.first == RatingSource.TRAKT,
          ifTrue = { clickable { onClick() } },
        )
        .background(MaterialTheme.colorScheme.surfaceContainer)
        .padding(MaterialTheme.dimensions.keyline_8)
        .size(MaterialTheme.dimensions.keyline_48),
      painter = painterResource(item.first.iconRes),
      contentDescription = item.first.name,
    )

    Column(
      verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimensions.keyline_4),
      horizontalAlignment = Alignment.CenterHorizontally,
    ) {
      when (item.second) {
        RatingDetails.Initial -> RatingContentShimmer(
          modifier = Modifier.testTag(TestTags.Rating.RATING_SOURCE_SKELETON.format(item.first)),
          withVoteCount = item.first == RatingSource.IMDB ||
            item.first == RatingSource.TMDB ||
            item.first == RatingSource.TRAKT,
        )
        is RatingDetails.Score -> {
          val score = item.second as RatingDetails.Score

          Text(
            style = MaterialTheme.typography.titleMedium,
            text = score.voteAverage.round(1).toString(),
          )
          Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(MaterialTheme.dimensions.keyline_4),
          ) {
            Text(
              text = score.voteCount.toShortString(),
              style = MaterialTheme.typography.titleSmall,
              color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f),
            )

            Icon(
              modifier = Modifier.size(MaterialTheme.dimensions.keyline_16),
              imageVector = Icons.Default.PeopleAlt,
              contentDescription = null,
              tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f),
            )
          }
        }
        RatingDetails.Unavailable -> Text(
          style = MaterialTheme.typography.titleMedium,
          text = "-",
        )
        is RatingDetails.Rating -> Text(
          style = MaterialTheme.typography.titleMedium,
          text = when (item.first) {
            RatingSource.TMDB -> (item.second as RatingDetails.Rating).value.toString()
            RatingSource.IMDB -> (item.second as RatingDetails.Rating).value.toString()
            RatingSource.TRAKT -> (item.second as RatingDetails.Rating).value.toString()
            RatingSource.RT -> stringResource(
              UiString.core_ui_percentage,
              (item.second as RatingDetails.Rating).value,
            )
            RatingSource.METACRITIC -> (item.second as RatingDetails.Rating).value.toString()
          },
        )
      }
    }
  }
}

@Previews
@Composable
fun AllRatingsContentPreview(
  @PreviewParameter(RatingCountParameterProvider::class) ratingCount: RatingCount,
) {
  AppTheme {
    Surface {
      AllRatingsContent(
        onClick = {},
        ratingCount = ratingCount,
      )
    }
  }
}
