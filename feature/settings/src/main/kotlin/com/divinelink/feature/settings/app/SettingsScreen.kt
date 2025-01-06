package com.divinelink.feature.settings.app

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.HelpOutline
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material.icons.outlined.AutoAwesome
import androidx.compose.material.icons.outlined.DisplaySettings
import androidx.compose.material.icons.outlined.Link
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.divinelink.core.ui.IconWrapper
import com.divinelink.feature.settings.R
import com.divinelink.feature.settings.components.SettingsClickItem
import com.divinelink.feature.settings.components.SettingsDivider
import com.divinelink.feature.settings.components.SettingsScaffold
import com.divinelink.feature.settings.navigation.SettingsGraph
import com.divinelink.feature.settings.screens.destinations.AccountSettingsScreenDestination
import com.divinelink.feature.settings.screens.destinations.AppearanceSettingsScreenDestination
import com.divinelink.feature.settings.screens.destinations.DetailPreferencesSettingsScreenDestination
import com.divinelink.feature.settings.screens.destinations.HelpSettingsScreenDestination
import com.divinelink.feature.settings.screens.destinations.LinkHandlingSettingsScreenDestination
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@Destination<SettingsGraph>(start = true)
@Composable
fun SettingsScreen(navigator: DestinationsNavigator) {
  SettingsScaffold(
    title = stringResource(R.string.settings),
    onNavigationClick = navigator::navigateUp,
    navigationIconPainter = Icons.AutoMirrored.Rounded.ArrowBack,
  ) { paddingValues ->

    LazyColumn(
      contentPadding = paddingValues,
    ) {
      item {
        SettingsClickItem(
          icon = IconWrapper.Vector(Icons.Outlined.AccountCircle),
          text = stringResource(R.string.preferences__account),
          onClick = { navigator.navigate(AccountSettingsScreenDestination) },
        )
      }

      item {
        SettingsDivider()
      }

      item {
        SettingsClickItem(
          icon = IconWrapper.Vector(Icons.Outlined.AutoAwesome),
          text = stringResource(R.string.preferences__appearance),
          onClick = { navigator.navigate(AppearanceSettingsScreenDestination) },
        )
      }

      item {
        SettingsDivider()
      }

      item {
        SettingsClickItem(
          icon = IconWrapper.Vector(Icons.Outlined.DisplaySettings),
          text = stringResource(R.string.feature_settings_details_preferences),
          onClick = { navigator.navigate(DetailPreferencesSettingsScreenDestination) },
        )
      }

      item {
        SettingsDivider()
      }

      item {
        SettingsClickItem(
          icon = IconWrapper.Vector(Icons.Outlined.Link),
          text = stringResource(R.string.feature_settings_link_handling),
          onClick = { navigator.navigate(LinkHandlingSettingsScreenDestination) },
        )
      }

      item {
        SettingsDivider()
      }

      item {
        SettingsClickItem(
          icon = IconWrapper.Vector(Icons.AutoMirrored.Outlined.HelpOutline),
          text = stringResource(R.string.preferences__help),
          onClick = { navigator.navigate(HelpSettingsScreenDestination) },
        )
      }
    }
  }
}
