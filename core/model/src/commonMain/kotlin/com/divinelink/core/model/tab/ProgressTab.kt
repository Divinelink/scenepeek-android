package com.divinelink.core.model.tab

import com.divinelink.core.model.resources.Res
import com.divinelink.core.model.resources.history
import com.divinelink.core.model.resources.on_hold_shows
import com.divinelink.core.model.resources.progress
import com.divinelink.core.model.resources.upcoming
import org.jetbrains.compose.resources.StringResource

sealed class ProgressTab(
  override val order: Int,
  override val titleRes: StringResource,
  override val value: String,
) : Tab(order, value, titleRes) {
  data object Progress : ProgressTab(
    order = 0,
    value = "progress",
    titleRes = Res.string.progress,
  )

  data object Upcoming : ProgressTab(
    order = 1,
    value = "upcoming",
    titleRes = Res.string.upcoming,
  )

  data object History : ProgressTab(
    order = 2,
    value = "history",
    titleRes = Res.string.history,
  )

  data object OnHold : ProgressTab(
    order = 3,
    value = "on_hold",
    titleRes = Res.string.on_hold_shows,
  )

  companion object {
    val entries
      get() = setOf(
        Progress,
        Upcoming,
        History,
        OnHold,
      )
  }
}
