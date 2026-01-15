package com.divinelink.core.navigation.utilities

import com.divinelink.core.model.home.MediaListSection
import com.divinelink.core.model.media.MediaItem
import com.divinelink.core.model.search.SearchEntryPoint

object NavType {
  val SearchEntryPoint = serializableNavType<SearchEntryPoint>()
  val MediaItem = serializableNavType<MediaItem>()
  val MediaListSection = serializableNavType<MediaListSection>()
}
