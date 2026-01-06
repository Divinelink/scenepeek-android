package com.divinelink.feature.settings.app.appearance

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
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
import com.divinelink.core.designsystem.theme.dimensions
import com.divinelink.core.ui.UiString
import com.divinelink.core.ui.resources.core_ui_cancel
import com.divinelink.core.ui.resources.core_ui_okay
import com.divinelink.feature.settings.resources.Res
import com.divinelink.feature.settings.resources.feature_settings_choose_a_color
import com.github.skydoves.colorpicker.compose.BrightnessSlider
import com.github.skydoves.colorpicker.compose.ColorEnvelope
import com.github.skydoves.colorpicker.compose.HsvColorPicker
import com.github.skydoves.colorpicker.compose.rememberColorPickerController
import org.jetbrains.compose.resources.stringResource

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
          verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimensions.keyline_8),
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

          BrightnessSlider(
            modifier = Modifier
              .fillMaxWidth()
              .height(MaterialTheme.dimensions.keyline_36),
            controller = controller,
          )

          Row(
            horizontalArrangement = Arrangement.spacedBy(MaterialTheme.dimensions.keyline_16),
            verticalAlignment = Alignment.CenterVertically,
          ) {
            ColorSampleTile(colorLong = currentColor)

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
