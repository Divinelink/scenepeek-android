package com.divinelink.core.ui.blankslate

import com.divinelink.core.model.UIText
import com.divinelink.core.ui.R

sealed class BlankSlateState(
  open val icon: Int? = null,
  open val title: UIText,
  open val description: UIText? = null,
) {
  data object Offline : BlankSlateState(
    icon = R.drawable.core_ui_feeling_blue,
    title = UIText.ResourceText(R.string.core_ui_offline_title),
    description = UIText.ResourceText(R.string.core_ui_offline_description),
  )

  data object Generic : BlankSlateState(
    title = UIText.ResourceText(R.string.core_ui_error_generic_title),
    description = UIText.ResourceText(R.string.core_ui_error_generic_description),
  )

  data class Unauthenticated(override val description: UIText? = null) :
    BlankSlateState(
      icon = com.divinelink.core.model.R.drawable.core_model_ic_tmdb,
      title = UIText.ResourceText(R.string.core_ui_login_title),
      description = description,
    )

  data class Custom(
    override val icon: Int? = null,
    override val title: UIText,
    override val description: UIText? = null,
  ) : BlankSlateState(icon, title, description)
}
