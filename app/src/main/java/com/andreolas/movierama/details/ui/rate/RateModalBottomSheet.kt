package com.andreolas.movierama.details.ui.rate

import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.navigationBarsIgnoringVisibility
import androidx.compose.foundation.layout.windowInsetsBottomHeight
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
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
    RateDialogContent(
      modifier = modifier,
      value = value?.toFloat() ?: 0f,
      mediaTitle = mediaTitle,
      onRateChanged = onRateChanged,
      onSubmitRate = onSubmitRate,
      onDeleteRate = { }
    )
    Spacer(Modifier.windowInsetsBottomHeight(WindowInsets.navigationBarsIgnoringVisibility))
  }
}
