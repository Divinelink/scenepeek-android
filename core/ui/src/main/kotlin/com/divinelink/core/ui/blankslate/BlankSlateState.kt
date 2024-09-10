package com.divinelink.core.ui.blankslate

import com.divinelink.core.ui.R
import com.divinelink.core.ui.UIText

sealed class BlankSlateState(
  open val title: UIText,
  open val description: UIText? = null,
) {
  data object Offline : BlankSlateState(
    title = UIText.ResourceText(R.string.core_ui_offline_title),
    description = UIText.ResourceText(R.string.core_ui_offline_description),
  )

  data class Custom(
    override val title: UIText,
    override val description: UIText? = null,
  ) : BlankSlateState(title, description)
}
