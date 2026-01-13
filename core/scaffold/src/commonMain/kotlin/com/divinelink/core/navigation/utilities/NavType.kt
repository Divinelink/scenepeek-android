package com.divinelink.core.navigation.utilities

import com.divinelink.core.model.home.HomeSection
import com.divinelink.core.model.media.MediaItem
import com.divinelink.core.model.search.SearchEntryPoint

object NavType {
  val SearchEntryPoint = serializableNavType<SearchEntryPoint>()
  val MediaItem = serializableNavType<MediaItem>()
  val HomeSection = serializableNavType<HomeSection>()
}
