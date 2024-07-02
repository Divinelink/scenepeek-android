package com.divinelink.core.ui.components.dialog

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.AlertDialogDefaults
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import com.divinelink.core.designsystem.theme.AppTheme
import com.divinelink.core.designsystem.theme.dimensions
import com.divinelink.core.ui.Previews
import com.divinelink.core.ui.R
import com.divinelink.core.ui.TestTags

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SelectSeasonsDialog(
  numberOfSeasons: Int,
  onRequestClick: (List<Int>) -> Unit,
  onDismissRequest: () -> Unit,
) {
  val selectedSeasons = remember { mutableStateListOf<Int>() }

  BasicAlertDialog(
    modifier = Modifier.testTag(TestTags.Dialogs.SELECT_SEASONS_DIALOG),
    onDismissRequest = onDismissRequest,
    content = {
      Card(
        elevation = CardDefaults.cardElevation(
          defaultElevation = AlertDialogDefaults.TonalElevation,
        ),
        colors = CardDefaults.cardColors(
          containerColor = AlertDialogDefaults.containerColor,
        ),
        shape = MaterialTheme.shapes.extraLarge,
      ) {
        Box {
          LazyColumn(
            modifier = Modifier
              .padding(vertical = MaterialTheme.dimensions.keyline_24)
              .padding(bottom = MaterialTheme.dimensions.keyline_84),
          ) {
            item {
              Text(
                modifier = Modifier.padding(MaterialTheme.dimensions.keyline_16),
                text = stringResource(id = R.string.core_ui_request_series),
                style = MaterialTheme.typography.headlineSmall,
              )
            }
            itemsIndexed(
              items = (1..numberOfSeasons).toList(),
              key = { index, _ -> "$index" },
            ) { _, index ->
              Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                  .fillMaxWidth()
                  .clickable {
                    if (selectedSeasons.contains(index)) {
                      selectedSeasons.remove(index)
                    } else {
                      selectedSeasons.add(index)
                    }
                  },
              ) {
                RadioButton(
                  modifier = Modifier
                    .testTag(TestTags.RadioButton.SELECT_SEASON_RADIO_BUTTON.format(index))
                    .padding(start = MaterialTheme.dimensions.keyline_16),
                  selected = selectedSeasons.contains(index),
                  onClick = {
                    if (selectedSeasons.contains(index)) {
                      selectedSeasons.remove(index)
                    } else {
                      selectedSeasons.add(index)
                    }
                  },
                )
                Text(
                  text = stringResource(id = R.string.core_ui_season_number, index),
                  modifier = Modifier.padding(start = MaterialTheme.dimensions.keyline_8),
                )
              }
            }
          }

          Column(
            modifier = Modifier.align(Alignment.BottomCenter),
          ) {
            ElevatedButton(
              modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = MaterialTheme.dimensions.keyline_16),
              onClick = { onDismissRequest() },
            ) {
              Text(text = stringResource(id = R.string.core_ui_cancel))
            }
            Button(
              modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = MaterialTheme.dimensions.keyline_8)
                .padding(horizontal = MaterialTheme.dimensions.keyline_16),
              enabled = selectedSeasons.isNotEmpty(),
              onClick = {
                onRequestClick(selectedSeasons)
                onDismissRequest()
              },
            ) {
              val text = if (selectedSeasons.isEmpty()) {
                stringResource(id = R.string.core_ui_select_seasons_button)
              } else {
                pluralStringResource(
                  id = R.plurals.core_ui_request_series_button,
                  count = selectedSeasons.size,
                  selectedSeasons.size,
                )
              }

              Text(text = text)
            }
          }
        }
      }
    },
  )
}

@Previews
@Composable
private fun SelectSeasonsDialogPreview() {
  AppTheme {
    Surface {
      SelectSeasonsDialog(
        numberOfSeasons = 20,
        onRequestClick = {},
        onDismissRequest = {},
      )
    }
  }
}
