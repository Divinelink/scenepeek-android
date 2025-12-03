package com.divinelink.core.model.filter

import com.divinelink.core.model.UIText
import com.divinelink.core.model.resources.Res
import com.divinelink.core.model.resources.all
import com.divinelink.core.model.resources.approved
import com.divinelink.core.model.resources.available
import com.divinelink.core.model.resources.completed
import com.divinelink.core.model.resources.deleted
import com.divinelink.core.model.resources.failed
import com.divinelink.core.model.resources.pending
import com.divinelink.core.model.resources.processing
import com.divinelink.core.model.resources.unavailable

sealed class MediaRequestFilter(
  override val value: String,
  override val name: UIText,
) : Filter(value, name) {
  data object All : MediaRequestFilter(
    value = "all",
    name = UIText.ResourceText(Res.string.all),
  )

  data object Approved : MediaRequestFilter(
    value = "approved",
    name = UIText.ResourceText(Res.string.approved),
  )

  data object Available : MediaRequestFilter(
    value = "available",
    name = UIText.ResourceText(Res.string.available),
  )

  data object Pending : MediaRequestFilter(
    value = "pending",
    name = UIText.ResourceText(Res.string.pending),
  )

  data object Processing : MediaRequestFilter(
    value = "processing",
    name = UIText.ResourceText(Res.string.processing),
  )

  data object Unavailable : MediaRequestFilter(
    value = "unavailable",
    name = UIText.ResourceText(Res.string.unavailable),
  )

  data object Failed : MediaRequestFilter(
    value = "failed",
    name = UIText.ResourceText(Res.string.failed),
  )

  data object Deleted : MediaRequestFilter(
    value = "deleted",
    name = UIText.ResourceText(Res.string.deleted),
  )

  data object Completed : MediaRequestFilter(
    value = "completed",
    name = UIText.ResourceText(Res.string.completed),
  )

  companion object {
    val entries
      get() = listOf(
        All,
        Approved,
        Available,
        Pending,
        Processing,
        Unavailable,
        Failed,
        Deleted,
        Completed,
      )
  }
}
