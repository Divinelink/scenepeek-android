package com.divinelink.core.model.tab

import com.divinelink.core.model.resources.Res
import com.divinelink.core.model.resources.history
import com.divinelink.core.model.resources.progress
import org.jetbrains.compose.resources.StringResource

sealed class ProgressTab(
  override val order: Int,
  override val titleRes: StringResource,
  override val value: String,
) : Tab(order, value, titleRes) {
  data object NextUp : ProgressTab(
    order = 0,
    value = "nextup",
    titleRes = Res.string.progress,
  )

  data object History : ProgressTab(
    order = 1,
    value = "history",
    titleRes = Res.string.history,
  )

  companion object {
    val entries
      get() = listOf(
        NextUp,
        History,
      )
  }
}
