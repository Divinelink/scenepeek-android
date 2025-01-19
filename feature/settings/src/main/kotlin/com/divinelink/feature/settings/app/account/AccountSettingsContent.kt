package com.divinelink.feature.settings.app.account

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.layout.PaddingValues
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

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun SharedTransitionScope.AccountSettingsContent(
  paddingValues: PaddingValues = PaddingValues(),
  animatedVisibilityScope: AnimatedVisibilityScope,
  uiState: AccountSettingsViewState,
  onLogoutClick: () -> Unit,
  onLoginClick: () -> Unit,
  onNavigateToJellyseerrLogin: () -> Unit,
) {
  ScenePeekLazyColumn(
    modifier = Modifier.fillMaxSize(),
    contentPadding = paddingValues,
  ) {
    item {
      AccountItem(
        accountDetails = uiState.accountDetails,
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
        accountDetails = uiState.jellyseerrAccountDetails,
        onNavigateToJellyseerrLogin = onNavigateToJellyseerrLogin,
      )
    }
  }
}

@OptIn(ExperimentalSharedTransitionApi::class)
@Previews
@Composable
fun AccountSettingsContentPreview(
  @PreviewParameter(AccountDetailsParameterProvider::class) uiState: AccountSettingsViewState,
) {
  AnimatedVisibilityScopeProvider {
    AccountSettingsContent(
      uiState = uiState,
      animatedVisibilityScope = it,
      onLogoutClick = {},
      onLoginClick = {},
      onNavigateToJellyseerrLogin = {},
    )
  }
}
