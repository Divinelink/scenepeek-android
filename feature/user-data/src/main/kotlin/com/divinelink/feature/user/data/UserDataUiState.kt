package com.divinelink.feature.user.data

import com.divinelink.core.model.media.MediaItem
import com.divinelink.core.model.media.MediaType
import com.divinelink.core.model.user.data.UserDataSection

/**
 * @param tabs A map of [MediaTab] to the number of items in that tab.
 */
data class UserDataUiState(
  val section: UserDataSection,
  val selectedTabIndex: Int,
  val tabs: Map<MediaTab, Int?>,
  val pages: Map<MediaType, Int>,
  val forms: Map<MediaType, UserDataForm<MediaItem.Media>>,
  val canFetchMore: Map<MediaType, Boolean>,
) {
  val mediaType = MediaType.from(MediaTab.entries[selectedTabIndex].value)
}
