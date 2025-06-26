package com.divinelink.feature.settings.app.account

import androidx.compose.animation.AnimatedVisibilityScope

import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewParameter
import com.divinelink.core.designsystem.component.ScenePeekLazyColumn
import com.divinelink.core.designsystem.theme.dimensions
import com.divinelink.core.ui.AnimatedVisibilityScopeProvider
import com.divinelink.core.ui.Previews
import com.divinelink.feature.settings.R
import com.divinelink.feature.settings.components.SettingsDivider
import com.divinelink.feature.settings.components.SettingsTextItem
import com.divinelink.feature.settings.provider.AccountDetailsParameterProvider

@Composable
fun AccountSettingsContent(
  transitionScope: SharedTransitionScope,
  animatedVisibilityScope: AnimatedVisibilityScope,
  uiState: AccountSettingsViewState,
  onLogoutClick: () -> Unit,
  onLoginClick: () -> Unit,
  onNavigateToJellyseerrLogin: () -> Unit,
) {
  ScenePeekLazyColumn(
    modifier = Modifier.fillMaxSize(),
  ) {
    item {
      AccountItem(
        accountDetails = uiState.tmdbAccount,
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
        transitionScope = transitionScope,
        animatedVisibilityScope = animatedVisibilityScope,
        accountDetails = uiState.jellyseerrAccountDetails,
        onNavigateToJellyseerrLogin = onNavigateToJellyseerrLogin,
      )
    }
  }
}

@Previews
@Composable
fun AccountSettingsContentPreview(
  @PreviewParameter(AccountDetailsParameterProvider::class) uiState: AccountSettingsViewState,
) {
  AnimatedVisibilityScopeProvider { transitionScope, visibilityScope ->
    AccountSettingsContent(
      uiState = uiState,
      transitionScope = transitionScope,
      animatedVisibilityScope = visibilityScope,
      onLogoutClick = {},
      onLoginClick = {},
      onNavigateToJellyseerrLogin = {},
    )
  }
}
