package com.divinelink.feature.request.media

import com.divinelink.core.model.details.Season
import com.divinelink.core.model.details.canBeRequested
import com.divinelink.core.model.jellyseerr.media.JellyseerrRequest
import com.divinelink.core.model.jellyseerr.permission.ProfilePermission
import com.divinelink.core.model.jellyseerr.server.InstanceProfile
import com.divinelink.core.model.jellyseerr.server.InstanceRootFolder
import com.divinelink.core.model.jellyseerr.server.ServerInstance
import com.divinelink.core.model.media.MediaItem
import com.divinelink.core.ui.components.dialog.TwoButtonDialogState
import com.divinelink.core.ui.snackbar.SnackbarMessage

data class RequestMediaUiState(
  val request: JellyseerrRequest?,
  val seasons: List<Season>,
  val media: MediaItem.Media,
  val is4k: Boolean,
  val isLoading: Boolean,
  val snackbarMessage: SnackbarMessage?,
  val dialogState: TwoButtonDialogState?,
  val permissions: List<ProfilePermission>,
  val instances: List<ServerInstance>,
  val profiles: List<InstanceProfile>,
  val rootFolders: List<InstanceRootFolder>,
  val selectedInstance: LCEState<ServerInstance>,
  val selectedProfile: LCEState<InstanceProfile>,
  val selectedRootFolder: LCEState<InstanceRootFolder>,
  val selectedSeasons: List<Int>,
) {
  val isEditMode
    get() = request != null

  val validSeasons
    get() = seasons.filterNot { it.seasonNumber == 0 }

  val requestableSeasons
    get() = (
      request
        ?.seasons
        ?.map { it.seasonNumber }
        ?: emptyList()
      ).plus(validSeasons.filter { it.canBeRequested() }.map { it.seasonNumber })

  companion object {
    fun initial(
      request: JellyseerrRequest?,
      media: MediaItem.Media,
    ) = RequestMediaUiState(
      media = media,
      request = request,
      is4k = false,
      isLoading = false,
      snackbarMessage = null,
      dialogState = null,
      seasons = emptyList(),
      permissions = emptyList(),
      instances = emptyList(),
      profiles = emptyList(),
      rootFolders = emptyList(),
      selectedInstance = LCEState.Loading,
      selectedProfile = LCEState.Loading,
      selectedRootFolder = LCEState.Loading,
      selectedSeasons = request?.seasons?.map { it.seasonNumber } ?: emptyList(),
    )
  }
}

sealed interface LCEState<out T> {
  data object Idle : LCEState<Nothing>
  data object Loading : LCEState<Nothing>
  data class Content<T>(val data: T) : LCEState<T>
}
