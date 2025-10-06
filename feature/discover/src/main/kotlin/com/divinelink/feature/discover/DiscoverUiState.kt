package com.divinelink.feature.discover

import com.divinelink.core.model.Genre
import com.divinelink.core.model.media.MediaItem
import com.divinelink.core.model.media.MediaType
import com.divinelink.core.model.tab.MediaTab

data class DiscoverUiState(
  val selectedTabIndex: Int,
  val tabs: List<MediaTab>,
  val pages: Map<MediaType, Int>,
  val forms: Map<MediaType, DiscoverForm<MediaItem.Media>>,
  val canFetchMore: Map<MediaType, Boolean>,
  val genreFilters: Map<MediaType, List<Genre>>,
) {
  companion object {
    val initial = DiscoverUiState(
      selectedTabIndex = MediaTab.Movie.order,
      tabs = MediaTab.entries,
      pages = mapOf(
        MediaType.MOVIE to 1,
        MediaType.TV to 1,
      ),
      forms = mapOf(
        MediaType.MOVIE to DiscoverForm.Initial,
        MediaType.TV to DiscoverForm.Initial,
      ),
      canFetchMore = mapOf(
        MediaType.MOVIE to true,
        MediaType.TV to true,
      ),
      genreFilters = mapOf(
        MediaType.MOVIE to emptyList(),
        MediaType.TV to emptyList(),
      ),
    )
  }

  val selectedTab = tabs[selectedTabIndex]
  val selectedGenreFilters = genreFilters[selectedTab.mediaType] ?: emptyList()
}
