package com.divinelink.core.ui.components.dialog

import com.divinelink.core.model.UIText
import com.divinelink.core.ui.R
import com.divinelink.core.ui.UiString

data class AlertDialogUiState(
  val title: UIText = UIText.ResourceText(UiString.core_ui_general_error_title),
  val text: UIText,
)
