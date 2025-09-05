package com.divinelink.feature.request.media

import androidx.compose.animation.Crossfade
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropUp
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.res.stringResource
import com.divinelink.core.designsystem.theme.dimensions
import com.divinelink.core.ui.UiString

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun <T> LceDropdownMenu(
  modifier: Modifier = Modifier,
  options: List<T>,
  currentInstance: LCEState<T>,
  label: @Composable () -> Unit,
  displayText: @Composable (T) -> String,
  onUpdate: (T) -> Unit,
) {
  var expanded by remember { mutableStateOf(false) }

  val rotationState by animateFloatAsState(
    targetValue = if (expanded) 180f else 0f,
    animationSpec = tween(durationMillis = 300),
    label = "IconRotationAnimation",
  )

  ExposedDropdownMenuBox(
    modifier = modifier.animateContentSize(),
    expanded = expanded,
    onExpandedChange = { expanded = !expanded },
  ) {
    Crossfade(currentInstance) { state ->
      when (state) {
        is LCEState.Content -> OutlinedTextField(
          modifier = Modifier
            .fillMaxWidth()
            .menuAnchor(MenuAnchorType.PrimaryEditable),
          readOnly = true,
          value = displayText(state.data),
          onValueChange = {},
          label = label,
          trailingIcon = {
            Icon(
              modifier = Modifier.rotate(rotationState),
              imageVector = Icons.Filled.ArrowDropUp,
              contentDescription = if (expanded) {
                stringResource(UiString.core_ui_collapse)
              } else {
                stringResource(UiString.core_ui_expand)
              },
            )
          },
        )

        LCEState.Error -> {
          // Optionally show error UI, or leave empty as before
        }

        LCEState.Loading -> OutlinedTextField(
          modifier = Modifier.fillMaxWidth(),
          readOnly = true,
          enabled = false,
          value = stringResource(UiString.core_ui_loading),
          onValueChange = {},
          label = label,
        )
      }
    }

    ExposedDropdownMenu(
      expanded = expanded,
      onDismissRequest = { expanded = false },
      containerColor = MaterialTheme.colorScheme.surfaceContainerHighest,
    ) {
      options.forEach { selectionOption ->
        DropdownMenuItem(
          text = {
            Text(
              text = displayText(selectionOption),
              style = MaterialTheme.typography.bodyMedium,
            )
          },
          onClick = {
            onUpdate(selectionOption)
            expanded = false
          },
          contentPadding = PaddingValues(
            horizontal = MaterialTheme.dimensions.keyline_16,
            vertical = MaterialTheme.dimensions.keyline_4,
          ),
        )
      }
    }
  }
}
