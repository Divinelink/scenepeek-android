package com.divinelink.core.model.home

import org.jetbrains.compose.resources.StringResource

data class HomeSectionInfo(
  val section: MediaListSection.Home,
  val title: StringResource,
) {
  val key: String
    get() = when (section) {
      MediaListSection.TrendingAll -> "trending_all"
      is MediaListSection.Popular -> "popular_${section.mediaType.value}"
      is MediaListSection.Upcoming -> "upcoming_${section.mediaType.value}"
    }
}
