package com.divinelink.feature.add.to.account.rate

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RateModalBottomSheet(
  modifier: Modifier = Modifier,
  sheetState: SheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
  value: Int?,
  mediaTitle: String,
  onSubmitRate: (Int) -> Unit,
  onClearRate: () -> Unit,
  onDismissRequest: () -> Unit,
) {
  ModalBottomSheet(
    sheetState = sheetState,
    onDismissRequest = onDismissRequest,
  ) {
    RateDialogContent(
      modifier = modifier,
      value = value?.toFloat() ?: 0f,
      mediaTitle = mediaTitle,
      onSubmitRate = {
        onSubmitRate(it)
        onDismissRequest()
      },
      onClearRate = {
        onClearRate()
        onDismissRequest()
      },
      canClearRate = value != null,
    )
  }
}
