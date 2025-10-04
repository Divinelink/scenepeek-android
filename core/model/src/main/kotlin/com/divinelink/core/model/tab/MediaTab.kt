package com.divinelink.core.model.tab

import com.divinelink.core.model.R

sealed class MediaTab(
  override val order: Int,
  override val titleRes: Int,
  override val value: String,
) : Tab(order, value, titleRes) {
  data object Movie : MediaTab(
    order = 0,
    value = "movie",
    titleRes = R.string.movie_tab,
  )

  data object TV : MediaTab(
    order = 1,
    value = "show",
    titleRes = R.string.tv_show_tab,
  )

  companion object {
    val entries
      get() = listOf(
        Movie,
        TV,
      )
  }
}
