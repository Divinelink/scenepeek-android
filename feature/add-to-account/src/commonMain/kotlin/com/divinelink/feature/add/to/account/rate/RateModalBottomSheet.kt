package com.divinelink.feature.add.to.account.rate

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.divinelink.core.ui.modal.ScenePeekBottomSheet

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RateModalBottomSheet(
  modifier: Modifier = Modifier,
  isVisible: Boolean,
  value: Int?,
  mediaTitle: String,
  onSubmitRate: (Int) -> Unit,
  onClearRate: () -> Unit,
  onDismissRequest: () -> Unit,
) {
  ScenePeekBottomSheet(
    isVisible = isVisible,
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
