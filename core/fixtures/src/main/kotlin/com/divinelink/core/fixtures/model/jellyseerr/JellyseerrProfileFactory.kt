package com.divinelink.core.fixtures.model.jellyseerr

import com.divinelink.core.model.jellyseerr.JellyseerrProfile
import com.divinelink.core.model.jellyseerr.ProfilePermission

object JellyseerrProfileFactory {

  fun jellyfin() = JellyseerrProfile(
    id = 1,
    displayName = "Cup10",
    avatar = "http://localhost:5000/avatarproxy/1dde62cf4a2c436d95e17b9",
    requestCount = 10,
    email = "cup10@proton.me",
    createdAt = "2023-08-19T00:00:00.000Z",
    permissions = listOf(
      ProfilePermission.REQUEST,
      ProfilePermission.REQUEST_4K_TV,
      ProfilePermission.REQUEST_MOVIE,
      ProfilePermission.REQUEST_TV,
      ProfilePermission.VIEW_ISSUES,
      ProfilePermission.CREATE_ISSUES,
    ),
  )

  fun jellyseerr() = JellyseerrProfile(
    id = 2,
    displayName = "Zabaob",
    avatar = "",
    requestCount = 20,
    email = "zabaob@proton.me",
    createdAt = "2022-08-20T00:00:00.000Z",
    permissions = listOf(),
  )
}
