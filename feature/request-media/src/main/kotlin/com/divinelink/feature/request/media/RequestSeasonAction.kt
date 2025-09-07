package com.divinelink.feature.request.media

import com.divinelink.core.model.jellyseerr.server.InstanceProfile
import com.divinelink.core.model.jellyseerr.server.InstanceRootFolder
import com.divinelink.core.model.jellyseerr.server.sonarr.SonarrInstance

sealed interface RequestSeasonAction {
  data class RequestMedia(val seasons: List<Int>) : RequestSeasonAction
  data class SelectRootFolder(val folder: InstanceRootFolder) : RequestSeasonAction
  data class SelectQualityProfile(val quality: InstanceProfile) : RequestSeasonAction
  data class SelectInstance(val instance: SonarrInstance) : RequestSeasonAction
  data object DismissSnackbar : RequestSeasonAction
  data object DismissDialog : RequestSeasonAction
}
