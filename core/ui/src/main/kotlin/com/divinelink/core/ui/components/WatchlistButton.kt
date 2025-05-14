package com.divinelink.core.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BookmarkAdded
import androidx.compose.material.icons.outlined.BookmarkAdd
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.divinelink.core.designsystem.theme.AppTheme
import com.divinelink.core.designsystem.theme.dimensions
import com.divinelink.core.designsystem.theme.shape
import com.divinelink.core.ui.Previews

@Composable
fun WatchlistButton(
  modifier: Modifier = Modifier,
  onWatchlist: Boolean,
  onClick: () -> Unit,
) {
  ElevatedButton(
    modifier = modifier,
    onClick = onClick,
    elevation = ButtonDefaults.buttonElevation(
      defaultElevation = MaterialTheme.dimensions.keyline_2,
    ),
    shape = MaterialTheme.shape.medium,
  ) {
    Icon(
      modifier = Modifier.size(MaterialTheme.dimensions.keyline_24),
      imageVector = if (onWatchlist) {
        Icons.Filled.BookmarkAdded
      } else {
        Icons.Outlined.BookmarkAdd
      },
      tint = MaterialTheme.colorScheme.primary,
      contentDescription = null,
    )
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
        WatchlistButton(onWatchlist = false, onClick = {})
        WatchlistButton(onWatchlist = true, onClick = {})
      }
    }
  }
}
