package com.divinelink.feature.settings.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import com.divinelink.core.designsystem.theme.dimensions

@Composable
fun <T> SettingsRadioButtonSingleSelection(
  modifier: Modifier = Modifier,
  options: List<T>,
  selectedOption: T,
  onSelectOption: (T) -> Unit,
  displayText: @Composable (T) -> String,
) {
  Column(modifier.selectableGroup()) {
    options.forEach { option ->
      Row(
        Modifier
          .fillMaxWidth()
          .selectable(
            selected = (option == selectedOption),
            onClick = { onSelectOption(option) },
            role = Role.RadioButton,
          )
          .padding(MaterialTheme.dimensions.keyline_16),
        verticalAlignment = Alignment.CenterVertically,
      ) {
        Text(
          text = displayText(option),
          style = MaterialTheme.typography.bodyLarge,
          modifier = Modifier.weight(1f),
        )

        RadioButton(
          selected = (option == selectedOption),
          onClick = null,
        )
      }
    }
  }
}
