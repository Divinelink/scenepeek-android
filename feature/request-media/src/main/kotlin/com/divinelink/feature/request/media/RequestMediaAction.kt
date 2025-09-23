package com.divinelink.feature.request.media

import com.divinelink.core.model.jellyseerr.server.InstanceProfile
import com.divinelink.core.model.jellyseerr.server.InstanceRootFolder
import com.divinelink.core.model.jellyseerr.server.ServerInstance

sealed interface RequestMediaAction {
  data class RequestMedia(val seasons: List<Int>) : RequestMediaAction
  data class SelectRootFolder(val folder: InstanceRootFolder) : RequestMediaAction
  data class SelectQualityProfile(val quality: InstanceProfile) : RequestMediaAction
  data class SelectInstance(val instance: ServerInstance) : RequestMediaAction
  data class SelectAllSeasons(val selectAll: Boolean) : RequestMediaAction
  data class SelectSeason(val number: Int) : RequestMediaAction
  data object DismissSnackbar : RequestMediaAction
  data object DismissDialog : RequestMediaAction
}
