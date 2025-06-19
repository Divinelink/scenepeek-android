package com.divinelink.core.ui.components.modal.jellyseerr.manage

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import com.divinelink.core.ui.TestTags

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ManageSeriesModal(
  onDismissRequest: () -> Unit,
  onConfirm: () -> Unit,
) {
  val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

  ModalBottomSheet(
    modifier = Modifier
      .testTag(TestTags.Modal.BOTTOM_SHEET),
    shape = MaterialTheme.shapes.extraLarge,
    onDismissRequest = onDismissRequest,
    sheetState = sheetState,
    content = {
      LazyColumn(
        modifier = Modifier
          .testTag(TestTags.LAZY_COLUMN),
      ) {
      }
    },
  )
}
