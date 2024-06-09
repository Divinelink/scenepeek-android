package com.andreolas.movierama.ui.components.dialog

import com.andreolas.movierama.R
import com.divinelink.core.ui.UIText

data class AlertDialogUiState(
  val title: UIText = UIText.ResourceText(R.string.general_error_title),
  val text: UIText,
)
