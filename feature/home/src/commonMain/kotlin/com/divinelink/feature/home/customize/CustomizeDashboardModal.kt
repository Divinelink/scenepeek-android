package com.divinelink.feature.home.customize

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.koin.compose.viewmodel.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomizeDashboardModal(
  viewModel: CustomizeDashboardViewModel = koinViewModel(),
  onDismissRequest: () -> Unit,
) {
  val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
  val state by viewModel.uiState.collectAsStateWithLifecycle()

  ModalBottomSheet(
    shape = MaterialTheme.shapes.extraLarge,
    sheetState = sheetState,
    onDismissRequest = onDismissRequest,
  ) {
    ReorderableHomeSections(
      uiState = state,
      onShowOrHide = viewModel::onToggleSectionVisibility,
      onReorderSections = viewModel::updateSectionOrder,
    )
  }
}
