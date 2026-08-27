package com.divinelink.feature.settings.app.account

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.PreviewParameter
import com.divinelink.core.designsystem.component.ScenePeekLazyColumn
import com.divinelink.core.designsystem.theme.dimensions
import com.divinelink.core.model.account.TMDBAccount
import com.divinelink.core.navigation.route.Navigation
import com.divinelink.core.ui.Previews
import com.divinelink.core.ui.SharedTransitionScopeProvider
import com.divinelink.feature.settings.components.SettingsDivider
import com.divinelink.feature.settings.components.SettingsExternalLinkItem
import com.divinelink.feature.settings.components.SettingsTextItem
import com.divinelink.feature.settings.provider.AccountDetailsParameterProvider
import com.divinelink.feature.settings.resources.Res
import com.divinelink.feature.settings.resources.danger_zone
import com.divinelink.feature.settings.resources.feature_settings_additional_features
import com.divinelink.feature.settings.resources.feature_settings_additional_features_summary
import com.divinelink.feature.settings.resources.feature_settings_delete_account
import com.divinelink.feature.settings.resources.feature_settings_delete_account_summary
import org.jetbrains.compose.resources.stringResource

@Composable
fun AccountSettingsContent(
  transitionScope: SharedTransitionScope,
  animatedVisibilityScope: AnimatedVisibilityScope,
  uiState: AccountSettingsViewState,
  onNavigate: (Navigation) -> Unit,
  onLogoutClick: () -> Unit,
  onLoginClick: () -> Unit,
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
        onNavigateToJellyseerrLogin = { onNavigate(Navigation.JellyseerrSettingsRoute(true)) },
      )
    }

    if (uiState.tmdbAccount is TMDBAccount.LoggedIn) {
      item {
        SettingsDivider()
      }

      item {
        Text(
          modifier = Modifier
            .padding(horizontal = MaterialTheme.dimensions.keyline_16),
          text = stringResource(Res.string.danger_zone),
          style = MaterialTheme.typography.titleSmall,
          color = MaterialTheme.colorScheme.error,
        )
        SettingsExternalLinkItem(
          text = stringResource(Res.string.feature_settings_delete_account),
          summary = stringResource(Res.string.feature_settings_delete_account_summary),
          url = "https://www.themoviedb.org/settings/delete-account",
          onNavigate = onNavigate,
        )
      }
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
      onNavigate = {},
    )
  }
}
