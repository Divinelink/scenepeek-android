package com.divinelink.core.ui.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BookmarkAdded
import androidx.compose.material.icons.outlined.BookmarkAdd
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import org.jetbrains.compose.resources.stringResource
import com.divinelink.core.designsystem.theme.AppTheme
import com.divinelink.core.designsystem.theme.dimensions
import com.divinelink.core.designsystem.theme.shape
import com.divinelink.core.ui.Previews
import com.divinelink.core.ui.UiString
import com.divinelink.core.ui.core_ui_add_to_watchlist_content_desc
import com.divinelink.core.ui.core_ui_remove_from_watchlist_content_desc

@Composable
fun WatchlistButton(
  modifier: Modifier = Modifier,
  onWatchlist: Boolean,
  onClick: () -> Unit,
  isLoading: Boolean,
) {
  ElevatedButton(
    modifier = modifier,
    onClick = onClick,
    elevation = ButtonDefaults.buttonElevation(
      defaultElevation = MaterialTheme.dimensions.keyline_2,
    ),
    shape = MaterialTheme.shape.medium,
  ) {
    AnimatedContent(isLoading) { loading ->
      if (loading) {
        CircularProgressIndicator(modifier = Modifier.size(MaterialTheme.dimensions.keyline_24))
      } else {
        Icon(
          imageVector = if (onWatchlist) {
            Icons.Filled.BookmarkAdded
          } else {
            Icons.Outlined.BookmarkAdd
          },
          tint = MaterialTheme.colorScheme.primary,
          contentDescription = if (onWatchlist) {
            stringResource(UiString.core_ui_remove_from_watchlist_content_desc)
          } else {
            stringResource(UiString.core_ui_add_to_watchlist_content_desc)
          },
        )
      }
    }
  }
}

@Previews
@Composable
private fun WatchlistButtonPreview() {
  AppTheme {
    Surface {
      Column(
        verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimensions.keyline_8),
      ) {
        WatchlistButton(onWatchlist = false, onClick = {}, isLoading = false)
        WatchlistButton(onWatchlist = true, onClick = {}, isLoading = false)
        WatchlistButton(onWatchlist = true, onClick = {}, isLoading = true)
      }
    }
  }
}
