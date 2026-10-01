package com.divinelink.feature.discover

import com.divinelink.core.model.discover.DiscoverFilterType
import com.divinelink.core.model.discover.DiscoverType
import com.divinelink.core.model.discover.MediaTypeFilters
import com.divinelink.core.model.media.MediaItem
import com.divinelink.core.model.media.MediaType
import com.divinelink.core.model.sort.SortOption
import com.divinelink.core.model.tab.MediaTab
import com.divinelink.core.model.ui.SortableSection
import com.divinelink.core.navigation.route.Navigation

data class DiscoverUiState(
  val uuid: String,
  val entryPoint: DiscoverType,
  val selectedTabIndex: Int,
  val tabs: List<MediaTab>,
  val pages: Map<MediaType, Int>,
  val forms: Map<MediaType, DiscoverForm<MediaItem.Media>>,
  val canFetchMore: Map<MediaType, Boolean>,
  val sortOption: Map<MediaType, SortOption>,
  val filters: Map<MediaType, MediaTypeFilters>,
  val lastFilters: Map<MediaType, MediaTypeFilters>,
  val loadingMap: Map<MediaType, Boolean>,
  val availableFilters: List<DiscoverFilterType>,
  val minDate: String,
) {
  companion object {
    fun initial(
      route: Navigation.Discover,
      minDate: String,
    ): DiscoverUiState {
      val mediaType = MediaType.from(route.mediaType)
      val (uuid, entryPoint) = when (route) {
        is Navigation.Discover.Custom -> route.entryPoint.value to route.entryPoint
        is Navigation.Discover.Default -> route.entryPointUuid to DiscoverType.Discover
      }

      return DiscoverUiState(
        uuid = uuid,
        entryPoint = entryPoint,
        selectedTabIndex = when (mediaType) {
          MediaType.MOVIE -> MediaTab.Movie.order
          MediaType.TV -> MediaTab.TV.order
          else -> MediaTab.Movie.order
        },
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
          MediaType.MOVIE to MediaTypeFilters.initial(MediaType.MOVIE, entryPoint, minDate),
          MediaType.TV to MediaTypeFilters.initial(MediaType.TV, entryPoint, minDate),
        ),
        lastFilters = mapOf(
          MediaType.MOVIE to MediaTypeFilters.initial(MediaType.MOVIE, entryPoint, minDate),
          MediaType.TV to MediaTypeFilters.initial(MediaType.TV, entryPoint, minDate),
        ),
        sortOption = emptyMap(),
        loadingMap = mapOf(
          MediaType.MOVIE to false,
          MediaType.TV to false,
        ),
        minDate = minDate,
        availableFilters = DiscoverFilterType.availability(entryPoint),
      )
    }
  }

  val selectedTab = tabs[selectedTabIndex]
  val selectedMedia = selectedTab.mediaType
  val section = if (selectedMedia == MediaType.TV) {
    SortableSection.Discover.Shows(entryPoint)
  } else {
    SortableSection.Discover.Movies(entryPoint)
  }
  val currentFilters = filters[selectedTab.mediaType] ?: MediaTypeFilters.initial(
    mediaType = selectedTab.mediaType,
    entryPoint = entryPoint,
    minDate = minDate,
  )
  val currentLastFilters = lastFilters[selectedTab.mediaType] ?: MediaTypeFilters.initial(
    mediaType = selectedTab.mediaType,
    entryPoint = entryPoint,
    minDate = minDate,
  )
  val canFetchMoreForSelectedTab = canFetchMore[selectedMedia] == true
  val isLoading = loadingMap[selectedMedia] == true
}
