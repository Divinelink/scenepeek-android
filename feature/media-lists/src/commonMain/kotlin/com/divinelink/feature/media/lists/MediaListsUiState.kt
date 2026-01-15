package com.divinelink.feature.media.lists

import com.divinelink.core.model.UIText
import com.divinelink.core.model.home.MediaListSection
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
 * This screen conditionally displays tabs based on the provided [MediaListSection].
 *
 * Tabs are determined at initialization time. When the section already specifies
 * a concrete media type (e.g., "Popular Movies"), tabs are hidden because the context
 * is unambiguous. However, for sections like "Favorites" or "Top Rated" that apply
 * to multiple media types, Movie and TV Show tabs are shown to let the user choose.
 */
data class MediaListsUiState(
  val selectedTabIndex: Int,
  val tabs: List<MediaListTab>,
  val section: MediaListSection,
  val title: UIText,
  val pages: Map<MediaType, Int>,
  val forms: Map<MediaType, MediaListsForm<MediaItem>>,
) {
  val selectedTab = tabs[selectedTabIndex]
  val selectedMediaType = selectedTab.mediaType
  val selectedForm = forms[selectedMediaType]
  val showTabs = tabs.isNotEmpty() && tabs.size != 1

  companion object {
    fun initial(section: MediaListSection) = MediaListsUiState(
      selectedTabIndex = 0,
      section = section,
      title = when (section) {
        MediaListSection.TrendingAll -> UIText.ResourceText(Res.string.trending)
        is MediaListSection.Popular -> if (section.mediaType == MediaType.MOVIE) {
          UIText.ResourceText(Res.string.popular_movies)
        } else {
          UIText.ResourceText(Res.string.popular_series)
        }
        is MediaListSection.Upcoming -> if (section.mediaType == MediaType.MOVIE) {
          UIText.ResourceText(Res.string.upcoming_movies)
        } else {
          UIText.ResourceText(Res.string.upcoming_series)
        }
        MediaListSection.Favorites -> UIText.ResourceText(Res.string.favorites)
        is MediaListSection.TopRated -> UIText.ResourceText(Res.string.top_rated)
      },
      pages = emptyMap(),
      forms = when (section) {
        MediaListSection.Favorites -> mapOf(
          MediaType.MOVIE to MediaListsForm.Initial,
          MediaType.TV to MediaListsForm.Initial,
        )
        is MediaListSection.Popular -> mapOf(
          section.mediaType to MediaListsForm.Initial,
        )
        is MediaListSection.TopRated -> mapOf(
          MediaType.MOVIE to MediaListsForm.Initial,
          MediaType.TV to MediaListsForm.Initial,
        )
        MediaListSection.TrendingAll -> mapOf(
          MediaType.UNKNOWN to MediaListsForm.Initial,
        )
        is MediaListSection.Upcoming -> mapOf(
          section.mediaType to MediaListsForm.Initial,
        )
      },
      tabs = when (section) {
        MediaListSection.Favorites -> MediaListTab.entries
        is MediaListSection.TopRated -> MediaListTab.entries
        is MediaListSection.Popular -> MediaListTab.from(section.mediaType)
        is MediaListSection.Upcoming -> MediaListTab.from(section.mediaType)
        MediaListSection.TrendingAll -> MediaListTab.from(null)
      },
    )
  }
}
