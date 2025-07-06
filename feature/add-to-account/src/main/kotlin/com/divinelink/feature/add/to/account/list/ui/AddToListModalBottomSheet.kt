package com.divinelink.feature.add.to.account.list.ui

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.divinelink.core.ui.TestTags
import com.divinelink.feature.add.to.account.list.AddToListAction
import com.divinelink.feature.add.to.account.list.AddToListViewModel
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddToListModalBottomSheet(
  onDismissRequest: () -> Unit,
  viewModel: AddToListViewModel = koinViewModel(),
) {
  val uiState by viewModel.uiState.collectAsStateWithLifecycle()
  val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

  ModalBottomSheet(
    modifier = Modifier
      .testTag(TestTags.Modal.BOTTOM_SHEET),
    shape = MaterialTheme.shapes.extraLarge,
    onDismissRequest = {
      viewModel.onUserInteraction(AddToListAction.ConsumeDisplayMessage)
      onDismissRequest()
    },
    sheetState = sheetState,
    content = {
      AddToListContent(
        uiState = uiState,
        userInteraction = viewModel::onUserInteraction,
      )
    },
  )
}
