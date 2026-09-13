package com.divinelink.feature.details.media.ui.actions

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.PlaylistAdd
import androidx.compose.material.icons.filled.BookmarkAdded
import androidx.compose.material.icons.outlined.BookmarkAdd
import androidx.compose.material.icons.rounded.Download
import androidx.compose.material.icons.rounded.Favorite
import androidx.compose.material.icons.rounded.FavoriteBorder
import androidx.compose.material.icons.rounded.LibraryAdd
import androidx.compose.material.icons.rounded.LibraryAddCheck
import androidx.compose.material.icons.rounded.SettingsSuggest
import androidx.compose.material.icons.rounded.StarRate
import com.divinelink.core.model.UIText
import com.divinelink.core.model.details.AccountDataSection
import com.divinelink.core.ui.IconWrapper
import com.divinelink.core.ui.UiString
import com.divinelink.core.ui.components.action.MediaActionButtonItem
import com.divinelink.core.ui.resources.core_ui_add_rating
import com.divinelink.core.ui.resources.track_show
import com.divinelink.core.ui.resources.tracking_show
import com.divinelink.feature.details.resources.Res
import com.divinelink.feature.details.resources.feature_details__add_to_list
import com.divinelink.feature.details.resources.feature_details__favorite
import com.divinelink.feature.details.resources.feature_details__watchlist
import com.divinelink.feature.details.resources.feature_details_manage_movie
import com.divinelink.feature.details.resources.feature_details_manage_tv
import com.divinelink.feature.details.resources.feature_details_request

object MediaActionDefaults {

  fun rate(
    rating: Float?,
    action: (MediaAction) -> Unit,
  ) = MediaActionButtonItem(
    icon = IconWrapper.Vector(Icons.Rounded.StarRate),
    label = if (rating == null) {
      UIText.ResourceText(UiString.core_ui_add_rating)
    } else {
      UIText.StringText(rating.toInt().toString())
    },
    onClick = { action(MediaAction.AddRating) },
  )

  fun watchlist(
    onWatchlist: Boolean,
    action: (MediaAction) -> Unit,
  ) = MediaActionButtonItem(
    icon = if (onWatchlist) {
      IconWrapper.Vector(Icons.Filled.BookmarkAdded)
    } else {
      IconWrapper.Vector(Icons.Outlined.BookmarkAdd)
    },
    label = UIText.ResourceText(Res.string.feature_details__watchlist),
    onClick = { action(MediaAction.AddToAccount(AccountDataSection.Watchlist)) },
  )

  fun addToList(action: (MediaAction) -> Unit) = MediaActionButtonItem(
    icon = IconWrapper.Vector(Icons.AutoMirrored.Rounded.PlaylistAdd),
    label = UIText.ResourceText(Res.string.feature_details__add_to_list),
    onClick = { action(MediaAction.AddToList) },
  )


  fun favorite(
    favorite: Boolean,
    action: (MediaAction) -> Unit,
  ): MediaActionButtonItem {
    return MediaActionButtonItem(
      icon = if (favorite) {
        IconWrapper.Vector(Icons.Rounded.Favorite)
      } else {
        IconWrapper.Vector(Icons.Rounded.FavoriteBorder)
      },
      label = UIText.ResourceText(Res.string.feature_details__favorite),
      onClick = { action(MediaAction.AddToAccount(AccountDataSection.Favorite)) },
    )
  }

  fun addShow(
    added: Boolean,
    action: (MediaAction) -> Unit,
  ): MediaActionButtonItem {
    val text = if (added) {
      UiString.tracking_show
    } else {
      UiString.track_show
    }
    val icon = if (added) {
      Icons.Rounded.LibraryAddCheck
    } else {
      Icons.Rounded.LibraryAdd
    }
    return MediaActionButtonItem(
      icon = IconWrapper.Vector(icon),
      label = UIText.ResourceText(text),
      onClick = { action(MediaAction.AddShow) },
    )
  }

  fun request(
    action: (MediaAction) -> Unit,
  ) = MediaActionButtonItem(
    icon = IconWrapper.Vector(Icons.Rounded.Download),
    label = UIText.ResourceText(Res.string.feature_details_request),
    seerrMode = true,
    onClick = { action(MediaAction.Request) },
  )

  fun manageMovie(
    action: (MediaAction) -> Unit,
  ) = MediaActionButtonItem(
    icon = IconWrapper.Vector(Icons.Rounded.SettingsSuggest),
    label = UIText.ResourceText(Res.string.feature_details_manage_movie),
    seerrMode = true,
    onClick = { action(MediaAction.ManageMovie) },
  )

  fun manageTv(
    action: (MediaAction) -> Unit,
  ) = MediaActionButtonItem(
    icon = IconWrapper.Vector(Icons.Rounded.SettingsSuggest),
    label = UIText.ResourceText(Res.string.feature_details_manage_tv),
    seerrMode = true,
    onClick = { action(MediaAction.ManageTv) },
  )
}
