package com.divinelink.feature.settings.app.backup

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.divinelink.core.commons.util.encodeToString
import com.divinelink.core.data.details.repository.DetailsRepository
import com.divinelink.core.data.track.TrackRepository
import com.divinelink.core.model.export.FileData
import com.divinelink.core.model.media.MediaReference
import com.divinelink.core.model.media.MediaType
import com.divinelink.core.network.Resource
import io.github.aakira.napier.Napier
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.seconds

class BackupViewModel(
  private val trackRepository: TrackRepository,
  private val repository: DetailsRepository,
) : ViewModel() {

  private val _uiState = MutableStateFlow(BackupUiState.initial)
  val uiState: StateFlow<BackupUiState> = _uiState

  private val _exportFile = Channel<FileData>()
  val exportFile: Flow<FileData> = _exportFile.receiveAsFlow()

  fun backupLibrary() {
    _uiState.update { it.copy(loading = true) }
    viewModelScope.launch {
      trackRepository
        .backupLibrary()
        .fold(
          onSuccess = { export ->
            val file = FileData(
              extension = "json",
              name = "scenepeek-export-${export.exportedAt}",
              content = export.encodeToString(),
            )
            _exportFile.send(file)
            _uiState.update { state -> state.copy(loading = false) }
          },
          onFailure = {
            _uiState.update { it.copy(loading = false) }
          },
        )
    }
  }

  fun restore(fileData: FileData?) {
    fileData ?: return

    _uiState.update { it.copy(loading = true) }

    viewModelScope.launch {
      trackRepository
        .restoreLibrary(fileData.content)
        .fold(
          onSuccess = {
            it.forEach { show ->
              delay(1.seconds)
              repository.fetchMediaItem(
                media = MediaReference(
                  mediaId = show.id,
                  mediaType = MediaType.TV,
                ),
              ).first { result -> result is Resource.Success }
            }
            _uiState.update { state -> state.copy(loading = false) }
          },
          onFailure = {
            _uiState.update { state -> state.copy(loading = false) }
          },
        )
    }
  }
}
