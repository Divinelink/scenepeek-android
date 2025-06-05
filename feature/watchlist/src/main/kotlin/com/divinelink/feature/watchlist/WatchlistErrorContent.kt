package com.divinelink.feature.watchlist

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import com.divinelink.core.designsystem.theme.AppTheme
import com.divinelink.core.designsystem.theme.LocalBottomNavigationPadding
import com.divinelink.core.designsystem.theme.dimensions
import com.divinelink.core.model.UIText
import com.divinelink.core.ui.Previews
import com.divinelink.core.ui.TestTags
import com.divinelink.core.ui.blankslate.BlankSlate
import com.divinelink.core.ui.blankslate.BlankSlateState

@Composable
fun WatchlistErrorContent(
  error: WatchlistForm.Error,
  onRetry: (() -> Unit)? = null,
) {
  Column(
    modifier = Modifier
      .testTag(TestTags.Watchlist.WATCHLIST_ERROR_CONTENT)
      .fillMaxSize()
      .padding(horizontal = MaterialTheme.dimensions.keyline_16)
      .padding(bottom = LocalBottomNavigationPadding.current),
    verticalArrangement = Arrangement.Center,
    horizontalAlignment = Alignment.CenterHorizontally,
  ) {
    when (error) {
      WatchlistForm.Error.Unauthenticated -> BlankSlate(
        uiState = BlankSlateState.Custom(
          icon = com.divinelink.core.model.R.drawable.core_model_ic_tmdb,
          title = UIText.ResourceText(R.string.feature_watchlist_login_title),
          description = UIText.ResourceText(R.string.feature_watchlist_login_description),
        ),
      )
      WatchlistForm.Error.Network -> BlankSlate(
        uiState = BlankSlateState.Offline,
        onRetry = onRetry,
      )

      WatchlistForm.Error.Unknown -> BlankSlate(
        uiState = BlankSlateState.Generic,
        onRetry = onRetry,
      )
    }
  }
}

@Previews
@Composable
private fun WatchlistInvalidSessionErrorContentPreview() {
  AppTheme {
    Surface {
      Column {
        WatchlistErrorContent(
          error = WatchlistForm.Error.Unauthenticated,
        )
      }
    }
  }
}

@Previews
@Composable
private fun WatchlistUnknownErrorContentPreview() {
  AppTheme {
    Surface {
      Column {
        WatchlistErrorContent(
          error = WatchlistForm.Error.Unknown,
        )
      }
    }
  }
}
