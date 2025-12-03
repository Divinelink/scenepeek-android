package com.divinelink.core.model.tab

import com.divinelink.core.model.resources.Res
import com.divinelink.core.model.resources.core_model_tab_about
import com.divinelink.core.model.resources.core_model_tab_movies
import com.divinelink.core.model.resources.core_model_tab_tv_shows

sealed class PersonTab {
  data object About : Tab(
    order = 0,
    value = "about",
    titleRes = Res.string.core_model_tab_about,
  )

  data object Movies : Tab(
    order = 1,
    value = "movies",
    titleRes = Res.string.core_model_tab_movies,
  )

  data object TVShows : Tab(
    order = 2,
    value = "tv_shows",
    titleRes = Res.string.core_model_tab_tv_shows,
  )

  companion object {
    val entries: List<Tab>
      get() = listOf(
        About,
        Movies,
        TVShows,
      )
  }
}
