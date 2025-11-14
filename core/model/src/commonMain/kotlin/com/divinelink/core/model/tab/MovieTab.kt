package com.divinelink.core.model.tab

import com.divinelink.core.model.Res
import com.divinelink.core.model.core_model_tab_about
import com.divinelink.core.model.core_model_tab_cast
import com.divinelink.core.model.core_model_tab_recommendations
import com.divinelink.core.model.core_model_tab_reviews
import org.jetbrains.compose.resources.StringResource

sealed class MovieTab(
  override val order: Int,
  override val titleRes: StringResource,
  override val value: String,
) : Tab(order, value, titleRes) {
  data object About : MovieTab(
    order = 0,
    value = "about",
    titleRes = Res.string.core_model_tab_about,
  )

  data object Cast : MovieTab(
    order = 1,
    value = "cast",
    titleRes = Res.string.core_model_tab_cast,
  )

  data object Recommendations : MovieTab(
    order = 2,
    value = "recommendations",
    titleRes = Res.string.core_model_tab_recommendations,
  )

  data object Reviews : MovieTab(
    order = 3,
    value = "reviews",
    titleRes = Res.string.core_model_tab_reviews,
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
