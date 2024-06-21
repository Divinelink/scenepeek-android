package com.divinelink.feature.watchlist

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import com.divinelink.core.designsystem.theme.AppTheme
import com.divinelink.core.designsystem.theme.dimensions
import com.divinelink.core.ui.Previews
import com.divinelink.core.ui.UIText
import com.divinelink.core.ui.getString

@Composable
fun WatchlistEmptyContent(text: UIText) {
  Column(
    modifier = Modifier
      .fillMaxSize()
      .padding(horizontal = MaterialTheme.dimensions.keyline_16),
    verticalArrangement = Arrangement.Center,
    horizontalAlignment = Alignment.CenterHorizontally,
  ) {
    Text(
      textAlign = TextAlign.Center,
      style = MaterialTheme.typography.titleMedium,
      text = text.getString(),
    )
  }
}

@Previews
@Composable
private fun WatchlistContentPreview() {
  AppTheme {
    Surface {
      WatchlistEmptyContent(
        UIText.ResourceText(R.string.feature_watchlist_empty_movies_watchlist),
      )
    }
  }
}
