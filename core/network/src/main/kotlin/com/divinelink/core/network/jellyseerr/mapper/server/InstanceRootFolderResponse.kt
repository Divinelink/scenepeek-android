package com.divinelink.core.network.jellyseerr.mapper.server

import com.divinelink.core.commons.extensions.bytesToHumanReadable
import com.divinelink.core.model.jellyseerr.server.InstanceRootFolder
import com.divinelink.core.network.jellyseerr.model.server.InstanceRootFolderResponse

fun InstanceRootFolderResponse.map() = InstanceRootFolder(
  id = id,
  freeSpace = freeSpace.bytesToHumanReadable(),
  path = path,
)
