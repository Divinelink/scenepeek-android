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
import com.divinelink.core.commons.platform.Platform
import com.divinelink.core.designsystem.component.ScenePeekLazyColumn
import com.divinelink.core.navigation.route.Navigation
import com.divinelink.core.ui.IconWrapper
import com.divinelink.core.ui.TestTags
import com.divinelink.core.ui.rememberCurrentPlatform
import com.divinelink.feature.settings.Res
import com.divinelink.feature.settings.components.SettingsClickItem
import com.divinelink.feature.settings.components.SettingsDivider
import com.divinelink.feature.settings.components.SettingsScaffold
import com.divinelink.feature.settings.feature_settings_about
import com.divinelink.feature.settings.feature_settings_details_preferences
import com.divinelink.feature.settings.feature_settings_link_handling
import com.divinelink.feature.settings.preferences__account
import com.divinelink.feature.settings.preferences__appearance
import com.divinelink.feature.settings.settings
import org.jetbrains.compose.resources.stringResource

@Composable
fun SettingsScreen(
  animatedVisibilityScope: AnimatedVisibilityScope,
  onNavigate: (Navigation) -> Unit,
) {
  val currentPlatform = rememberCurrentPlatform()

  SettingsScaffold(
    title = stringResource(Res.string.settings),
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
          text = stringResource(Res.string.preferences__account),
          onClick = { onNavigate(Navigation.AccountSettingsRoute) },
        )
      }

      item {
        SettingsDivider()
      }

      item {
        SettingsClickItem(
          icon = IconWrapper.Vector(Icons.Outlined.AutoAwesome),
          text = stringResource(Res.string.preferences__appearance),
          onClick = { onNavigate(Navigation.AppearanceSettingsRoute) },
        )
      }

      item {
        SettingsDivider()
      }

      item {
        SettingsClickItem(
          icon = IconWrapper.Vector(Icons.Outlined.DisplaySettings),
          text = stringResource(Res.string.feature_settings_details_preferences),
          onClick = { onNavigate(Navigation.DetailsPreferencesSettingsRoute) },
        )
      }

      item {
        SettingsDivider()
      }

      if (currentPlatform == Platform.Android) {
        item {
          SettingsClickItem(
            icon = IconWrapper.Vector(Icons.Outlined.Link),
            text = stringResource(Res.string.feature_settings_link_handling),
            onClick = { onNavigate(Navigation.LinkHandlingSettingsRoute) },
          )
        }

        item {
          SettingsDivider()
        }
      }

      item {
        SettingsClickItem(
          icon = IconWrapper.Vector(Icons.AutoMirrored.Outlined.HelpOutline),
          text = stringResource(Res.string.feature_settings_about),
          onClick = { onNavigate(Navigation.AboutSettingsRoute) },
        )
      }
    }
  }
}
