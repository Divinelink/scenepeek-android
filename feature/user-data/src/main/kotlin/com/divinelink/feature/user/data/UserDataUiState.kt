package com.divinelink.feature.user.data

import com.divinelink.core.model.media.MediaItem
import com.divinelink.core.model.media.MediaType

data class UserDataUiState(
  val selectedTabIndex: Int,
  val tabs: List<MediaTab>,
  val pages: Map<MediaType, Int>,
  val forms: Map<MediaType, UserDataForm<MediaItem.Media>>,
  val canFetchMore: Map<MediaType, Boolean>,
) {
  val mediaType = MediaType.from(MediaTab.entries[selectedTabIndex].value)
}
