package com.divinelink.feature.lists.create.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import com.divinelink.core.designsystem.theme.dimensions
import com.divinelink.core.ui.TestTags
import com.divinelink.core.ui.coil.ListItemBackdropImage

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SelectBackdropModal(
  onDismissRequest: () -> Unit,
  onBackdropSelected: (String) -> Unit,
  backdrops: List<Pair<String, String>>,
) {
  val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
  ModalBottomSheet(
    modifier = Modifier
      .testTag(TestTags.Modal.BOTTOM_SHEET),
    shape = MaterialTheme.shapes.extraLarge,
    onDismissRequest = {
      onDismissRequest()
    },
    sheetState = sheetState,
    content = {
      LazyColumn(
        verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimensions.keyline_4),
      ) {
        itemsIndexed(
          items = backdrops,
          key = { index, backdrop ->
            backdrop.first + index
          },
        ) { _, backdrop ->
          Row(
            modifier = Modifier
              .fillMaxWidth()
              .clickable {
                onBackdropSelected(backdrop.second)
                onDismissRequest()
              }
              .padding(
                horizontal = MaterialTheme.dimensions.keyline_16,
                vertical = MaterialTheme.dimensions.keyline_8,
              ),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
          ) {
            ListItemBackdropImage(
              modifier = Modifier
                .width(160.dp),
              url = backdrop.second,
            )

            Text(
              text = backdrop.first,
              style = MaterialTheme.typography.bodyLarge,
              color = MaterialTheme.colorScheme.onSurface,
              modifier = Modifier
                .weight(1f)
                .padding(horizontal = MaterialTheme.dimensions.keyline_16),
            )
          }
        }
      }
    },
  )
}
