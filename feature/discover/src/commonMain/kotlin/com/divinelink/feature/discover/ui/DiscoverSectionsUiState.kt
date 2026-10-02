package com.divinelink.feature.discover.ui

import com.divinelink.core.model.discover.DiscoverType
import com.divinelink.core.model.media.MediaType
import com.divinelink.core.model.tab.MediaTab
import com.divinelink.core.navigation.route.Navigation

data class DiscoverSectionsUiState(
  val uuid: String,
  val type: DiscoverType,
  val selectedTabIndex: Int,
  val tabs: List<MediaTab>,
  val minDate: String,
) {
  companion object {
    fun initial(
      route: Navigation.Discover,
      minDate: String,
    ): DiscoverSectionsUiState {
      val mediaType = route.mediaType
      val (uuid, entryPoint) = when (route) {
        is Navigation.Discover.Custom -> route.entryPoint.value to route.entryPoint
        is Navigation.Discover.Default -> route.entryPointUuid to DiscoverType.Discover
      }

      return DiscoverSectionsUiState(
        uuid = uuid,
        type = entryPoint,
        selectedTabIndex = when (mediaType) {
          MediaType.MOVIE -> MediaTab.Movie.order
          MediaType.TV -> MediaTab.TV.order
          else -> MediaTab.Movie.order
        },
        tabs = MediaTab.entries,
        minDate = minDate,
      )
    }
  }
}
