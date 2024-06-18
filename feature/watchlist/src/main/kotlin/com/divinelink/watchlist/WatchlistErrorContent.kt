package com.divinelink.watchlist

import android.content.res.Configuration
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import com.divinelink.core.designsystem.theme.AppTheme
import com.divinelink.core.designsystem.theme.dimensions
import com.divinelink.feature.watchlist.R
import com.divinelink.core.ui.R as uiR

@Composable
fun WatchlistErrorContent(
  error: WatchlistForm.Error,
  onLogin: () -> Unit,
  onRetry: () -> Unit,
) {

  Column(
    modifier = Modifier
      .fillMaxSize()
      .padding(horizontal = MaterialTheme.dimensions.keyline_16),
    verticalArrangement = Arrangement.Center,
    horizontalAlignment = Alignment.CenterHorizontally,
  ) {
    when (error) {
      WatchlistForm.Error.InvalidSession -> {
        Text(
          textAlign = TextAlign.Center,
          style = MaterialTheme.typography.titleLarge,
          text = stringResource(id = R.string.feature_watchlist_login_to_see_watchlist)
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
          text = stringResource(id = uiR.string.core_ui_error_retry)
        )
        Spacer(modifier = Modifier.height(MaterialTheme.dimensions.keyline_16))
        Button(onClick = onRetry) {
          Text(text = stringResource(id = uiR.string.core_ui_retry))
        }
      }
    }
  }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_NO)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun WatchlistInvalidSessionErrorContentPreview() {
  AppTheme {
    Surface {
      Column {
        WatchlistErrorContent(
          error = WatchlistForm.Error.InvalidSession,
          onLogin = {},
          onRetry = {},
        )
      }
    }
  }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_NO)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
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
