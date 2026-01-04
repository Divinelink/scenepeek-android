package com.divinelink.core.model.tab

import com.divinelink.core.model.media.MediaType
import com.divinelink.core.model.resources.Res
import com.divinelink.core.model.resources.all_tab
import com.divinelink.core.model.resources.movie_tab
import com.divinelink.core.model.resources.people_tab
import com.divinelink.core.model.resources.tv_tab
import org.jetbrains.compose.resources.StringResource

sealed class SearchTab(
  override val order: Int,
  override val titleRes: StringResource,
  override val value: String,
) : Tab(order, value, titleRes) {

  data object All : SearchTab(
    order = 0,
    value = "All",
    titleRes = Res.string.all_tab,
  )

  data object Movie : SearchTab(
    order = 1,
    value = MediaType.MOVIE.value,
    titleRes = Res.string.movie_tab,
  )

  data object TV : SearchTab(
    order = 2,
    value = MediaType.TV.value,
    titleRes = Res.string.tv_tab,
  )

  data object People : SearchTab(
    order = 3,
    value = MediaType.PERSON.value,
    titleRes = Res.string.people_tab,
  )

  companion object {
    val entries
      get() = listOf(
        All,
        Movie,
        TV,
        People,
      )

    fun fromIndex(index: Int): SearchTab = entries.first { it.order == index }
  }
}
