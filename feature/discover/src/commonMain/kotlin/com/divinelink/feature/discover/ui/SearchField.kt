package com.divinelink.feature.discover.ui

import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import com.divinelink.core.ui.UiString
import com.divinelink.core.ui.resources.core_ui_toolbar_search
import com.divinelink.core.ui.text.BasicTextFieldWithCursorAtEnd
import org.jetbrains.compose.resources.stringResource

@Composable
fun SearchField(
  modifier: Modifier = Modifier,
  value: String?,
  onValueChange: (String) -> Unit,
) {
  Surface {
    BasicTextFieldWithCursorAtEnd(
      modifier = modifier,
      value = value ?: "",
      textStyle = TextStyle(
        fontFamily = MaterialTheme.typography.bodyMedium.fontFamily,
        fontSize = MaterialTheme.typography.bodyMedium.fontSize,
        color = MaterialTheme.colorScheme.onSurface,
      ),
      cursorBrush = SolidColor(MaterialTheme.colorScheme.primary),
      keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
      keyboardActions = KeyboardActions.Default,
      onValueChange = onValueChange,
      singleLine = true,
      decorationBox = { innerTextField ->
        if (value.isNullOrEmpty()) {
          Text(
            textAlign = TextAlign.Start,
            text = stringResource(UiString.core_ui_toolbar_search),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
          )
        }
        innerTextField()
      },
    )
  }
}
