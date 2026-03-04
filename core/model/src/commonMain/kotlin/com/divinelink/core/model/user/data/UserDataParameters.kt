package com.divinelink.core.model.user.data

import com.divinelink.core.model.media.MediaType
import com.divinelink.core.model.sort.SortOption

data class UserDataParameters(
  val page: Int,
  val sortOption: SortOption,
  val mediaType: MediaType,
  val section: UserDataSection,
)
