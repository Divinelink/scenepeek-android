package com.divinelink.feature.settings.app.appearance

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.divinelink.core.designsystem.component.ScenePeekLazyColumn
import com.divinelink.core.designsystem.theme.dimensions
import com.divinelink.core.designsystem.theme.model.ColorSystem
import com.divinelink.core.ui.UiString
import com.divinelink.core.ui.resources.core_ui_cancel
import com.divinelink.core.ui.resources.core_ui_okay
import com.divinelink.feature.settings.components.SettingsDivider
import com.divinelink.feature.settings.components.SettingsRadioButtonSingleSelection
import com.divinelink.feature.settings.components.SettingsRadioPrefItem
import com.divinelink.feature.settings.components.SettingsScaffold
import com.divinelink.feature.settings.components.SettingsSwitchItem
import com.divinelink.feature.settings.resources.AppearanceSettingsScreen__black_backgrounds
import com.divinelink.feature.settings.resources.AppearanceSettingsScreen__black_backgrounds_summary
import com.divinelink.feature.settings.resources.Res
import com.divinelink.feature.settings.resources.feature_settings_choose_a_color
import com.divinelink.feature.settings.resources.feature_settings_color
import com.divinelink.feature.settings.resources.feature_settings_color_preference
import com.divinelink.feature.settings.resources.preferences__appearance
import com.divinelink.feature.settings.resources.preferences__theme
import com.github.skydoves.colorpicker.compose.ColorEnvelope
import com.github.skydoves.colorpicker.compose.HsvColorPicker
import com.github.skydoves.colorpicker.compose.rememberColorPickerController
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun AppearanceSettingsScreen(
  animatedVisibilityScope: AnimatedVisibilityScope,
  onNavigateUp: () -> Unit,
  viewModel: AppearanceSettingsViewModel = koinViewModel(),
) {
  val uiState by viewModel.uiState.collectAsStateWithLifecycle()

  val themeValues = uiState.availableThemes.map { it.storageKey }
  var showColorWheel by remember { mutableStateOf(false) }

  if (showColorWheel) {
    SelectColorDialog(
      onDismissRequest = { showColorWheel = false },
      onConfirm = viewModel::setThemeColor,
      initialColorLong = uiState.themePreferences.themeColor,
    )
  }

  SettingsScaffold(
    animatedVisibilityScope = animatedVisibilityScope,
    title = stringResource(Res.string.preferences__appearance),
    onNavigationClick = onNavigateUp,
  ) {
    ScenePeekLazyColumn {
      item {
        SettingsRadioPrefItem(
          title = stringResource(Res.string.preferences__theme),
          selected = stringResource(uiState.themePreferences.theme.label),
          selectedIndex = themeValues.indexOf(uiState.themePreferences.theme.storageKey),
          listItems = uiState.availableThemes.map { theme ->
            stringResource(theme.label)
          },
          onSelected = { index ->
            viewModel.setTheme(uiState.availableThemes[index])
          },
        )
      }

      item {
        SettingsDivider()
        SettingsSwitchItem(
          title = stringResource(Res.string.AppearanceSettingsScreen__black_backgrounds),
          summary = stringResource(Res.string.AppearanceSettingsScreen__black_backgrounds_summary),
          isChecked = uiState.themePreferences.isPureBlack,
          onCheckedChange = viewModel::setBlackBackgrounds,
        )
      }

      item {
        SettingsDivider()

        Text(
          modifier = Modifier.padding(MaterialTheme.dimensions.keyline_16),
          text = stringResource(Res.string.feature_settings_color_preference),
          style = MaterialTheme.typography.titleMedium,
        )

        SettingsRadioButtonSingleSelection(
          modifier = Modifier,
          options = uiState.availableColorSystems,
          displayText = { option -> stringResource(option.resource) },
          onSelectOption = viewModel::updateColorSystem,
          selectedOption = uiState.themePreferences.colorSystem,
        )
      }

      item {
        AnimatedVisibility(
          visible = uiState.themePreferences.colorSystem == ColorSystem.Custom,
          enter = slideInVertically() + expandVertically(),
          exit = slideOutVertically() + shrinkVertically(),
        ) {
          HorizontalDivider()

          Row(
            modifier = Modifier
              .clickable(onClick = { showColorWheel = true })
              .padding(MaterialTheme.dimensions.keyline_16)
              .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
          ) {
            Text(
              modifier = Modifier.weight(1f),
              style = MaterialTheme.typography.bodyMedium,
              text = stringResource(Res.string.feature_settings_color),
            )

            ColorSampleBox(colorLong = uiState.themePreferences.themeColor)
          }
        }
      }
    }
  }
}

@Composable
fun SelectColorDialog(
  onDismissRequest: () -> Unit,
  onConfirm: (Long) -> Unit,
  initialColorLong: Long,
) {
  val controller = rememberColorPickerController()
  var currentColor by remember { mutableStateOf(initialColorLong) }
  var hex by remember { mutableStateOf<String?>(null) }

  Dialog(
    onDismissRequest = onDismissRequest,
    content = {
      Surface(
        shape = MaterialTheme.shapes.extraLarge,
        color = MaterialTheme.colorScheme.surfaceContainerHigh,
      ) {
        Column(
          modifier = Modifier.padding(MaterialTheme.dimensions.keyline_16),
        ) {
          Text(
            text = stringResource(Res.string.feature_settings_choose_a_color),
            style = MaterialTheme.typography.titleLarge,
          )

          HsvColorPicker(
            modifier = Modifier
              .fillMaxWidth()
              .height(250.dp)
              .padding(MaterialTheme.dimensions.keyline_40),
            controller = controller,
            initialColor = Color(initialColorLong.toULong()),
            onColorChanged = { colorEnvelope: ColorEnvelope ->
              currentColor = colorEnvelope.color.value.toLong()
              hex = colorEnvelope.hexCode
            },
          )

          Row(
            horizontalArrangement = Arrangement.spacedBy(MaterialTheme.dimensions.keyline_16),
            verticalAlignment = Alignment.CenterVertically,
          ) {
            ColorSampleBox(currentColor)

            hex?.let { Text(it.uppercase()) }
          }

          Row(
            modifier = Modifier
              .padding(top = MaterialTheme.dimensions.keyline_16)
              .fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(MaterialTheme.dimensions.keyline_8),
          ) {
            Spacer(modifier = Modifier.weight(1f))

            Button(
              onClick = onDismissRequest,
            ) {
              Text(stringResource(UiString.core_ui_cancel))
            }

            Button(
              onClick = {
                onConfirm(currentColor)
                onDismissRequest()
              },
            ) {
              Text(stringResource(UiString.core_ui_okay))
            }
          }
        }
      }
    },
  )
}
