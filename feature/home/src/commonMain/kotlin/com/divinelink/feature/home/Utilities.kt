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
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Clock

fun buildHomeSections(clock: Clock): List<HomeSectionInfo> {
  val today = clock.now().toLocalDateTime(TimeZone.currentSystemDefault()).date.toString()

  return listOf(
    HomeSectionInfo(
      section = MediaListSection.TrendingAll,
      title = Res.string.trending,
    ),
    HomeSectionInfo(
      section = MediaListSection.Popular(MediaType.MOVIE),
      title = Res.string.popular_movies,
    ),
    HomeSectionInfo(
      section = MediaListSection.Upcoming(MediaType.MOVIE, minDate = today),
      title = Res.string.upcoming_movies,
    ),
    HomeSectionInfo(
      section = MediaListSection.Popular(MediaType.TV),
      title = Res.string.popular_series,
    ),
    HomeSectionInfo(
      section = MediaListSection.Upcoming(MediaType.TV, minDate = today),
      title = Res.string.upcoming_series,
    ),
  )
}
