package com.divinelink.core.ui.components.dialog

import com.divinelink.core.ui.R
import com.divinelink.core.ui.UIText

data class AlertDialogUiState(
  val title: UIText = UIText.ResourceText(R.string.general_error_title),
  val text: UIText,
)
