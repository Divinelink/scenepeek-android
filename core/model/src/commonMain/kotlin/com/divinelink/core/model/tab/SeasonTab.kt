package com.divinelink.core.model.tab

import com.divinelink.core.model.resources.Res
import com.divinelink.core.model.resources.core_model_tab_about
import com.divinelink.core.model.resources.core_model_tab_cast
import com.divinelink.core.model.resources.core_model_tab_episodes
import org.jetbrains.compose.resources.StringResource

sealed class SeasonTab(
  override val order: Int,
  override val titleRes: StringResource,
  override val value: String,
) : Tab(order, value, titleRes) {

  data object Episodes : SeasonTab(
    order = 0,
    value = "episodes",
    titleRes = Res.string.core_model_tab_episodes,
  )

  data object About : SeasonTab(
    order = 1,
    value = "about",
    titleRes = Res.string.core_model_tab_about,
  )

  data object Cast : SeasonTab(
    order = 2,
    value = "cast",
    titleRes = Res.string.core_model_tab_cast,
  )

  companion object {
    val entries
      get() = listOf(
        Episodes,
        About,
        Cast,
      )
  }
}
