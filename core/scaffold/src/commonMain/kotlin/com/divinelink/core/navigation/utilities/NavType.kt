package com.divinelink.core.navigation.utilities

import com.divinelink.core.model.media.MediaItem
import com.divinelink.core.model.person.Gender
import com.divinelink.core.model.search.SearchEntryPoint
import com.divinelink.core.model.user.data.UserDataSection

object NavType {
  val SearchEntryPoint = serializableNavType<SearchEntryPoint>()
  val MediaItem = serializableNavType<MediaItem>()
}
