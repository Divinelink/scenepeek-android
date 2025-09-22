package com.divinelink.feature.request.media

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.divinelink.core.data.auth.AuthRepository
import com.divinelink.core.data.jellyseerr.model.JellyseerrRequestParams
import com.divinelink.core.data.jellyseerr.repository.JellyseerrRepository
import com.divinelink.core.domain.jellyseerr.GetServerInstanceDetailsUseCase
import com.divinelink.core.domain.jellyseerr.GetServerInstancesUseCase
import com.divinelink.core.domain.jellyseerr.RequestMediaUseCase
import com.divinelink.core.model.UIText
import com.divinelink.core.model.exception.AppException
import com.divinelink.core.model.jellyseerr.media.JellyseerrMediaInfo
import com.divinelink.core.model.jellyseerr.media.JellyseerrRequest
import com.divinelink.core.model.jellyseerr.permission.canRequestAdvanced
import com.divinelink.core.model.jellyseerr.server.InstanceProfile
import com.divinelink.core.model.jellyseerr.server.InstanceRootFolder
import com.divinelink.core.model.jellyseerr.server.ServerInstance
import com.divinelink.core.network.jellyseerr.model.JellyseerrEditRequestMediaBodyApi
import com.divinelink.core.ui.UiString
import com.divinelink.core.ui.components.dialog.TwoButtonDialogState
import com.divinelink.core.ui.snackbar.SnackbarMessage
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber

