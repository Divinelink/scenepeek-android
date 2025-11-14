package com.divinelink.core.model.list

data class CreatedByUser(
  val id: String,
  val name: String,
  val username: String,
  val avatarPath: String?,
  val gravatarHash: String?,
)
