package com.divinelink.feature.discover

import com.divinelink.core.model.discover.DiscoverFilterType
import com.divinelink.core.model.discover.DiscoverType
import com.divinelink.core.model.discover.MediaTypeFilters
import com.divinelink.core.model.media.MediaItem
import com.divinelink.core.model.media.MediaType
import com.divinelink.core.model.sort.SortOption
import com.divinelink.core.model.ui.SortableSection
import com.divinelink.core.navigation.route.Navigation

data class DiscoverUiState(
  val uuid: String,
  val entryPoint: DiscoverType,
  val page: Int,
  val mediaType: MediaType,
  val form: DiscoverForm<MediaItem.Media>,
  val canFetchMore: Boolean,
  val sortOption: SortOption?,
  val filters: MediaTypeFilters,
  val lastFilters: MediaTypeFilters,
  val loading: Boolean,
  val availableFilters: List<DiscoverFilterType>,
  val minDate: String,
) {
  companion object {
    fun initial(
      route: Navigation.Discover,
      mediaType: MediaType,
      minDate: String,
    ): DiscoverUiState {
      val entryPoint = when (route) {
        is Navigation.Discover.Custom -> route.entryPoint
        is Navigation.Discover.Default -> DiscoverType.Discover
      }

      return DiscoverUiState(
        uuid = route.entryPointUuid,
        entryPoint = entryPoint,
        mediaType = mediaType,
        page = 1,
        form = DiscoverForm.Initial,
        canFetchMore = true,
        filters = MediaTypeFilters.initial(mediaType, entryPoint, minDate),
        lastFilters = MediaTypeFilters.empty,
        sortOption = null,
        loading = false,
        minDate = minDate,
        availableFilters = DiscoverFilterType.availability(entryPoint),
      )
    }
  }

  val section = if (mediaType == MediaType.TV) {
    SortableSection.Discover.Shows(entryPoint)
  } else {
    SortableSection.Discover.Movies(entryPoint)
  }
}
