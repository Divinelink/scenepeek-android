package com.divinelink.core.model.tab

import com.divinelink.core.model.R

sealed interface TvTab {
  data object About : Tab(
    order = 0,
    value = "about",
    titleRes = R.string.core_model_tab_about,
  )

  data object Seasons : Tab(
    order = 1,
    value = "seasons",
    titleRes = R.string.core_model_tab_seasons,
  )

  data object Cast : Tab(
    order = 2,
    value = "cast",
    titleRes = R.string.core_model_tab_cast,
  )

  data object Recommendations : Tab(
    order = 3,
    value = "recommendations",
    titleRes = R.string.core_model_tab_recommendations,
  )

  data object Reviews : Tab(
    order = 4,
    value = "reviews",
    titleRes = R.string.core_model_tab_reviews,
  )
}
