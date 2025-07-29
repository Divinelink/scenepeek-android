package com.divinelink.feature.add.to.account.modal

import android.content.Intent
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.divinelink.core.model.media.MediaItem
import com.divinelink.core.ui.TestTags
import com.divinelink.feature.add.to.account.modal.ui.ActionMenuContent
import kotlinx.coroutines.flow.collectLatest
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ActionMenuModal(
  mediaItem: MediaItem,
  entryPoint: ActionMenuEntryPoint,
  onDismissRequest: () -> Unit,
  onMultiSelect: (MediaItem) -> Unit = {},
  onNavigateToAddToList: (MediaItem) -> Unit = {},
  viewModel: ActionMenuViewModel = koinViewModel(
    key = "${mediaItem.id} ${mediaItem.mediaType.value}",
  ) { parametersOf(mediaItem, entryPoint) },
) {
  val uiState by viewModel.uiState.collectAsStateWithLifecycle()
  val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
  val context = LocalContext.current

  LaunchedEffect(Unit) {
    viewModel.shareUrl.collectLatest { url ->
      val shareIntent = Intent(Intent.ACTION_SEND).apply {
        type = "text/plain"
        putExtra(Intent.EXTRA_TEXT, url)
      }
      context.startActivity(Intent.createChooser(shareIntent, "Share via"))
      onDismissRequest()
    }
  }

  LaunchedEffect(Unit) {
    viewModel.addToList.collect {
      onNavigateToAddToList(it)
    }
  }

  ModalBottomSheet(
    modifier = Modifier.testTag(TestTags.Modal.REQUEST_SEASONS),
    shape = MaterialTheme.shapes.extraLarge,
    onDismissRequest = onDismissRequest,
    sheetState = sheetState,
    content = {
      ActionMenuContent(
        uiState = uiState,
        onAction = { intent ->
          when (intent) {
            ActionMenuIntent.MultiSelect -> {
              onDismissRequest()
              onMultiSelect(uiState.media)
            }
            ActionMenuIntent.RemoveFromList,
            ActionMenuIntent.AddToList,
            -> {
              onDismissRequest()
              viewModel.onAction(intent)
            }
            ActionMenuIntent.Share,
            -> viewModel.onAction(intent)
          }
        },
      )
    },
  )
}
