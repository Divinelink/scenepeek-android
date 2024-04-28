package com.andreolas.movierama.settings.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import com.andreolas.movierama.R
import com.andreolas.movierama.settings.app.appearance.AlertDialogSelectOption
import com.andreolas.movierama.ui.theme.dimensions

@Composable
fun SettingsRadioPrefItem(
  icon: Painter? = null,
  title: String,
  selected: String,
  selectedIndex: Int,
  listItems: List<String>,
  onSelected: (Int) -> Unit
) {
  var showDialog by remember { mutableStateOf(false) }

  if (showDialog) {
    AlertDialogSelectOption(
      title = title,
      listItems = listItems,
      selectedOption = selectedIndex,
      onSelected = onSelected,
      onDismissRequest = { showDialog = false }
    )
  }

  Row(
    verticalAlignment = androidx.compose.ui.Alignment.CenterVertically,
    horizontalArrangement = Arrangement.spacedBy(MaterialTheme.dimensions.keyline_16),
    modifier = Modifier
      .clickable {
        showDialog = true
      }
      .padding(MaterialTheme.dimensions.keyline_16)
      .fillMaxWidth()
  ) {
    icon?.let {
      Icon(
        painter = icon,
        contentDescription = null
      )
    }

    Column {

      Text(
        text = title,
        style = MaterialTheme.typography.bodyLarge
      )

      Text(
        text = selected,
        style = MaterialTheme.typography.bodyMedium,
        color = MaterialTheme.colorScheme.secondary
      )
    }
  }
}

@Composable
@Preview(showBackground = true)
private fun SettingsScreenPreview() {
  MaterialTheme {
    SettingsRadioPrefItem(
      icon = painterResource(id = R.drawable.ic_appearance_24),
      title = "Theme",
      selected = "System default",
      selectedIndex = 0,
      listItems = listOf("System default", "Light", "Dark"),
      onSelected = {},
    )
  }
}
