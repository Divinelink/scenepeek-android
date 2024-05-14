package com.andreolas.movierama.settings.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.divinelink.core.designsystem.theme.AppTheme
import com.divinelink.core.designsystem.theme.dimensions

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AlertDialogSelectOption(
  title: String,
  listItems: List<String>,
  selectedOption: Int,
  onSelected: (Int) -> Unit,
  onDismissRequest: () -> Unit
) {
  AlertDialog(
    onDismissRequest = onDismissRequest,
    content = {
      Card(
        shape = MaterialTheme.shapes.extraLarge
      ) {
        Column(
          modifier = Modifier.padding(vertical = MaterialTheme.dimensions.keyline_24)
        ) {
          Text(
            modifier = Modifier.padding(
              start = MaterialTheme.dimensions.keyline_24,
              bottom = MaterialTheme.dimensions.keyline_16
            ),
            text = title,
            style = MaterialTheme.typography.headlineSmall,
          )

          listItems.forEachIndexed { index, option ->
            Row(
              verticalAlignment = Alignment.CenterVertically,
              modifier = Modifier
                .fillMaxWidth()
                .clickable {
                  onSelected(index)
                }
            ) {
              RadioButton(
                modifier = Modifier.padding(start = MaterialTheme.dimensions.keyline_16),
                selected = selectedOption == index,
                onClick = { onSelected(index) }
              )
              Text(
                text = option,
                modifier = Modifier.padding(start = MaterialTheme.dimensions.keyline_8)
              )
            }
          }
        }
      }
    }
  )
}

@Preview(showBackground = true)
@Composable
private fun RadioSelectAlertDialog() {
  AppTheme {
    AlertDialogSelectOption(
      title = "Select an Option",
      listItems = listOf("Option 1", "Option 2", "Option 3"),
      selectedOption = 0,
      onSelected = {},
      onDismissRequest = {}
    )
  }
}
