package com.divinelink.core.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.PlaylistAdd
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.divinelink.core.designsystem.theme.AppTheme
import com.divinelink.core.designsystem.theme.dimensions
import com.divinelink.core.designsystem.theme.shape
import com.divinelink.core.ui.Previews
import com.divinelink.core.ui.R

@Composable
fun AddToListButton(
  modifier: Modifier = Modifier,
  onClick: () -> Unit,
) {
  ElevatedButton(
    modifier = modifier,
    onClick = onClick,
    elevation = ButtonDefaults.buttonElevation(
      defaultElevation = MaterialTheme.dimensions.keyline_2,
    ),
    shape = MaterialTheme.shape.medium,
  ) {
    Icon(
      imageVector = Icons.AutoMirrored.Filled.PlaylistAdd,
      tint = MaterialTheme.colorScheme.primary,
      contentDescription = stringResource(R.string.core_ui_add_to_list_content_desc),
    )
  }
}

@Previews
@Composable
private fun AddToListButtonPreview() {
  AppTheme {
    Surface {
      AddToListButton(
        onClick = {},
      )
    }
  }
}
