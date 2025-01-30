package com.divinelink.feature.watchlist

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import com.divinelink.core.designsystem.theme.AppTheme
import com.divinelink.core.designsystem.theme.LocalBottomNavigationPadding
import com.divinelink.core.designsystem.theme.dimensions
import com.divinelink.core.ui.Previews
import com.divinelink.core.ui.TestTags
import com.divinelink.core.ui.R as uiR

@Composable
fun WatchlistErrorContent(
  error: WatchlistForm.Error,
  onLogin: () -> Unit,
  onRetry: () -> Unit,
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
      WatchlistForm.Error.Unauthenticated -> {
        Text(
          textAlign = TextAlign.Center,
          style = MaterialTheme.typography.titleMedium,
          text = stringResource(id = R.string.feature_watchlist_login_to_see_watchlist),
        )
        Spacer(modifier = Modifier.height(MaterialTheme.dimensions.keyline_16))
        Button(onClick = onLogin) {
          Text(text = stringResource(id = uiR.string.core_ui_login))
        }
      }
      WatchlistForm.Error.Unknown -> {
        Text(
          textAlign = TextAlign.Center,
          style = MaterialTheme.typography.titleLarge,
          text = stringResource(id = uiR.string.core_ui_error_retry),
        )
        Spacer(modifier = Modifier.height(MaterialTheme.dimensions.keyline_16))
        Button(onClick = onRetry) {
          Text(text = stringResource(id = uiR.string.core_ui_retry))
        }
      }
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
          onLogin = {},
          onRetry = {},
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
          onLogin = {},
          onRetry = {},
        )
      }
    }
  }
}
