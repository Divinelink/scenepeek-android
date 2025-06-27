package com.divinelink.feature.details.media.ui.rate

import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.navigationBarsIgnoringVisibility
import androidx.compose.foundation.layout.windowInsetsBottomHeight
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun RateModalBottomSheet(
  modifier: Modifier = Modifier,
  sheetState: SheetState,
  value: Int?,
  mediaTitle: String,
  canClearRate: Boolean,
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
      onSubmitRate = onSubmitRate,
      onClearRate = onClearRate,
      canClearRate = canClearRate,
    )
    Spacer(Modifier.windowInsetsBottomHeight(WindowInsets.navigationBarsIgnoringVisibility))
  }
}
