package com.divinelink.core.model.tab

import com.divinelink.core.model.R
import com.divinelink.core.model.media.MediaType

sealed class MediaTab(
  val mediaType: MediaType,
  override val order: Int,
  override val titleRes: Int,
  override val value: String,
) : Tab(order, value, titleRes) {
  data object Movie : MediaTab(
    mediaType = MediaType.MOVIE,
    order = 0,
    value = MediaType.MOVIE.value,
    titleRes = R.string.movie_tab,
  )

  data object TV : MediaTab(
    mediaType = MediaType.TV,
    order = 1,
    value = MediaType.TV.value,
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
