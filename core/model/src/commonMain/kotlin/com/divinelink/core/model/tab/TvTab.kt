package com.divinelink.core.model.tab

import com.divinelink.core.model.Res
import com.divinelink.core.model.core_model_tab_about
import com.divinelink.core.model.core_model_tab_cast
import com.divinelink.core.model.core_model_tab_recommendations
import com.divinelink.core.model.core_model_tab_reviews
import com.divinelink.core.model.core_model_tab_seasons
import org.jetbrains.compose.resources.StringResource

sealed class TvTab(
  override val order: Int,
  override val titleRes: StringResource,
  override val value: String,
) : Tab(order, value, titleRes) {
  data object About : TvTab(
    order = 0,
    value = "about",
    titleRes = Res.string.core_model_tab_about,
  )

  data object Seasons : TvTab(
    order = 1,
    value = "seasons",
    titleRes = Res.string.core_model_tab_seasons,
  )

  data object Cast : TvTab(
    order = 2,
    value = "cast",
    titleRes = Res.string.core_model_tab_cast,
  )

  data object Recommendations : TvTab(
    order = 3,
    value = "recommendations",
    titleRes = Res.string.core_model_tab_recommendations,
  )

  data object Reviews : TvTab(
    order = 4,
    value = "reviews",
    titleRes = Res.string.core_model_tab_reviews,
  )

  companion object {
    val entries
      get() = listOf(
        About,
        Seasons,
        Cast,
        Recommendations,
        Reviews,
      )
  }
}
