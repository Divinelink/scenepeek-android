package com.divinelink.feature.details.media.ui.forms.seasons

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.divinelink.core.designsystem.component.ScenePeekLazyColumn
import com.divinelink.core.designsystem.theme.AppTheme
import com.divinelink.core.designsystem.theme.LocalBottomNavigationPadding
import com.divinelink.core.designsystem.theme.dimensions
import com.divinelink.core.fixtures.details.season.SeasonFactory
import com.divinelink.core.model.UIText
import com.divinelink.core.model.details.Season
import com.divinelink.core.model.details.media.DetailsData
import com.divinelink.core.model.jellyseerr.media.JellyseerrStatus
import com.divinelink.core.ui.MovieImage
import com.divinelink.core.ui.Previews
import com.divinelink.core.ui.TestTags
import com.divinelink.core.ui.UiPlurals
import com.divinelink.core.ui.blankslate.BlankSlate
import com.divinelink.core.ui.blankslate.BlankSlateState
import com.divinelink.core.ui.components.JellyseerrStatusPill
import com.divinelink.core.ui.resources.core_ui_episode_count
import com.divinelink.feature.details.resources.Res
import com.divinelink.feature.details.resources.feature_details_no_seasons_available
import com.divinelink.feature.details.resources.feature_details_no_seasons_available_desc
import org.jetbrains.compose.resources.pluralStringResource

@Composable
fun SeasonsFormContent(
  modifier: Modifier = Modifier,
  title: String,
  reviews: DetailsData.Seasons,
  onClick: (seasonNumber: Int) -> Unit,
) {
  ScenePeekLazyColumn(
    modifier = modifier.testTag(TestTags.Details.Seasons.FORM),
    contentPadding = PaddingValues(top = MaterialTheme.dimensions.keyline_16),
  ) {
    if (reviews.items.isEmpty()) {
      item {
        BlankSlate(
          modifier = Modifier
            .padding(horizontal = MaterialTheme.dimensions.keyline_16)
            .testTag(TestTags.Details.Seasons.EMPTY),
          uiState = BlankSlateState.Custom(
            title = UIText.ResourceText(Res.string.feature_details_no_seasons_available),
            description = UIText.ResourceText(
              Res.string.feature_details_no_seasons_available_desc,
              title,
            ),
          ),
        )
      }
    } else {
      items(items = reviews.items) { item ->
        SeasonItem(
          season = item,
          onClick = onClick,
        )
      }

      item {
        Spacer(modifier = Modifier.height(LocalBottomNavigationPadding.current))
      }
    }
  }
}

@Composable
fun SeasonItem(
  modifier: Modifier = Modifier,
  season: Season,
  onClick: (seasonNumber: Int) -> Unit,
) {
  Row(
    modifier = modifier
      .clip(MaterialTheme.shapes.medium)
      .clickable { onClick(season.seasonNumber) }
      .padding(
        horizontal = MaterialTheme.dimensions.keyline_16,
        vertical = MaterialTheme.dimensions.keyline_8,
      ),
    horizontalArrangement = Arrangement.spacedBy(MaterialTheme.dimensions.keyline_16),
  ) {
    MovieImage(
      modifier = Modifier.weight(1.35f),
      path = season.posterPath,
    )

    Column(
      modifier = Modifier.weight(5f),
      verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimensions.keyline_8),
    ) {
      Row(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.Top,
      ) {
        Text(
          text = season.name,
          style = MaterialTheme.typography.titleMedium,
          modifier = Modifier.weight(1f),
        )
        AnimatedVisibility(season.status != null) {
          season.status?.let { JellyseerrStatusPill(status = it) }
        }
      }

      Text(
        text = pluralStringResource(
          UiPlurals.core_ui_episode_count,
          season.episodeCount,
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

// TODO Add screenshots
@Previews
@Composable
fun SeasonItemPreview() {
  AppTheme {
    Surface {
      Column(
        verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimensions.keyline_16),
      ) {
        SeasonItem(
          season = SeasonFactory.season1(),
          onClick = {},
        )

        SeasonItem(
          season = SeasonFactory.season2().copy(status = JellyseerrStatus.Media.AVAILABLE),
          onClick = {},
        )

        SeasonItem(
          season = SeasonFactory.season3().copy(status = JellyseerrStatus.Request.PENDING),
          onClick = {},
        )
      }
    }
  }
}
