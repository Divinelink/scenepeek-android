package com.divinelink.feature.settings.app.backup

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.divinelink.core.data.app.AppInfoRepository
import com.divinelink.core.data.preferences.PreferencesRepository
import com.divinelink.core.data.track.TrackRepository
import com.divinelink.core.domain.BackupManager
import com.divinelink.core.model.UIText
import com.divinelink.core.ui.UiString
import com.divinelink.core.ui.components.dialog.DialogState
import com.divinelink.core.ui.resources.backup_completed
import com.divinelink.core.ui.resources.backup_failed
import com.divinelink.core.ui.resources.restore_failed
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class BackupViewModel(
  private val trackRepository: TrackRepository,
  private val preferencesRepository: PreferencesRepository,
  private val appInfoRepository: AppInfoRepository,
  private val backupManager: BackupManager,
) : ViewModel() {

  private val _uiState = MutableStateFlow(BackupUiState.initial)
  val uiState: StateFlow<BackupUiState> = _uiState

  init {
    appInfoRepository
      .backupInfo
      .distinctUntilChanged()
      .onEach { info ->
        _uiState.update {
          it.copy(backupInfo = info)
        }
      }
      .launchIn(viewModelScope)
  }

  fun onAction(action: BackupAction) {
    when (action) {
      is BackupAction.SetBackupLocation -> viewModelScope.launch {
        backupManager.getFolderPath()?.let { path ->
          preferencesRepository.setBackupPath(path)
        }
      }
      is BackupAction.UpdateAutomaticBackups -> viewModelScope.launch {
        preferencesRepository.setAutomaticBackups(action.enabled)
      }
    }
  }

  fun backupLibrary() {
    _uiState.update { it.copy(loading = true) }
    viewModelScope.launch {
      trackRepository
        .backupLibrary()
        .fold(
          onSuccess = { backup ->
            backupManager.saveFile(backup).fold(
              onSuccess = { saved ->
                if (saved) {
                  setDialog(
                    dialog = DialogState(
                      message = UIText.ResourceText(UiString.backup_completed),
                      error = null,
                    ),
                  )
                  appInfoRepository.updateLastBackup()
                }
              },
              onFailure = {
                setDialog(
                  DialogState(
                    message = UIText.ResourceText(UiString.backup_failed),
                    error = null,
                  ),
                )
              },
            )
          },
          onFailure = { error ->
            _uiState.update {
              it.copy(
                loading = false,
                dialog = DialogState(
                  message = UIText.ResourceText(UiString.backup_failed),
                  error = error,
                ),
              )
            }
          },
        )
    }
  }

  fun restore() {
    viewModelScope.launch {
      val fileData = backupManager.readFile(setOf("json")) ?: return@launch
      _uiState.update { it.copy(loading = true) }

      trackRepository
        .restoreLibrary(fileData.content)
        .fold(
          onSuccess = { result ->
            _uiState.update { state ->
              state.copy(
                loading = false,
                restoreProcess = result,
              )
            }
          },
          onFailure = {
            _uiState.update { state ->
              state.copy(
                loading = false,
                dialog = DialogState(
                  title = null,
                  message = UIText.ResourceText(UiString.restore_failed),
                  error = it,
                ),
              )
            }
          },
        )
    }
  }

  fun setDialog(dialog: DialogState?) {
    _uiState.update {
      it.copy(
        restoreProcess = null,
        loading = false,
        dialog = dialog,
      )
    }
  }
}
