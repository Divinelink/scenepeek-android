package com.divinelink.core.model.user.data

import com.divinelink.core.model.media.MediaType

data class UserDataParameters(
  val page: Int,
  val sortBy: UserDataSorting = UserDataSorting.DESCENDING,
  val mediaType: MediaType,
)
