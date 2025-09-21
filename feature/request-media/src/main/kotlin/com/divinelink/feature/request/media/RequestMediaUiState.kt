package com.divinelink.feature.request.media

import com.divinelink.core.model.details.Season
import com.divinelink.core.model.jellyseerr.permission.ProfilePermission
import com.divinelink.core.model.jellyseerr.server.InstanceProfile
import com.divinelink.core.model.jellyseerr.server.InstanceRootFolder
import com.divinelink.core.model.jellyseerr.server.ServerInstance
import com.divinelink.core.model.media.MediaItem
import com.divinelink.core.ui.components.dialog.TwoButtonDialogState
import com.divinelink.core.ui.snackbar.SnackbarMessage

data class RequestMediaUiState(
  val isEditMode: Boolean,
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
) {
  companion object {
    fun initial(
      isEditMode: Boolean,
      seasons: List<Season>,
      media: MediaItem.Media,
    ) = RequestMediaUiState(
      isEditMode = isEditMode,
      seasons = seasons,
      media = media,
      is4k = false,
      isLoading = false,
      snackbarMessage = null,
      dialogState = null,
      permissions = emptyList(),
      instances = emptyList(),
      profiles = emptyList(),
      rootFolders = emptyList(),
      selectedInstance = LCEState.Loading,
      selectedProfile = LCEState.Loading,
      selectedRootFolder = LCEState.Loading,
    )
  }
}

sealed interface LCEState<out T> {
  data object Idle : LCEState<Nothing>
  data object Loading : LCEState<Nothing>
  data class Content<T>(val data: T) : LCEState<T>
}
