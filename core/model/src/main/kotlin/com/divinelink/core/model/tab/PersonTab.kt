package com.divinelink.core.model.tab

import com.divinelink.core.model.R

sealed class PersonTab {
  data object About : Tab(
    order = 0,
    value = "about",
    titleRes = R.string.core_model_tab_about,
  )

  data object Movies : Tab(
    order = 1,
    value = "movies",
    titleRes = R.string.core_model_tab_movies,
  )

  data object TVShows : Tab(
    order = 2,
    value = "tv_shows",
    titleRes = R.string.core_model_tab_tv_shows,
  )

  companion object {
    val entries: List<Tab> = listOf(
      About,
      Movies,
      TVShows,
    )
  }
}
