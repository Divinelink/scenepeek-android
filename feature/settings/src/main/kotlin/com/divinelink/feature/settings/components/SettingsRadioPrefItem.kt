package com.divinelink.feature.settings.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
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
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import com.divinelink.core.designsystem.theme.AppTheme
import com.divinelink.core.designsystem.theme.dimensions
import com.divinelink.core.ui.Previews
import com.divinelink.feature.settings.R

@Composable
fun SettingsRadioPrefItem(
  icon: Painter? = null,
  title: String,
  selected: String,
  selectedIndex: Int,
  listItems: List<String>,
  onSelected: (Int) -> Unit,
) {
  var showDialog by remember { mutableStateOf(false) }

  if (showDialog) {
    AlertDialogSelectOption(
      title = title,
      listItems = listItems,
      selectedOption = selectedIndex,
      onSelected = {
        showDialog = false
        onSelected(it)
      },
      onDismissRequest = { showDialog = false },
    )
  }

  Row(
    verticalAlignment = Alignment.CenterVertically,
    horizontalArrangement = Arrangement.spacedBy(MaterialTheme.dimensions.keyline_16),
    modifier = Modifier
      .clickable {
        showDialog = true
      }
      .padding(MaterialTheme.dimensions.keyline_16)
      .fillMaxWidth(),
  ) {
    icon?.let {
      Icon(
        painter = icon,
        contentDescription = null,
      )
    }

    Column(
      verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimensions.keyline_4),
    ) {
      Text(
        text = title,
        style = MaterialTheme.typography.bodyLarge,
      )
      Text(
        text = selected,
        style = MaterialTheme.typography.bodyMedium,
        color = MaterialTheme.colorScheme.secondary,
      )
    }
  }
}

@Composable
@Previews
private fun SettingsScreenPreview() {
  AppTheme {
    Surface {
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
}
