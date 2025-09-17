package com.divinelink.core.model.jellyseerr.permission

fun List<ProfilePermission>.canPerform(permission: ProfilePermission): Boolean = isAdmin ||
  contains(permission)

fun List<ProfilePermission>.canManageRequests() = isAdmin || canPerform(
  ProfilePermission.MANAGE_REQUESTS,
)

fun List<ProfilePermission>.canRequest(isTV: Boolean) = isAdmin ||
  contains(ProfilePermission.REQUEST) ||
  if (isTV) {
    contains(ProfilePermission.REQUEST_TV)
  } else {
    contains(ProfilePermission.REQUEST_MOVIE)
  }

fun List<ProfilePermission>.canRequest4K(isTV: Boolean) = isAdmin ||
  contains(ProfilePermission.REQUEST_4K) ||
  if (isTV) {
    contains(ProfilePermission.REQUEST_4K_TV)
  } else {
    contains(ProfilePermission.REQUEST_4K_MOVIE)
  }

val List<ProfilePermission>.isAdmin
  get() = contains(ProfilePermission.ADMIN)
