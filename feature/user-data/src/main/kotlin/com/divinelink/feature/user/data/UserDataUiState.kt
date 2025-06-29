package com.divinelink.feature.user.data

import com.divinelink.core.model.media.MediaItem
import com.divinelink.core.model.media.MediaType
import com.divinelink.core.model.user.data.UserDataSection

data class UserDataUiState(
  val section: UserDataSection,
  val selectedTabIndex: Int,
  val tabs: List<MediaTab>,
  val pages: Map<MediaType, Int>,
  val forms: Map<MediaType, UserDataForm<MediaItem.Media>>,
  val canFetchMore: Map<MediaType, Boolean>,
) {
  val mediaType = MediaType.from(MediaTab.entries[selectedTabIndex].value)

  val totalMovies: Int? = forms[MediaType.MOVIE]?.let {
    if (it is UserDataForm.Data<*>) it.totalResults else null
  }

  val totalTvShows: Int? = forms[MediaType.TV]?.let {
    if (it is UserDataForm.Data<*>) it.totalResults else null
  }
}
