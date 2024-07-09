package com.divinelink.feature.settings.app.account

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.divinelink.core.designsystem.theme.dimensions
import com.divinelink.core.model.account.AccountDetails
import com.divinelink.core.model.jellyseerr.JellyseerrAccountDetails
import com.divinelink.core.ui.AnimatedVisibilityScopeProvider
import com.divinelink.core.ui.Previews
import com.divinelink.feature.settings.R
import com.divinelink.feature.settings.components.SettingsDivider
import com.divinelink.feature.settings.components.SettingsTextItem

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun SharedTransitionScope.AccountSettingsContent(
  paddingValues: PaddingValues = PaddingValues(),
  animatedVisibilityScope: AnimatedVisibilityScope,
  accountDetails: AccountDetails?,
  jellyseerrAccountDetails: JellyseerrAccountDetails?,
  onLogoutClick: () -> Unit,
  onLoginClick: () -> Unit,
  onNavigateToJellyseerrLogin: () -> Unit,
) {
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

      JellyseerrAccountItem(
        animatedVisibilityScope = animatedVisibilityScope,
        accountDetails = jellyseerrAccountDetails,
        onNavigateToJellyseerrLogin = onNavigateToJellyseerrLogin,
      )
    }
  }
}

@OptIn(ExperimentalSharedTransitionApi::class)
@Previews
@Composable
private fun AccountSettingsContentPreview() {
  AnimatedVisibilityScopeProvider {
    AccountSettingsContent(
      accountDetails = AccountDetails(
        id = 123,
        username = "Jessee Pinkman",
        name = "name",
      ),
      animatedVisibilityScope = it,
      jellyseerrAccountDetails = null,
      onLogoutClick = {},
      onLoginClick = {},
      onNavigateToJellyseerrLogin = {},
    )
  }
}
