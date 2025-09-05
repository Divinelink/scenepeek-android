package com.divinelink.core.network.jellyseerr.mapper.server

import com.divinelink.core.model.jellyseerr.server.InstanceProfile
import com.divinelink.core.network.jellyseerr.model.server.InstanceProfileResponse

fun InstanceProfileResponse.map() = InstanceProfile(
  id = id,
  name = name,
)
