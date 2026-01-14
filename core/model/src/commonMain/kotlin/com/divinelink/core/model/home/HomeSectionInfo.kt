package com.divinelink.core.model.home

import org.jetbrains.compose.resources.StringResource

data class HomeSectionInfo(
  val section: HomeSection.Remote,
  val title: StringResource,
) {
  val key: String
    get() = when (section) {
      HomeSection.TrendingAll -> "trending_all"
      is HomeSection.Popular -> "popular_${section.mediaType.value}"
      is HomeSection.Upcoming -> "upcoming_${section.mediaType.value}"
      is HomeSection.TopRated -> "top_rated_${section.mediaType?.value}"
    }
}
