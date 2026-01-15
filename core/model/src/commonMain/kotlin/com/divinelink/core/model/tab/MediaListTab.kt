package com.divinelink.core.model.tab

import com.divinelink.core.model.media.MediaType
import com.divinelink.core.model.resources.Res
import com.divinelink.core.model.resources.empty_string
import com.divinelink.core.model.resources.movie_tab
import com.divinelink.core.model.resources.tv_show_tab
import org.jetbrains.compose.resources.StringResource

sealed class MediaListTab(
  open val mediaType: MediaType,
  override val order: Int,
  override val titleRes: StringResource,
  override val value: String,
) : Tab(order, value, titleRes) {
  data object Movie : MediaListTab(
    mediaType = MediaType.MOVIE,
    order = 0,
    value = MediaType.MOVIE.value,
    titleRes = Res.string.movie_tab,
  )

  data object TV : MediaListTab(
    mediaType = MediaType.TV,
    order = 1,
    value = MediaType.TV.value,
    titleRes = Res.string.tv_show_tab,
  )

  data object None : MediaListTab(
    mediaType = MediaType.UNKNOWN,
    order = 0,
    value = MediaType.UNKNOWN.value,
    titleRes = Res.string.empty_string,
  )

  companion object {
    val entries
      get() = listOf(
        Movie,
        TV,
      )

    fun from(mediaType: MediaType?): List<MediaListTab> = when (mediaType) {
      MediaType.TV -> listOf(TV)
      MediaType.MOVIE -> listOf(Movie)
      else -> listOf(None)
    }
  }
}
