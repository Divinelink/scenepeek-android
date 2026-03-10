package com.divinelink.feature.updater.ui

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.divinelink.core.navigation.route.Navigation
import com.divinelink.feature.updater.UpdaterAction
import com.divinelink.feature.updater.UpdaterViewModel
import kotlinx.coroutines.launch
import org.koin.compose.viewmodel.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UpdaterScreen(
  onNavigate: (Navigation) -> Unit,
  viewModel: UpdaterViewModel = koinViewModel(),
) {
  val uiState by viewModel.uiState.collectAsStateWithLifecycle()
  val sheetState = rememberModalBottomSheetState()
  val scope = rememberCoroutineScope()

  ModalBottomSheet(
    modifier = Modifier,
    sheetState = sheetState,
    shape = MaterialTheme.shapes.extraLarge,
    onDismissRequest = { onNavigate(Navigation.Back) },
    content = {
      UpdaterContent(
        uiState = uiState,
        action = { action ->
          when (action) {
            UpdaterAction.Dismiss -> scope.launch {
              sheetState.hide()
              onNavigate(Navigation.Back)
            }
          }
        },
      )
    },
  )
}
