package com.divinelink.core.ui

import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import com.divinelink.core.designsystem.theme.AppTheme

@Composable
fun PasswordOutlinedTextField(
  modifier: Modifier = Modifier,
  colors: TextFieldColors = TextFieldDefaults.colors(),
  value: String,
  onValueChange: (String) -> Unit,
) {
  var isMasked by rememberSaveable { mutableStateOf(true) }

  OutlinedTextField(
    modifier = modifier,
    colors = colors,
    label = { Text(text = stringResource(R.string.core_ui_password)) },
    value = value,
    singleLine = true,
    visualTransformation = if (isMasked) {
      PasswordVisualTransformation()
    } else {
      VisualTransformation.None
    },
    keyboardOptions = KeyboardOptions(
      keyboardType = KeyboardType.Password,
      imeAction = ImeAction.Next,
    ),
    onValueChange = onValueChange,
    trailingIcon = {
      val image = if (isMasked) {
        Icons.Filled.VisibilityOff
      } else {
        Icons.Filled.Visibility
      }

      IconButton(onClick = { isMasked = !isMasked }) {
        Icon(
          imageVector = image,
          contentDescription = stringResource(
            R.string.core_ui_toggle_password_visibility_button,
          ),
        )
      }
    },
  )
}

@Previews
@Composable
private fun PasswordOutlinedTextFieldPreview() {
  AppTheme {
    PasswordOutlinedTextField(
      value = "Password",
      onValueChange = {},
    )
  }
}
