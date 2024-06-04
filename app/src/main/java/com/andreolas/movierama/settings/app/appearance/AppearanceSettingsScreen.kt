package com.andreolas.movierama.settings.app.appearance

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import com.andreolas.movierama.R
import com.andreolas.movierama.settings.components.SettingsDivider
import com.andreolas.movierama.settings.components.SettingsRadioPrefItem
import com.andreolas.movierama.settings.components.SettingsScaffold
import com.andreolas.movierama.settings.components.SettingsSwitchItem
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootGraph
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@Composable
@Destination<RootGraph>
fun AppearanceSettingsScreen(
  navigator: DestinationsNavigator,
  viewModel: AppearanceSettingsViewModel = hiltViewModel()
) {
  val viewState by viewModel.uiState.collectAsState()

  val resources = LocalContext.current.resources
  val themeLabels = resources.getStringArray(R.array.pref_theme_entries)
  val themeValues = resources.getStringArray(R.array.pref_theme_values)

  SettingsScaffold(
    title = stringResource(id = R.string.preferences__appearance),
    onNavigationClick = navigator::navigateUp
  ) {

    LazyColumn(contentPadding = it) {
      item {
        SettingsRadioPrefItem(
          title = stringResource(id = R.string.preferences__theme),
          selected = themeLabels[viewState.theme.ordinal],
          selectedIndex = themeValues.indexOf(viewState.theme.storageKey),
          listItems = themeLabels.toList(),
          onSelected = { index ->
            viewModel.setTheme(viewState.availableThemes[index])
          }
        )
      }

      item {
        SettingsDivider()
        SettingsSwitchItem(
          title = stringResource(R.string.AppearanceSettingsScreen__black_backgrounds),
          summary = stringResource(R.string.AppearanceSettingsScreen__black_backgrounds_summary),
          isChecked = viewState.blackBackgroundsEnabled,
          onCheckedChange = viewModel::setBlackBackgrounds
        )
      }

      if (viewState.materialYouVisible) {
        item {
          SettingsDivider()
          SettingsSwitchItem(
            title = stringResource(id = R.string.AppearanceSettingsScreen__material_you),
            summary = stringResource(id = R.string.AppearanceSettingsScreen__material_you_summary),
            isChecked = viewState.materialYouEnabled,
            onCheckedChange = viewModel::setMaterialYou
          )
        }
      }
    }
  }
}
