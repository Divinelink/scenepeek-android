package com.divinelink.feature.details.media.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.divinelink.core.designsystem.theme.dimensions
import com.divinelink.core.model.details.AccountDataSection
import com.divinelink.core.model.details.toMediaItem
import com.divinelink.core.navigation.route.Navigation
import com.divinelink.core.ui.button.RatingButton
import com.divinelink.core.ui.components.AddToListButton
import com.divinelink.core.ui.components.WatchlistButton
import com.divinelink.feature.details.media.ui.DetailsViewState

@Composable
fun DetailActions(
  onAddRateClick: () -> Unit,
  uiState: DetailsViewState,
  onAddToWatchlistClick: () -> Unit,
  onNavigate: (Navigation) -> Unit,
) {
  Row(
    modifier = Modifier.padding(MaterialTheme.dimensions.keyline_16),
    horizontalArrangement = Arrangement.spacedBy(MaterialTheme.dimensions.keyline_8),
    verticalAlignment = Alignment.CenterVertically,
  ) {
    RatingButton(
      modifier = Modifier.weight(3f),
      onClick = onAddRateClick,
      accountRating = uiState.userDetails.beautifiedRating,
      isLoading = uiState.accountDataState[AccountDataSection.Rating] == true,
    )

    WatchlistButton(
      modifier = Modifier.weight(1f),
      onWatchlist = uiState.userDetails.watchlist,
      onClick = onAddToWatchlistClick,
      isLoading = uiState.accountDataState[AccountDataSection.Watchlist] == true,
    )

    AddToListButton(
      modifier = Modifier.weight(1f),
      onClick = {
        uiState.mediaDetails?.toMediaItem()?.let { mediaItem ->
          with(mediaItem) {
            onNavigate(
              Navigation.AddToListRoute(
                id = id,
                mediaType = mediaType.value,
              ),
            )
          }
        }
      },
    )
  }
}