class RequestMediaViewModel(
  data: RequestMediaEntryData,
  private val getServerInstancesUseCase: GetServerInstancesUseCase,
  private val getServerInstanceDetailsUseCase: GetServerInstanceDetailsUseCase,
  private val requestMediaUseCase: RequestMediaUseCase,
  private val jellyseerrRepository: JellyseerrRepository,
  authRepository: AuthRepository,
) : ViewModel() {

  private val _uiState = MutableStateFlow(
    RequestMediaUiState.initial(
      request = data.request,
      media = data.media,
      seasons = data.seasons,
    ),
  )
  val uiState = _uiState.asStateFlow()

  private val _updatedMediaInfo = Channel<JellyseerrMediaInfo>()
  val updatedMediaInfo: Flow<JellyseerrMediaInfo> = _updatedMediaInfo.receiveAsFlow()

  private val _updatedRequest = Channel<JellyseerrRequest>()
  val updatedRequest: Flow<JellyseerrRequest> = _updatedRequest.receiveAsFlow()

  init {
    authRepository
      .profilePermissions
      .distinctUntilChanged()
      .onEach { permissions ->
        _uiState.update { it.copy(permissions = permissions) }
      }
      .launchIn(viewModelScope)

    viewModelScope.launch {
      if (uiState.value.permissions.canRequestAdvanced()) {
        getServerInstancesUseCase(uiState.value.media.mediaType).fold(
          onSuccess = { instances ->
            _uiState.update { uiState ->
              uiState.copy(
                instances = instances.filter { it.is4k == uiState.is4k },
              )
            }
            if (instances.isEmpty()) {
              hideAdvancedOptions()
            } else {
              val preferredInstance = when {
                uiState.value.request != null -> instances.find {
                  uiState.value.request!!.serverId == it.id && it.is4k == uiState.value.is4k
                }
                else -> instances.find { it.isDefault && it.is4k == uiState.value.is4k }
              }
              val usePreselected = uiState.value.request != null && preferredInstance != null
              val instanceToSelect = preferredInstance ?: instances.first()

              selectInstance(
                instance = instanceToSelect,
                usePreselected = usePreselected,
              )
            }
          },
          onFailure = {
            hideAdvancedOptions()
          },
        )
      } else {
        hideAdvancedOptions()
      }
    }
  }

  private fun hideAdvancedOptions() {
    _uiState.update { uiState ->
      uiState.copy(
        selectedInstance = LCEState.Idle,
        selectedProfile = LCEState.Idle,
        selectedRootFolder = LCEState.Idle,
      )
    }
  }

  private fun selectInstance(
    instance: ServerInstance,
    usePreselected: Boolean,
  ) {
    if (instance == (uiState.value.selectedInstance as? LCEState.Content)?.data) return

    _uiState.update { uiState ->
      uiState.copy(selectedInstance = LCEState.Content(instance))
    }

    viewModelScope.launch {
      _uiState.update { uiState ->
        uiState.copy(
          selectedProfile = LCEState.Loading,
          selectedRootFolder = LCEState.Loading,
        )
      }
      getServerInstanceDetailsUseCase(
        parameters = GetServerInstanceDetailsUseCase.Parameters(
          mediaType = uiState.value.media.mediaType,
          serverId = instance.id,
        ),
      ).fold(
        onSuccess = { result ->
          _uiState.update { uiState ->
            val defaultProfile = result.profiles.find { profile ->
              profile.id == when {
                usePreselected -> uiState.request?.profileId
                else -> instance.activeProfileId
              }
            }
            val defaultRootFolder = result.rootFolders.find { folder ->
              folder.path == when {
                usePreselected -> uiState.request?.rootFolder
                else -> instance.activeDirectory
              }
            }
            uiState.copy(
              profiles = result.profiles,
              rootFolders = result.rootFolders,
              selectedProfile = if (defaultProfile == null) {
                LCEState.Idle
              } else {
                LCEState.Content(defaultProfile)
              },
              selectedRootFolder = if (defaultRootFolder == null) {
                LCEState.Idle
              } else {
                LCEState.Content(defaultRootFolder)
              },
            )
          }
        },
        onFailure = {
          _uiState.update { uiState ->
            uiState.copy(
              profiles = emptyList(),
              rootFolders = emptyList(),
              selectedProfile = LCEState.Idle,
              selectedRootFolder = LCEState.Idle,
            )
          }
        },
      )
    }
  }

  private fun selectQualityProfile(quality: InstanceProfile) {
    _uiState.update { uiState ->
      uiState.copy(selectedProfile = LCEState.Content(quality))
    }
  }

  private fun selectRootFolder(folder: InstanceRootFolder) {
    _uiState.update { uiState ->
      uiState.copy(selectedRootFolder = LCEState.Content(folder))
    }
  }

  private fun onRequestMedia(seasons: List<Int>) {
    requestMediaUseCase(
      JellyseerrRequestParams(
        mediaId = uiState.value.media.id,
        mediaType = uiState.value.media.mediaType.value,
        seasons = seasons,
        is4k = uiState.value.is4k,
        serverId = (uiState.value.selectedInstance as? LCEState.Content)?.data?.id,
        profileId = (uiState.value.selectedProfile as? LCEState.Content)?.data?.id,
        rootFolder = (uiState.value.selectedRootFolder as? LCEState.Content)?.data?.path,
      ),
    )
      .onStart {
        _uiState.update { uiState ->
          uiState.copy(isLoading = true)
        }
      }
      .onCompletion {
        _uiState.update { uiState ->
          uiState.copy(isLoading = false)
        }
      }
      .onEach { result ->
        result.fold(
          onSuccess = { response ->
            val message = response.message?.let { message ->
              UIText.StringText(message)
            } ?: UIText.ResourceText(
              R.string.feature_request_media_success_request,
              uiState.value.media.name,
            )
            setSnackbarMessage(SnackbarMessage.from(text = message))

            _updatedMediaInfo.send(response.mediaInfo)
          },
          onFailure = { error ->
            when (error) {
              is AppException.Unauthorized,
              is AppException.Forbidden,
              -> _uiState.update {
                it.copy(
                  dialogState = TwoButtonDialogState(
                    message = UIText.ResourceText(
                      UiString.core_ui_jellyseerr_session_expired,
                    ),
                    confirmButtonText = UIText.ResourceText(UiString.core_ui_login),
                    dismissButtonText = UIText.ResourceText(UiString.core_ui_cancel),
                  ),
                )
              }
              is AppException.Conflict -> setSnackbarMessage(
                SnackbarMessage.from(
                  text = UIText.ResourceText(R.string.feature_request_media_request_exists),
                ),
              )

              else -> {
                setSnackbarMessage(
                  SnackbarMessage.from(
                    text = UIText.ResourceText(
                      R.string.feature_request_media_failed_request,
                      uiState.value.media.name,
                    ),
                  ),
                )
              }
            }
          },
        )
      }.launchIn(viewModelScope)
  }

  private fun onEditRequest(seasons: List<Int>) {
    viewModelScope.launch {
      jellyseerrRepository.editRequest(
        JellyseerrEditRequestMediaBodyApi(
          requestId = uiState.value.request?.id,
          mediaId = uiState.value.media.id,
          mediaType = uiState.value.media.mediaType.value,
          seasons = seasons,
          is4k = uiState.value.is4k,
          serverId = (uiState.value.selectedInstance as? LCEState.Content)?.data?.id,
          profileId = (uiState.value.selectedProfile as? LCEState.Content)?.data?.id,
          rootFolder = (uiState.value.selectedRootFolder as? LCEState.Content)?.data?.path,
        ),
      )
        .catch { Timber.d(it) }
        .distinctUntilChanged()
        .collect {
          it.fold(
            onSuccess = { response ->
              val profileName = uiState
                .value
                .profiles
                .find { profile -> profile.id == response.profileId }

              val message = UIText.ResourceText(
                R.string.feature_request_media_update_request_success,
              )

              setSnackbarMessage(SnackbarMessage.from(text = message))

              _updatedRequest.send(
                response.copy(
                  profileName = profileName?.name,
                ),
              )
            },
            onFailure = {
              setSnackbarMessage(
                SnackbarMessage.from(
                  text = UIText.ResourceText(
                    R.string.feature_request_media_update_request_failure,
                  ),
                ),
              )
            },
          )
        }
    }
  }

  private fun dismissSnackbar() {
    _uiState.update { uiState ->
      uiState.copy(snackbarMessage = null)
    }
  }

  private fun dismissDialog() {
    _uiState.update { uiState ->
      uiState.copy(dialogState = null)
    }
  }

  fun onAction(action: RequestMediaAction) {
    when (action) {
      RequestMediaAction.DismissSnackbar -> dismissSnackbar()
      RequestMediaAction.DismissDialog -> dismissDialog()
      is RequestMediaAction.RequestMedia -> if (uiState.value.isEditMode) {
        onEditRequest(action.seasons)
      } else {
        onRequestMedia(action.seasons)
      }
      is RequestMediaAction.SelectInstance -> selectInstance(
        instance = action.instance,
        usePreselected = false,
      )
      is RequestMediaAction.SelectQualityProfile -> selectQualityProfile(action.quality)
      is RequestMediaAction.SelectRootFolder -> selectRootFolder(action.folder)
    }
  }

  private fun setSnackbarMessage(snackbarMessage: SnackbarMessage) {
    _uiState.update { uiState ->
      uiState.copy(
        snackbarMessage = snackbarMessage,
      )
    }
  }
}
