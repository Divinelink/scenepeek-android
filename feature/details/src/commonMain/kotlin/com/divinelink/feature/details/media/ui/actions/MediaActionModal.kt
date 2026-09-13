package com.divinelink.feature.details.media.ui.actions

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.divinelink.core.designsystem.theme.colors
import com.divinelink.core.designsystem.theme.dimensions
import com.divinelink.core.model.account.AccountMediaDetails
import com.divinelink.core.model.details.DetailActionItem
import com.divinelink.core.ui.extension.getColorRating
import com.divinelink.feature.details.media.ui.actions.button.MediaMainAction
import com.divinelink.feature.details.media.ui.actions.button.MediaSecondaryAction

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MediaActionModal(
  sheetState: SheetState,
  actionButtons: List<DetailActionItem>,
  accountState: AccountMediaDetails,
  addedShow: Boolean = true,
  onDismissRequest: () -> Unit,
  action: (MediaAction) -> Unit,
) {
  ModalBottomSheet(
    sheetState = sheetState,
    onDismissRequest = onDismissRequest,
  ) {
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = MaterialTheme.dimensions.keyline_17),
    ) {
      MediaMainAction(
        modifier = Modifier.weight(1f),
        action = MediaActionDefaults.rate(
          rating = accountState.rating,
          action = action,
        ),
        containerColor = accountState.rating?.toDouble().getColorRating().copy(
          0.2f,
        ),
      )

      MediaMainAction(
        modifier = Modifier.weight(1f),
        action = MediaActionDefaults.watchlist(
          onWatchlist = accountState.watchlist,
          action = action,
        ),
        containerColor = if (accountState.watchlist) {
          MaterialTheme.colors.vibrantPurple
        } else {
          Color.Unspecified
        },
      )
      MediaMainAction(
        modifier = Modifier.weight(1f),
        action = MediaActionDefaults.addToList(action),
      )
    }

    Spacer(modifier = Modifier.height(MaterialTheme.dimensions.keyline_16))

    if (actionButtons.contains(DetailActionItem.AddShow)) {
      MediaSecondaryAction(
        action = MediaActionDefaults.addShow(
          added = addedShow,
          action = action,
        ),
        containerColor = if (addedShow) {
          MaterialTheme.colors.mediumSeaGreen
        } else {
          Color.Unspecified
        },
      )
    }

    if (actionButtons.contains(DetailActionItem.Favorite)) {
      MediaSecondaryAction(
        action = MediaActionDefaults.favorite(
          favorite = accountState.favorite,
          action = action,
        ),
        containerColor = if (accountState.favorite) {
          MaterialTheme.colors.crimsonRed
        } else {
          Color.Unspecified
        },
      )
    }

    Spacer(modifier = Modifier.height(MaterialTheme.dimensions.keyline_16))

    if (actionButtons.contains(DetailActionItem.Request)) {
      MediaSecondaryAction(
        action = MediaActionDefaults.request(action),
      )
    }

    if (actionButtons.contains(DetailActionItem.ManageMovie)) {
      MediaSecondaryAction(
        action = MediaActionDefaults.manageMovie(action),
      )
    }

    if (actionButtons.contains(DetailActionItem.ManageTvShow)) {
      MediaSecondaryAction(
        action = MediaActionDefaults.manageTv(action),
      )
    }
  }
}


