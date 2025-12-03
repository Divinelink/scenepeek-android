package com.divinelink.core.ui.blankslate

import com.divinelink.core.model.UIText
import com.divinelink.core.model.resources.Res
import com.divinelink.core.model.resources.core_model_ic_tmdb
import com.divinelink.core.ui.UiDrawable
import com.divinelink.core.ui.UiString
import com.divinelink.core.ui.resources.core_ui_error_contact_description
import com.divinelink.core.ui.resources.core_ui_error_generic_description
import com.divinelink.core.ui.resources.core_ui_error_generic_title
import com.divinelink.core.ui.resources.core_ui_login_title
import com.divinelink.core.ui.resources.core_ui_offline_description
import com.divinelink.core.ui.resources.core_ui_offline_title
import com.divinelink.core.ui.resources.core_ui_retry
import com.divinelink.core.ui.resources.no_connection
import org.jetbrains.compose.resources.DrawableResource

sealed class BlankSlateState(
  open val icon: DrawableResource? = null,
  open val title: UIText,
  open val description: UIText? = null,
  open val retryText: UIText? = null,
) {
  data object Offline : BlankSlateState(
    icon = UiDrawable.no_connection,
    title = UIText.ResourceText(UiString.core_ui_offline_title),
    description = UIText.ResourceText(UiString.core_ui_offline_description),
    retryText = UIText.ResourceText(UiString.core_ui_retry),
  )

  data object Generic : BlankSlateState(
    title = UIText.ResourceText(UiString.core_ui_error_generic_title),
    description = UIText.ResourceText(UiString.core_ui_error_generic_description),
    retryText = UIText.ResourceText(UiString.core_ui_retry),
  )

  data object Contact : BlankSlateState(
    title = UIText.ResourceText(UiString.core_ui_error_generic_title),
    description = UIText.ResourceText(UiString.core_ui_error_contact_description),
    retryText = UIText.ResourceText(UiString.core_ui_retry),
  )

  data class Unauthenticated(
    override val description: UIText? = null,
    override val retryText: UIText? = null,
  ) : BlankSlateState(
    icon = Res.drawable.core_model_ic_tmdb,
    title = UIText.ResourceText(UiString.core_ui_login_title),
    description = description,
    retryText = retryText,
  )

  data class Custom(
    override val icon: DrawableResource? = null,
    override val title: UIText,
    override val description: UIText? = null,
    override val retryText: UIText? = null,
  ) : BlankSlateState(icon, title, description, retryText)
}
