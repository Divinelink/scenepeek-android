package com.divinelink.feature.settings.app.account.jellyseerr

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.divinelink.core.designsystem.theme.AppTheme
import com.divinelink.core.designsystem.theme.dimensions
import com.divinelink.core.model.jellyseerr.JellyseerrAccountStatus
import com.divinelink.core.model.jellyseerr.JellyseerrLoginMethod
import com.divinelink.core.model.jellyseerr.JellyseerrState
import com.divinelink.core.ui.Previews
import com.divinelink.feature.settings.R
import com.divinelink.core.ui.R as uiR

@Composable
fun JellyseerrLoggedInBottomSheetContent(
  jellyseerrState: JellyseerrState.LoggedIn,
  onLogoutClock: (JellyseerrInteraction.OnLogoutClick) -> Unit,
) {
  val loggedInResource =
    if (jellyseerrState.loginData.signInMethod == JellyseerrLoginMethod.JELLYFIN) {
      R.string.feature_settings_jellyfin_already_logged_in
    } else {
      R.string.feature_settings_jellyseerr_already_logged_in
    }

  Column(
    modifier = Modifier
      .wrapContentSize()
      .padding(MaterialTheme.dimensions.keyline_16),
    verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimensions.keyline_16),
  ) {
    Text(
      text = stringResource(
        loggedInResource,
        jellyseerrState.loginData.username,
      ),
    )

    Spacer(Modifier.height(MaterialTheme.dimensions.keyline_32))

    Button(
      modifier = Modifier.fillMaxWidth(),
      onClick = { onLogoutClock(JellyseerrInteraction.OnLogoutClick) },
    ) {
      Text(stringResource(id = uiR.string.core_ui_logout))
    }
  }
}

@Previews
@Composable
private fun JellyseerrBottomSheetContentPreview() {
  AppTheme {
    Surface {
      JellyseerrLoggedInBottomSheetContent(
        jellyseerrState = JellyseerrState.LoggedIn(
          loginData = JellyseerrAccountStatus(
            address = "http://localhost:8080",
            username = "Username",
            signInMethod = JellyseerrLoginMethod.JELLYFIN,
          ),
        ),
        onLogoutClock = {},
      )
    }
  }
}
