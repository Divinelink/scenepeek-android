package com.divinelink.feature.media.lists

import com.divinelink.core.model.UIText
import com.divinelink.core.model.home.HomeSection
import com.divinelink.core.model.media.MediaItem
import com.divinelink.core.model.media.MediaType
import com.divinelink.core.model.resources.Res
import com.divinelink.core.model.resources.favorites
import com.divinelink.core.model.resources.popular_movies
import com.divinelink.core.model.resources.popular_series
import com.divinelink.core.model.resources.top_rated
import com.divinelink.core.model.resources.trending
import com.divinelink.core.model.resources.upcoming_movies
import com.divinelink.core.model.resources.upcoming_series
import com.divinelink.core.model.tab.MediaListTab
import com.divinelink.feature.media.lists.ui.MediaListsForm

/**
 * This screen conditionally displays tabs based on the provided [HomeSection].
 *
 * Tabs are determined at initialization time. When the section already specifies
 * a concrete media type (e.g., "Popular Movies"), tabs are hidden because the context
 * is unambiguous. However, for sections like "Favorites" or "Top Rated" that apply
 * to multiple media types, Movie and TV Show tabs are shown to let the user choose.
 */
data class MediaListsUiState(
  val selectedTabIndex: Int,
  val tabs: List<MediaListTab>,
  val section: HomeSection,
  val title: UIText,
  val pages: Map<MediaType, Int>,
  val forms: Map<MediaType, MediaListsForm<MediaItem>>,
) {
  val selectedTab = tabs[selectedTabIndex]
  val selectedMediaType = selectedTab.mediaType
  val selectedForm = forms[selectedMediaType]

  companion object {
    fun initial(section: HomeSection) = MediaListsUiState(
      selectedTabIndex = 0,
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
        HomeSection.Favorites -> UIText.ResourceText(Res.string.favorites)
        is HomeSection.TopRated -> UIText.ResourceText(Res.string.top_rated)
      },
      pages = emptyMap(),
      forms = when (section) {
        HomeSection.Favorites -> mapOf(
          MediaType.MOVIE to MediaListsForm.Initial,
          MediaType.TV to MediaListsForm.Initial,
        )
        is HomeSection.Popular -> mapOf(
          section.mediaType to MediaListsForm.Initial,
        )
        is HomeSection.TopRated -> mapOf(
          MediaType.MOVIE to MediaListsForm.Initial,
          MediaType.TV to MediaListsForm.Initial,
        )
        HomeSection.TrendingAll -> mapOf(
          MediaType.UNKNOWN to MediaListsForm.Initial,
        )
        is HomeSection.Upcoming -> mapOf(
          section.mediaType to MediaListsForm.Initial,
        )
      },
      tabs = when (section) {
        HomeSection.Favorites -> MediaListTab.entries
        is HomeSection.TopRated -> MediaListTab.entries
        is HomeSection.Popular -> MediaListTab.from(section.mediaType)
        is HomeSection.Upcoming -> MediaListTab.from(section.mediaType)
        HomeSection.TrendingAll -> MediaListTab.from(MediaType.UNKNOWN)
      },
    )
  }
}
