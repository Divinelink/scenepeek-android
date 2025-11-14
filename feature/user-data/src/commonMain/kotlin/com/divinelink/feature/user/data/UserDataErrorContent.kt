package com.divinelink.feature.user.data

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
import com.divinelink.core.model.user.data.UserDataSection
import com.divinelink.core.ui.Previews
import com.divinelink.core.ui.TestTags
import com.divinelink.core.ui.blankslate.BlankSlate
import com.divinelink.core.ui.blankslate.BlankSlateState

@Composable
fun UserDataErrorContent(
  error: UserDataForm.Error,
  section: UserDataSection,
  onRetry: (() -> Unit),
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
      is UserDataForm.Error.Unauthenticated -> BlankSlate(
        uiState = BlankSlateState.Unauthenticated(
          description = when (section) {
            UserDataSection.Watchlist -> UIText.ResourceText(
              Res.string.feature_user_data_login_watchlist_description,
            )
            UserDataSection.Ratings -> UIText.ResourceText(
              Res.string.feature_user_data_login_rated_description,
            )
          },
        ),
      )
      UserDataForm.Error.Network -> BlankSlate(
        uiState = BlankSlateState.Offline,
        onRetry = onRetry,
      )

      UserDataForm.Error.Unknown -> BlankSlate(
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
        UserDataErrorContent(
          error = UserDataForm.Error.Unauthenticated,
          section = UserDataSection.Watchlist,
          onRetry = { /* No-op */ },
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
        UserDataErrorContent(
          error = UserDataForm.Error.Unknown,
          section = UserDataSection.Watchlist,
          onRetry = { /* No-op */ },
        )
      }
    }
  }
}
