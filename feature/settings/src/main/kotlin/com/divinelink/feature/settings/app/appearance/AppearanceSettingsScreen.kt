package com.divinelink.feature.settings.app.appearance

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import com.divinelink.core.designsystem.component.ScenePeekLazyColumn
import com.divinelink.feature.settings.R
import com.divinelink.feature.settings.components.SettingsDivider
import com.divinelink.feature.settings.components.SettingsRadioPrefItem
import com.divinelink.feature.settings.components.SettingsScaffold
import com.divinelink.feature.settings.components.SettingsSwitchItem
import org.koin.androidx.compose.koinViewModel

@Composable
fun AppearanceSettingsScreen(
  animatedVisibilityScope: AnimatedVisibilityScope,
  onNavigateUp: () -> Unit,
  viewModel: AppearanceSettingsViewModel = koinViewModel(),
) {
  val viewState by viewModel.uiState.collectAsState()

  val resources = LocalContext.current.resources
  val themeValues = resources.getStringArray(R.array.pref_theme_values)

  SettingsScaffold(
    animatedVisibilityScope = animatedVisibilityScope,
    title = stringResource(id = R.string.preferences__appearance),
    onNavigationClick = onNavigateUp,
  ) {
    ScenePeekLazyColumn {
      item {
        SettingsRadioPrefItem(
          title = stringResource(id = R.string.preferences__theme),
          selected = stringResource(viewState.theme.label),
          selectedIndex = themeValues.indexOf(viewState.theme.storageKey),
          listItems = viewState.availableThemes.map { theme ->
            stringResource(theme.label)
          },
          onSelected = { index ->
            viewModel.setTheme(viewState.availableThemes[index])
          },
        )
      }

      item {
        SettingsDivider()
        SettingsSwitchItem(
          title = stringResource(R.string.AppearanceSettingsScreen__black_backgrounds),
          summary = stringResource(R.string.AppearanceSettingsScreen__black_backgrounds_summary),
          isChecked = viewState.blackBackgroundsEnabled,
          onCheckedChange = viewModel::setBlackBackgrounds,
        )
      }

      if (viewState.materialYouVisible) {
        item {
          SettingsDivider()
          SettingsSwitchItem(
            title = stringResource(id = R.string.AppearanceSettingsScreen__material_you),
            summary = stringResource(id = R.string.AppearanceSettingsScreen__material_you_summary),
            isChecked = viewState.materialYouEnabled,
            onCheckedChange = viewModel::setMaterialYou,
          )
        }
      }
    }
  }
}
