package com.andreolas.movierama.ui.components

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.divinelink.core.designsystem.theme.dimensions

@Composable
fun WatchlistButton(
  modifier: Modifier = Modifier,
  onWatchlist: Boolean,
  onClick: () -> Unit,
) {
  Crossfade(
    targetState = onWatchlist,
    label = "Watchlist button",
  ) { isOnWatchlist ->
    when (isOnWatchlist) {
      true -> {
        OutlinedButton(
          modifier = modifier.fillMaxWidth(),
          onClick = onClick
        ) {
          Icon(
            imageVector = Icons.Filled.Done,
            contentDescription = null
          )

          Spacer(modifier = Modifier.width(4.dp))

          Text(text = "Added to watchlist")
        }
      }
      false -> {
        Button(
          modifier = modifier.fillMaxWidth(),
          onClick = onClick
        ) {
          Icon(
            imageVector = Icons.Filled.Add,
            contentDescription = null
          )

          Spacer(modifier = Modifier.width(4.dp))

          Text(text = "Add to Watchlist")
        }
      }
    }
  }
}

@Preview
@Composable
private fun WatchlistButtonPreview() {
  MaterialTheme {
    Column(
      verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimensions.keyline_8),
    ) {
      WatchlistButton(onWatchlist = false, onClick = {})
      WatchlistButton(onWatchlist = true, onClick = {})
    }
  }
}
