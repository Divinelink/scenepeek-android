package com.divinelink.feature.settings.app.account

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.divinelink.core.designsystem.component.ScenePeekLazyColumn
import com.divinelink.core.designsystem.theme.dimensions
import com.divinelink.core.ui.Previews
import com.divinelink.core.ui.SharedTransitionScopeProvider
import com.divinelink.feature.settings.components.SettingsDivider
import com.divinelink.feature.settings.components.SettingsTextItem
import com.divinelink.feature.settings.provider.AccountDetailsParameterProvider
import com.divinelink.feature.settings.resources.Res
import com.divinelink.feature.settings.resources.feature_settings_additional_features
import com.divinelink.feature.settings.resources.feature_settings_additional_features_summary
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.PreviewParameter

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
        title = stringResource(Res.string.feature_settings_additional_features),
        summary = stringResource(Res.string.feature_settings_additional_features_summary),
      )

      Spacer(
        modifier = Modifier.padding(top = MaterialTheme.dimensions.keyline_4),
      )

      JellyseerrAccountItem(
        transitionScope = transitionScope,
        animatedVisibilityScope = animatedVisibilityScope,
        accountDetails = uiState.jellyseerrProfile,
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
  SharedTransitionScopeProvider {
    AccountSettingsContent(
      uiState = uiState,
      transitionScope = it,
      animatedVisibilityScope = this,
      onLogoutClick = {},
      onLoginClick = {},
      onNavigateToJellyseerrLogin = {},
    )
  }
}
