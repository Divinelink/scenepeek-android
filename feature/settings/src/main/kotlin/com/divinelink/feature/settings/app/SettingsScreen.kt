package com.divinelink.feature.settings.app

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.HelpOutline
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material.icons.outlined.AutoAwesome
import androidx.compose.material.icons.outlined.DisplaySettings
import androidx.compose.material.icons.outlined.Link
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import com.divinelink.core.designsystem.component.ScenePeekLazyColumn
import com.divinelink.core.navigation.route.Navigation
import com.divinelink.core.ui.IconWrapper
import com.divinelink.core.ui.TestTags
import com.divinelink.feature.settings.R
import com.divinelink.feature.settings.components.SettingsClickItem
import com.divinelink.feature.settings.components.SettingsDivider
import com.divinelink.feature.settings.components.SettingsScaffold

@Composable
fun SettingsScreen(
  animatedVisibilityScope: AnimatedVisibilityScope,
  onNavigate: (Navigation) -> Unit,
) {
  SettingsScaffold(
    title = stringResource(R.string.settings),
    onNavigationClick = { onNavigate(Navigation.Back) },
    animatedVisibilityScope = animatedVisibilityScope,
    navigationIconPainter = Icons.AutoMirrored.Rounded.ArrowBack,
  ) {
    ScenePeekLazyColumn(
      modifier = Modifier.testTag(TestTags.Settings.SCREEN_CONTENT),
    ) {
      item {
        SettingsClickItem(
          icon = IconWrapper.Vector(Icons.Outlined.AccountCircle),
          text = stringResource(R.string.preferences__account),
          onClick = { onNavigate(Navigation.AccountSettingsRoute) },
        )
      }

      item {
        SettingsDivider()
      }

      item {
        SettingsClickItem(
          icon = IconWrapper.Vector(Icons.Outlined.AutoAwesome),
          text = stringResource(R.string.preferences__appearance),
          onClick = { onNavigate(Navigation.AppearanceSettingsRoute) },
        )
      }

      item {
        SettingsDivider()
      }

      item {
        SettingsClickItem(
          icon = IconWrapper.Vector(Icons.Outlined.DisplaySettings),
          text = stringResource(R.string.feature_settings_details_preferences),
          onClick = { onNavigate(Navigation.DetailsPreferencesSettingsRoute) },
        )
      }

      item {
        SettingsDivider()
      }

      item {
        SettingsClickItem(
          icon = IconWrapper.Vector(Icons.Outlined.Link),
          text = stringResource(R.string.feature_settings_link_handling),
          onClick = { onNavigate(Navigation.LinkHandlingSettingsRoute) },
        )
      }

      item {
        SettingsDivider()
      }

      item {
        SettingsClickItem(
          icon = IconWrapper.Vector(Icons.AutoMirrored.Outlined.HelpOutline),
          text = stringResource(R.string.feature_settings_about),
          onClick = { onNavigate(Navigation.AboutSettingsRoute) },
        )
      }
    }
  }
}
