package com.divinelink.feature.episode.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.divinelink.core.designsystem.theme.dimensions
import com.divinelink.core.model.details.Episode
import com.divinelink.core.model.details.rating.RatingDetails
import com.divinelink.core.model.details.rating.RatingSource
import com.divinelink.core.navigation.route.Navigation
import com.divinelink.core.ui.button.RatingButton
import com.divinelink.core.ui.components.details.AirDateWithRuntime
import com.divinelink.core.ui.rating.MediaRatingItem
import com.divinelink.feature.add.to.account.rate.RateModalBottomSheet
import com.divinelink.feature.episode.EpisodeAction
import com.divinelink.feature.episode.EpisodeUiState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EpisodeTitleDetails(
  modifier: Modifier = Modifier,
  uiState: EpisodeUiState,
  episode: Episode,
  onNavigate: (Navigation) -> Unit,
  action: (EpisodeAction) -> Unit,
) {
  var showRateModal by rememberSaveable { mutableStateOf(false) }

  if (showRateModal) {
    RateModalBottomSheet(
      modifier = modifier,
      value = episode.accountRating,
      mediaTitle = episode.name,
      onSubmitRate = { action(EpisodeAction.OnSubmitRate(it)) },
      onClearRate = { action(EpisodeAction.OnClearRate) },
      onDismissRequest = { showRateModal = false },
    )
  }

  Column(
    modifier = modifier,
    verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimensions.keyline_8),
  ) {
    Row {
      Text(
        modifier = Modifier.clickable { onNavigate(Navigation.TwiceBack) },
        color = MaterialTheme.colorScheme.primary,
        style = MaterialTheme.typography.titleSmall,
        text = uiState.showTitle,
      )

      Text(
        style = MaterialTheme.typography.titleSmall,
        color = MaterialTheme.colorScheme.primary,
        text = " / ",
      )

      Text(
        modifier = Modifier.clickable { onNavigate(Navigation.Back) },
        color = MaterialTheme.colorScheme.primary,
        style = MaterialTheme.typography.titleSmall,
        text = uiState.seasonTitle,
      )
    }

    Text(
      style = MaterialTheme.typography.titleMedium,
      text = episode.name,
    )

    AirDateWithRuntime(
      airDate = episode.airDate,
      runtime = episode.runtime,
      style = MaterialTheme.typography.titleSmall,
    )

    if (uiState.seasonNumber != 0) {
      Row(
        verticalAlignment = Alignment.CenterVertically,
      ) {
        MediaRatingItem(
          ratingDetails = RatingDetails.Score(
            voteAverage = episode.voteAverage?.toDouble() ?: 0.0,
            voteCount = episode.voteCount ?: 0,
          ),
          source = RatingSource.TMDB,
        )

        Spacer(
          modifier = Modifier.weight(1f),
        )

        RatingButton(
          accountRating = episode.accountRating,
          onClick = { showRateModal = true },
          isLoading = uiState.submitLoading,
        )
      }
    }
  }
}
