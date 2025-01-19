package com.divinelink.core.model.account

import com.divinelink.core.commons.ApiConstants

data class AccountDetails(
  val id: Int,
  val username: String,
  val name: String,
  val tmdbAvatarPath: String?,
) {
  val avatarUrl = if (tmdbAvatarPath != null) {
    ApiConstants.TMDB_IMAGE_URL + tmdbAvatarPath
  } else {
    null
  }
}
