package com.divinelink.feature.discover

import com.divinelink.core.model.discover.MediaTypeFilters
import com.divinelink.core.model.media.MediaItem
import com.divinelink.core.model.media.MediaType
import com.divinelink.core.model.tab.MediaTab

data class DiscoverUiState(
  val selectedTabIndex: Int,
  val tabs: List<MediaTab>,
  val pages: Map<MediaType, Int>,
  val forms: Map<MediaType, DiscoverForm<MediaItem.Media>>,
  val canFetchMore: Map<MediaType, Boolean>,
  val filters: Map<MediaType, MediaTypeFilters>,
  val loadingMap: Map<MediaType, Boolean>,
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
      filters = mapOf(
        MediaType.MOVIE to MediaTypeFilters.initial,
        MediaType.TV to MediaTypeFilters.initial,
      ),
      loadingMap = mapOf(
        MediaType.MOVIE to false,
        MediaType.TV to false,
      ),
    )
  }

  val selectedTab = tabs[selectedTabIndex]
  val selectedMedia = selectedTab.mediaType
  val currentFilters = filters[selectedTab.mediaType] ?: MediaTypeFilters.initial
  val canFetchMoreForSelectedTab = canFetchMore[selectedMedia] == true
  val isLoading = loadingMap[selectedMedia] == true
}
