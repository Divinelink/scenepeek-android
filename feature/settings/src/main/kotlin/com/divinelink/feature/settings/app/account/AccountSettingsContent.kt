package com.divinelink.feature.settings.app.account

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AmpStories
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.divinelink.core.designsystem.theme.AppTheme
import com.divinelink.core.designsystem.theme.dimensions
import com.divinelink.core.model.account.AccountDetails
import com.divinelink.core.model.jellyseerr.JellyseerrIntegration
import com.divinelink.core.ui.Previews
import com.divinelink.feature.settings.app.account.jellyseerr.JellyseerrModalBottomSheet
import com.divinelink.feature.settings.components.SettingsClickItem
import com.divinelink.feature.settings.components.SettingsDivider
import com.divinelink.feature.settings.components.SettingsTextItem

@Composable
fun AccountSettingsContent(
  paddingValues: PaddingValues = PaddingValues(),
  accountDetails: AccountDetails?,
  onLogoutClick: () -> Unit,
  onLoginClick: () -> Unit,
) {
  var openJellyseerrBottomSheet by rememberSaveable { mutableStateOf(false) }

  if (openJellyseerrBottomSheet) {
    JellyseerrModalBottomSheet(
      jellyseerrIntegration = JellyseerrIntegration(
        address = "",
        apiKey = "",
      ),
      onApiKeyChange = { },
      onAddressChange = { },
      onTestClick = { },
      onSaveClick = { },
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
        title = "Additional features",
        summary = "Integrate your self-hosted apps to enable additional features like movie" +
          " and TV show requests.",
      )

      Spacer(
        modifier = Modifier.padding(top = MaterialTheme.dimensions.keyline_4),
      )

      SettingsClickItem(
        iconVector = Icons.Default.AmpStories,
        text = "Jellyseerr Integration (Beta)",
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
        onLogoutClick = {},
        onLoginClick = {},
      )
    }
  }
}
