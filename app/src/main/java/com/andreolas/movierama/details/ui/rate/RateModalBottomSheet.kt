package com.andreolas.movierama.details.ui.rate

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
  sheetState: SheetState = rememberModalBottomSheetState(),
  value: String?,
  mediaTitle: String,
  onSubmitRate: (Int) -> Unit,
  onRateChanged: (Float) -> Unit,
  onDismissRequest: () -> Unit,
) {
  ModalBottomSheet(
    sheetState = sheetState,
    onDismissRequest = onDismissRequest
  ) {
    RateBottomSheetContent(
      modifier = modifier,
      value = value?.toFloat() ?: 0f,
      mediaTitle = mediaTitle,
      onRateChanged = onRateChanged,
      onSubmitRate = onSubmitRate,
    )
  }
}
