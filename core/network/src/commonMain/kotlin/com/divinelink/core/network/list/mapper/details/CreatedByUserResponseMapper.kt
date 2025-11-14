package com.divinelink.core.network.list.mapper.details

import com.divinelink.core.model.list.CreatedByUser
import com.divinelink.core.network.list.model.details.CreatedByUserResponse

fun CreatedByUserResponse.map() = CreatedByUser(
  id = id,
  name = name,
  username = username,
  avatarPath = avatarPath,
  gravatarHash = gravatarHash,
)
