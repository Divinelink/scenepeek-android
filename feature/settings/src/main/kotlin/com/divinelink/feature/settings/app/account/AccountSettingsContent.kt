package com.divinelink.feature.settings.app.account

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.divinelink.core.designsystem.theme.AppTheme
import com.divinelink.core.designsystem.theme.dimensions
import com.divinelink.core.model.account.AccountDetails
import com.divinelink.core.model.jellyseerr.JellyseerrLoginMethod
import com.divinelink.core.model.jellyseerr.JellyseerrState
import com.divinelink.core.ui.IconWrapper
import com.divinelink.core.ui.Previews
import com.divinelink.feature.settings.R
import com.divinelink.feature.settings.app.account.jellyseerr.JellyseerrInteraction
import com.divinelink.feature.settings.app.account.jellyseerr.JellyseerrModalBottomSheet
import com.divinelink.feature.settings.components.SettingsClickItem
import com.divinelink.feature.settings.components.SettingsDivider
import com.divinelink.feature.settings.components.SettingsTextItem
import com.divinelink.core.ui.R as uiR

@Composable
fun AccountSettingsContent(
  paddingValues: PaddingValues = PaddingValues(),
  accountDetails: AccountDetails?,
  jellyseerrState: JellyseerrState,
  jellyseerrInteraction: (JellyseerrInteraction) -> Unit,
  onLogoutClick: () -> Unit,
  onLoginClick: () -> Unit,
) {
  var openJellyseerrBottomSheet by rememberSaveable { mutableStateOf(false) }

  if (openJellyseerrBottomSheet) {
    JellyseerrModalBottomSheet(
      jellyseerrState = jellyseerrState,
      interaction = jellyseerrInteraction,
      onDismissRequest = { openJellyseerrBottomSheet = false },
    )
  }

  LazyColumn(
    modifier = Modifier.fillMaxSize(),
    contentPadding = paddingValues,
  ) {
    item {
      AccountItem(
        accountDetails = accountDetails,
        onLoginClick = onLoginClick,
        onLogoutClick = onLogoutClick,
      )
    }
    item {
      SettingsDivider()
    }
    item {
      SettingsTextItem(
        title = stringResource(R.string.feature_settings_additional_features),
        summary = stringResource(R.string.feature_settings_additional_features_summary),
      )

      Spacer(
        modifier = Modifier.padding(top = MaterialTheme.dimensions.keyline_4),
      )

      SettingsClickItem(
        icon = IconWrapper.Image(uiR.drawable.core_ui_ic_jellyseerr),
        text = stringResource(R.string.feature_settings_jellyseerr_integration),
        onClick = {
          openJellyseerrBottomSheet = true
        },
      )
    }
  }
}

@Previews
@Composable
private fun AccountSettingsContentPreview() {
  AppTheme {
    Surface {
      AccountSettingsContent(
        accountDetails = AccountDetails(
          id = 123,
          username = "Jessee Pinkman",
          name = "name",
        ),
        jellyseerrState = JellyseerrState.Initial(
          isLoading = false,
          preferredOption = null,
        ),
        onLogoutClick = {},
        onLoginClick = {},
        jellyseerrInteraction = {},
      )
    }
  }
}
