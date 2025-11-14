package com.divinelink.feature.settings.app.appearance

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.divinelink.core.designsystem.component.ScenePeekLazyColumn
import com.divinelink.feature.settings.AppearanceSettingsScreen__black_backgrounds
import com.divinelink.feature.settings.AppearanceSettingsScreen__black_backgrounds_summary
import com.divinelink.feature.settings.AppearanceSettingsScreen__material_you
import com.divinelink.feature.settings.AppearanceSettingsScreen__material_you_summary
import com.divinelink.feature.settings.Res
import com.divinelink.feature.settings.components.SettingsDivider
import com.divinelink.feature.settings.components.SettingsRadioPrefItem
import com.divinelink.feature.settings.components.SettingsScaffold
import com.divinelink.feature.settings.components.SettingsSwitchItem
import com.divinelink.feature.settings.preferences__appearance
import com.divinelink.feature.settings.preferences__theme
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun AppearanceSettingsScreen(
  animatedVisibilityScope: AnimatedVisibilityScope,
  onNavigateUp: () -> Unit,
  viewModel: AppearanceSettingsViewModel = koinViewModel(),
) {
  val viewState by viewModel.uiState.collectAsState()

  val themeValues = viewState.availableThemes.map { it.storageKey }

  SettingsScaffold(
    animatedVisibilityScope = animatedVisibilityScope,
    title = stringResource(Res.string.preferences__appearance),
    onNavigationClick = onNavigateUp,
  ) {
    ScenePeekLazyColumn {
      item {
        SettingsRadioPrefItem(
          title = stringResource(Res.string.preferences__theme),
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
          title = stringResource(Res.string.AppearanceSettingsScreen__black_backgrounds),
          summary = stringResource(Res.string.AppearanceSettingsScreen__black_backgrounds_summary),
          isChecked = viewState.blackBackgroundsEnabled,
          onCheckedChange = viewModel::setBlackBackgrounds,
        )
      }

      if (viewState.materialYouVisible) {
        item {
          SettingsDivider()
          SettingsSwitchItem(
            title = stringResource(Res.string.AppearanceSettingsScreen__material_you),
            summary = stringResource(Res.string.AppearanceSettingsScreen__material_you_summary),
            isChecked = viewState.materialYouEnabled,
            onCheckedChange = viewModel::setMaterialYou,
          )
        }
      }
    }
  }
}
