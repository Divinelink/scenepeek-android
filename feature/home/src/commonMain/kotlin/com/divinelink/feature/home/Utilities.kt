package com.divinelink.feature.home

import com.divinelink.core.model.home.HomeSectionInfo
import com.divinelink.core.model.home.HomeSectionsConfig
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
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine

fun List<MediaListSection.Home>.buildSections() = map { section ->
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

fun Flow<HomeSectionsConfig>.withFeatureFlags(
  seerrEnabled: Flow<Boolean>,
): Flow<HomeSectionsConfig> = combine(this, seerrEnabled) { config, seerrEnabled ->
  val displayOrder = if (seerrEnabled) {
    val missingSections = MediaListSection
      .homeEntries
      .filter { it !in config.homeSectionsDisplayOrder && it !in config.hiddenHomeSections }

    if (missingSections.isEmpty()) {
      config.homeSectionsDisplayOrder
    } else {
      config.homeSectionsDisplayOrder + missingSections
    }
  } else {
    config.homeSectionsDisplayOrder.filter { it != MediaListSection.RecentRequests }
  }

  val hiddenSections = if (seerrEnabled) {
    config.hiddenHomeSections
  } else {
    config.hiddenHomeSections.minus(MediaListSection.RecentRequests)
  }

  config.copy(
    homeSectionsDisplayOrder = displayOrder,
    hiddenHomeSections = hiddenSections,
  )
}
