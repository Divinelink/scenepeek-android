package com.divinelink.core.model.filter

import com.divinelink.core.model.R
import com.divinelink.core.model.UIText

sealed class MediaRequestFilter(
  override val value: String,
  override val name: UIText,
) : Filter(value, name) {
  data object All : MediaRequestFilter(
    value = "all",
    name = UIText.ResourceText(R.string.all),
  )

  data object Approved : MediaRequestFilter(
    value = "approved",
    name = UIText.ResourceText(R.string.approved),
  )

  data object Available : MediaRequestFilter(
    value = "available",
    name = UIText.ResourceText(R.string.available),
  )

  data object Pending : MediaRequestFilter(
    value = "pending",
    name = UIText.ResourceText(R.string.pending),
  )

  data object Processing : MediaRequestFilter(
    value = "processing",
    name = UIText.ResourceText(R.string.processing),
  )

  data object Unavailable : MediaRequestFilter(
    value = "unavailable",
    name = UIText.ResourceText(R.string.unavailable),
  )

  data object Failed : MediaRequestFilter(
    value = "failed",
    name = UIText.ResourceText(R.string.failed),
  )

  data object Deleted : MediaRequestFilter(
    value = "deleted",
    name = UIText.ResourceText(R.string.deleted),
  )

  data object Completed : MediaRequestFilter(
    value = "completed",
    name = UIText.ResourceText(R.string.completed),
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
