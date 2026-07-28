package com.divinelink.core.ui.modal

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.OpenInNew
import androidx.compose.material.icons.filled.Share
import androidx.compose.ui.graphics.vector.ImageVector
import com.divinelink.core.ui.resources.Res
import com.divinelink.core.ui.resources.core_ui_open
import com.divinelink.core.ui.resources.core_ui_share
import org.jetbrains.compose.resources.StringResource

sealed class GenericModalAction(
  val title: StringResource,
  val icon: ImageVector,
) {
  data class Share(val url: String) : GenericModalAction(
    title = Res.string.core_ui_share,
    icon = Icons.Default.Share,
  )

  data class OpenInNew(val url: String) : GenericModalAction(
    title = Res.string.core_ui_open,
    icon = Icons.AutoMirrored.Default.OpenInNew,
  )

  companion object {
    fun forReview(url: String) = listOf(
      OpenInNew(url),
      Share(url),
    )
  }
}
