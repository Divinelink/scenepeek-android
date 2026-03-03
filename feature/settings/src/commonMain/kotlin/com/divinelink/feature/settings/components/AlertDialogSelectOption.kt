package com.divinelink.feature.settings.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import com.divinelink.core.designsystem.theme.AppTheme
import com.divinelink.core.designsystem.theme.dimensions
import com.divinelink.core.ui.Previews
import com.divinelink.core.ui.TestTags
import com.divinelink.core.ui.extension.format

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun <T> AlertDialogSelectOption(
  title: String,
  listItems: List<T>,
  selectedOption: T,
  displayText: @Composable (T) -> String,
  onSelected: (T) -> Unit,
  onDismissRequest: () -> Unit,
) {
  val state = rememberLazyListState()

  LaunchedEffect(Unit) {
    state.scrollToItem(listItems.indexOf(selectedOption))
  }

  BasicAlertDialog(
    onDismissRequest = onDismissRequest,
    content = {
      Card(
        shape = MaterialTheme.shapes.extraLarge,
      ) {
        LazyColumn(
          modifier = Modifier.padding(vertical = MaterialTheme.dimensions.keyline_24),
          state = state,
        ) {
          item {
            Text(
              modifier = Modifier.padding(
                start = MaterialTheme.dimensions.keyline_24,
                bottom = MaterialTheme.dimensions.keyline_16,
              ),
              text = title,
              style = MaterialTheme.typography.headlineSmall,
            )
          }

          itemsIndexed(
            items = listItems.distinct(),
            key = { index, item -> "$index $item" },
          ) { _, option ->
            Row(
              verticalAlignment = Alignment.CenterVertically,
              modifier = Modifier
                .fillMaxWidth()
                .testTag(TestTags.Settings.RADIO_BUTTON_SELECT_OPTION.format(displayText(option)))
                .clickable { onSelected(option) },
            ) {
              RadioButton(
                modifier = Modifier.padding(start = MaterialTheme.dimensions.keyline_16),
                selected = selectedOption == option,
                onClick = { onSelected(option) },
              )

              Text(
                text = displayText(option),
                modifier = Modifier.padding(start = MaterialTheme.dimensions.keyline_8),
              )
            }
          }
        }
      }
    },
  )
}

@Previews
@Composable
private fun RadioSelectAlertDialog() {
  AppTheme {
    Surface {
      AlertDialogSelectOption(
        title = "Select an Option",
        listItems = listOf("Option 1", "Option 2", "Option 3"),
        displayText = { "Option 2" },
        selectedOption = 0,
        onSelected = {},
        onDismissRequest = {},
      )
    }
  }
}
