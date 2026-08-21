package com.divinelink.feature.home

import com.divinelink.core.model.home.HomeSectionInfo
import com.divinelink.core.model.home.MediaListSection
import com.divinelink.core.model.media.MediaType
import com.divinelink.core.model.resources.Res
import com.divinelink.core.model.resources.popular_movies
import com.divinelink.core.model.resources.popular_series
import com.divinelink.core.model.resources.trending
import com.divinelink.core.model.resources.upcoming_movies
import com.divinelink.core.model.resources.upcoming_series
import com.divinelink.core.ui.UiString
import com.divinelink.core.ui.resources.recent_request

fun buildHomeSections(
  sections: List<MediaListSection.Home>,
): List<HomeSectionInfo> = sections.map { section ->
  val title = when (section) {
    MediaListSection.RecentRequests -> UiString.recent_request
    MediaListSection.TrendingAll -> Res.string.trending
    is MediaListSection.Popular -> if (section.mediaType == MediaType.MOVIE) {
      Res.string.popular_movies
    } else {
      Res.string.popular_series
    }
    is MediaListSection.Upcoming -> if (section.mediaType == MediaType.MOVIE) {
      Res.string.upcoming_movies
    } else {
      Res.string.upcoming_series
    }
  }

  HomeSectionInfo(
    title = title,
    section = section,
  )
}
