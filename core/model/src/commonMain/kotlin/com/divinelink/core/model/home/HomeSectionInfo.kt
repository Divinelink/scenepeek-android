package com.divinelink.core.model.home

import org.jetbrains.compose.resources.StringResource

data class HomeSectionInfo(
  val section: HomeSection,
  val title: StringResource,
) {
  val key: String
    get() = when (section) {
      HomeSection.TrendingAll -> "trending_all"
      is HomeSection.Popular -> "popular_${section.mediaType.name.lowercase()}"
      is HomeSection.Upcoming -> "upcoming_${section.mediaType.name.lowercase()}"
    }
}
