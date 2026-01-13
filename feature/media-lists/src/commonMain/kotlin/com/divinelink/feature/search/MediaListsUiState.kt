package com.divinelink.feature.search

import com.divinelink.core.model.UIText
import com.divinelink.core.model.home.HomeSection
import com.divinelink.core.model.media.MediaItem
import com.divinelink.core.model.media.MediaType
import com.divinelink.core.model.resources.Res
import com.divinelink.core.model.resources.popular_movies
import com.divinelink.core.model.resources.popular_series
import com.divinelink.core.model.resources.trending
import com.divinelink.core.model.resources.upcoming_movies
import com.divinelink.core.model.resources.upcoming_series
import com.divinelink.feature.search.ui.MediaListsForm

data class MediaListsUiState(
  val section: HomeSection,
  val title: UIText,
  val page: Int,
  val form: MediaListsForm<MediaItem>,
) {
  companion object {
    fun initial(section: HomeSection) = MediaListsUiState(
      section = section,
      title = when (section) {
        HomeSection.TrendingAll -> UIText.ResourceText(Res.string.trending)
        is HomeSection.Popular -> if (section.mediaType == MediaType.MOVIE) {
          UIText.ResourceText(Res.string.popular_movies)
        } else {
          UIText.ResourceText(Res.string.popular_series)
        }
        is HomeSection.Upcoming -> if (section.mediaType == MediaType.MOVIE) {
          UIText.ResourceText(Res.string.upcoming_movies)
        } else {
          UIText.ResourceText(Res.string.upcoming_series)
        }
      },
      page = 0,
      form = MediaListsForm.Initial,
    )
  }
}
