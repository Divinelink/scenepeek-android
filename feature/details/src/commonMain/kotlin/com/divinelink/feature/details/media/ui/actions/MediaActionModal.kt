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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.divinelink.core.designsystem.theme.colors
import com.divinelink.core.designsystem.theme.dimensions
import com.divinelink.core.model.UIText
import com.divinelink.core.model.account.AccountMediaDetails
import com.divinelink.core.model.details.DetailActionItem
import com.divinelink.core.ui.UiString
import com.divinelink.core.ui.extension.getColorRating
import com.divinelink.core.ui.resources.added_to_your_shows
import com.divinelink.core.ui.resources.removed_from_your_shows
import com.divinelink.core.ui.snackbar.SnackbarMessage
import com.divinelink.core.ui.snackbar.SnackbarMessageHandler
import com.divinelink.feature.details.media.ui.actions.button.MediaMainAction
import com.divinelink.feature.details.media.ui.actions.button.MediaSecondaryAction
import kotlinx.coroutines.launch

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
  var snackbarMessage by remember { mutableStateOf<SnackbarMessage?>(null) }
  val scope = rememberCoroutineScope()

  fun onAddShow(action: MediaAction) {
    scope.launch { action(action) }

    val text = if (addedShow) {
      UiString.removed_from_your_shows
    } else {
      UiString.added_to_your_shows
    }
    snackbarMessage = SnackbarMessage.from(text = UIText.ResourceText(text))
  }

  SnackbarMessageHandler(
    snackbarMessage = snackbarMessage,
    onDismissSnackbar = { snackbarMessage = null },
    onShowMessage = {},
  )

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
          action = { onAddShow(it) },
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
