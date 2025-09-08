package com.divinelink.feature.request.media

import com.divinelink.core.model.details.Season
import com.divinelink.core.model.jellyseerr.server.InstanceProfile
import com.divinelink.core.model.jellyseerr.server.InstanceRootFolder
import com.divinelink.core.model.jellyseerr.server.ServerInstance
import com.divinelink.core.model.media.MediaItem
import com.divinelink.core.ui.components.dialog.TwoButtonDialogState
import com.divinelink.core.ui.snackbar.SnackbarMessage

data class RequestMediaUiState(
  val seasons: List<Season>,
  val media: MediaItem.Media,
  val is4k: Boolean,
  val isLoading: Boolean,
  val snackbarMessage: SnackbarMessage?,
  val dialogState: TwoButtonDialogState?,
  val instances: List<ServerInstance>,
  val profiles: List<InstanceProfile>,
  val rootFolders: List<InstanceRootFolder>,
  val selectedInstance: LCEState<ServerInstance>,
  val selectedProfile: LCEState<InstanceProfile>,
  val selectedRootFolder: LCEState<InstanceRootFolder>,
) {
  companion object {
    fun initial(
      seasons: List<Season>,
      media: MediaItem.Media,
    ) = RequestMediaUiState(
      seasons = seasons,
      media = media,
      is4k = false,
      isLoading = false,
      snackbarMessage = null,
      dialogState = null,
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
  data object Error : LCEState<Nothing>
  data object Loading : LCEState<Nothing>
  data class Content<T>(val data: T) : LCEState<T>
}
