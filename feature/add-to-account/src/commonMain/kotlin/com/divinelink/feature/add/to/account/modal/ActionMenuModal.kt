package com.divinelink.feature.add.to.account.modal

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Surface
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.divinelink.core.designsystem.theme.AppTheme
import com.divinelink.core.model.media.MediaItem
import com.divinelink.core.ui.Previews
import com.divinelink.core.ui.TestTags
import com.divinelink.core.ui.composition.LocalIntentManager
import com.divinelink.core.ui.snackbar.SnackbarMessageHandler
import com.divinelink.feature.add.to.account.modal.ui.ActionMenuContent
import com.divinelink.feature.add.to.account.modal.ui.provider.ActionMenuUiStateParameterProvider
import kotlinx.coroutines.flow.collectLatest
import org.jetbrains.compose.ui.tooling.preview.PreviewParameter
import org.koin.compose.viewmodel.koinViewModel
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
  val intentManager = LocalIntentManager.current

  LaunchedEffect(Unit) {
    viewModel.shareUrl.collectLatest { url ->
      intentManager.shareText(url)
      onDismissRequest()
    }
  }

  LaunchedEffect(Unit) {
    viewModel.addToList.collect {
      onNavigateToAddToList(it)
    }
  }

  SnackbarMessageHandler(
    snackbarMessage = uiState.snackbarMessage,
    onDismissSnackbar = viewModel::onDismissSnackbar,
    onShowMessage = onDismissRequest,
  )

  ModalBottomSheet(
    modifier = Modifier.testTag(TestTags.Modal.ACTION_MENU),
    shape = MaterialTheme.shapes.extraLarge,
    onDismissRequest = onDismissRequest,
    sheetState = sheetState,
    content = {
      ActionMenuContent(
        uiState = uiState,
        onMarkAsFavorite = viewModel::onMarkAsFavorite,
        onAction = { intent ->
          when (intent) {
            ActionMenuIntent.MultiSelect -> {
              onMultiSelect(uiState.media)
              onDismissRequest()
            }
            ActionMenuIntent.AddToList -> {
              onDismissRequest()
              viewModel.onAction(intent)
            }
            ActionMenuIntent.Share,
            ActionMenuIntent.RemoveFromList,
              -> viewModel.onAction(intent)
          }
        },
      )
    },
  )
}

@Previews
@Composable
fun ActionMenuContentPreview(
  @PreviewParameter(ActionMenuUiStateParameterProvider::class) state: ActionMenuUiState,
) {
  AppTheme {
    Surface {
      ActionMenuContent(
        uiState = state,
        onMarkAsFavorite = {},
        onAction = {},
      )
    }
  }
}
