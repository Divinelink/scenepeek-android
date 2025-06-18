package com.divinelink.feature.details.media.ui.fab

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.BookmarkAdd
import androidx.compose.material.icons.rounded.Download
import androidx.compose.material.icons.rounded.SettingsSuggest
import androidx.compose.material.icons.rounded.StarRate
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.divinelink.core.model.UIText
import com.divinelink.core.model.details.DetailActionItem
import com.divinelink.core.scaffold.ScaffoldState
import com.divinelink.core.ui.IconWrapper
import com.divinelink.core.ui.components.expandablefab.FloatingActionButtonItem
import com.divinelink.feature.details.R

@Composable
internal fun ScaffoldState.DetailsExpandableFloatingActionButton(
  modifier: Modifier = Modifier,
  actionButtons: List<DetailActionItem>,
  onAddRateClicked: () -> Unit,
  onAddToWatchlistClicked: () -> Unit,
  onRequestClicked: () -> Unit,
  onManageMovie: () -> Unit,
  onManageTv: () -> Unit,
) {
  ExpandableFloatActionButton(
    modifier = modifier,
    buttons = actionButtons.map { button ->
      when (button) {
        DetailActionItem.Rate -> FloatingActionButtonItem(
          icon = IconWrapper.Vector(Icons.Rounded.StarRate),
          label = UIText.ResourceText(R.string.details__add_rating),
          contentDescription = UIText.ResourceText(R.string.details__add_rating),
          onClick = onAddRateClicked,
        )
        DetailActionItem.Watchlist -> FloatingActionButtonItem(
          icon = IconWrapper.Vector(Icons.Rounded.BookmarkAdd),
          label = UIText.ResourceText(R.string.feature_details__watchlist),
          contentDescription = UIText.ResourceText(R.string.feature_details__watchlist),
          onClick = onAddToWatchlistClicked,
        )
        DetailActionItem.Request -> FloatingActionButtonItem(
          icon = IconWrapper.Vector(Icons.Rounded.Download),
          label = UIText.ResourceText(R.string.feature_details_request),
          contentDescription = UIText.ResourceText(R.string.feature_details_request),
          onClick = onRequestClicked,
        )
        DetailActionItem.ManageMovie -> FloatingActionButtonItem(
          icon = IconWrapper.Vector(Icons.Rounded.SettingsSuggest),
          label = UIText.ResourceText(R.string.feature_details_manage_movie),
          contentDescription = UIText.ResourceText(R.string.feature_details_manage_movie),
          onClick = onManageMovie,
        )
        DetailActionItem.ManageTvShow -> FloatingActionButtonItem(
          icon = IconWrapper.Vector(Icons.Rounded.SettingsSuggest),
          label = UIText.ResourceText(R.string.feature_details_manage_tv),
          contentDescription = UIText.ResourceText(R.string.feature_details_manage_tv),
          onClick = onManageTv,
        )
      }
    },
  )
}
