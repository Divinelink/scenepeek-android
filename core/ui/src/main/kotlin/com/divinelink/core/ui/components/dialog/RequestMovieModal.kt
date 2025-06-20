package com.divinelink.core.ui.components.dialog

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Download
import androidx.compose.material3.Button
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import com.divinelink.core.designsystem.theme.AppTheme
import com.divinelink.core.designsystem.theme.dimensions
import com.divinelink.core.ui.Previews
import com.divinelink.core.ui.R
import com.divinelink.core.ui.TestTags

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RequestMovieModal(
  title: String,
  onDismissRequest: () -> Unit,
  onConfirm: () -> Unit,
) {
  val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

  ModalBottomSheet(
    modifier = Modifier.testTag(TestTags.Dialogs.SELECT_SEASONS_DIALOG),
    shape = MaterialTheme.shapes.extraLarge,
    onDismissRequest = onDismissRequest,
    sheetState = sheetState,
    content = {
      Box {
        Column(
          modifier = Modifier
            .padding(vertical = MaterialTheme.dimensions.keyline_24)
            .padding(bottom = MaterialTheme.dimensions.keyline_96),
          verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimensions.keyline_16),
        ) {
          Text(
            modifier = Modifier.padding(horizontal = MaterialTheme.dimensions.keyline_16),
            text = stringResource(id = R.string.core_ui_request_movie),
            style = MaterialTheme.typography.headlineSmall,
          )

          Text(
            modifier = Modifier.padding(horizontal = MaterialTheme.dimensions.keyline_16),
            text = stringResource(id = R.string.core_ui_request_confirmation_text, title),
            style = MaterialTheme.typography.bodyLarge,
          )
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
              .testTag(TestTags.Dialogs.REQUEST_MOVIE_BUTTON)
              .padding(bottom = MaterialTheme.dimensions.keyline_8)
              .padding(horizontal = MaterialTheme.dimensions.keyline_16),
            onClick = {
              onConfirm()
              onDismissRequest()
            },
          ) {
            Row(
              horizontalArrangement = Arrangement.spacedBy(MaterialTheme.dimensions.keyline_8),
            ) {
              Icon(Icons.Default.Download, null)
              Text(text = stringResource(id = R.string.core_ui_request_movie))
            }
          }
        }
      }
    },
  )
}

@Previews
@Composable
private fun RequestMovieDialogPreview() {
  AppTheme {
    RequestMovieModal(
      title = "Movie Title",
      onDismissRequest = {},
      onConfirm = {},
    )
  }
}
