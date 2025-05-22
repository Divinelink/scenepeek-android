package com.divinelink.core.model.tab

import com.divinelink.core.model.R

sealed class MovieTab(
  override val order: Int,
  override val titleRes: Int,
  override val value: String,
) : Tab(order, value, titleRes) {
  data object About : MovieTab(
    order = 0,
    value = "about",
    titleRes = R.string.core_model_tab_about,
  )

  data object Cast : MovieTab(
    order = 1,
    value = "cast",
    titleRes = R.string.core_model_tab_cast,
  )

  data object Recommendations : MovieTab(
    order = 2,
    value = "recommendations",
    titleRes = R.string.core_model_tab_recommendations,
  )

  data object Reviews : MovieTab(
    order = 3,
    value = "reviews",
    titleRes = R.string.core_model_tab_reviews,
  )

  companion object {
    val entries
      get() = listOf(
        About,
        Cast,
        Recommendations,
        Reviews,
      )
  }
}
