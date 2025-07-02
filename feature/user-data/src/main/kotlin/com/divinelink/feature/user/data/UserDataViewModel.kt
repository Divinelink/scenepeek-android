package com.divinelink.feature.user.data

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.divinelink.core.commons.ErrorHandler
import com.divinelink.core.data.session.model.SessionException
import com.divinelink.core.domain.FetchUserDataUseCase
import com.divinelink.core.domain.session.ObserveAccountUseCase
import com.divinelink.core.model.media.MediaType
import com.divinelink.core.model.user.data.UserDataParameters
import com.divinelink.core.model.user.data.UserDataResponse
import com.divinelink.core.model.user.data.UserDataSection
import com.divinelink.core.navigation.route.UserDataRoute
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber
import java.net.UnknownHostException

class UserDataViewModel(
  private val observeAccountUseCase: ObserveAccountUseCase,
  private val fetchUserDataUseCase: FetchUserDataUseCase,
  savedStateHandle: SavedStateHandle,
) : ViewModel() {

  private val route: UserDataRoute = UserDataRoute(
    userDataSection = savedStateHandle.get<UserDataSection>("userDataSection")!!,
  )

  private val _uiState: MutableStateFlow<UserDataUiState> = MutableStateFlow(
    UserDataUiState(
      section = route.userDataSection,
      selectedTabIndex = MediaTab.MOVIE.ordinal,
      tabs = mapOf(
        MediaTab.MOVIE to null,
        MediaTab.TV to null,
      ),
      pages = mapOf(
        MediaType.MOVIE to 1,
        MediaType.TV to 1,
      ),
      forms = mapOf(
        MediaType.MOVIE to UserDataForm.Loading,
        MediaType.TV to UserDataForm.Loading,
      ),
      canFetchMore = mapOf(
        MediaType.MOVIE to true,
        MediaType.TV to true,
      ),
    ),
  )
  val uiState: StateFlow<UserDataUiState> = _uiState

  init {
    viewModelScope.launch {
      observeAccountUseCase.invoke(Unit)
        .collectLatest { result ->
          result.onSuccess {
            fetchUserData(
              section = route.userDataSection,
              mediaType = MediaType.TV,
            )
            fetchUserData(
              section = route.userDataSection,
              mediaType = MediaType.MOVIE,
            )
          }.onFailure { throwable ->
            updateUiOnFailure(MediaType.TV, throwable)
            updateUiOnFailure(MediaType.MOVIE, throwable)
            resetPages()
          }
        }
    }
  }

  fun onLoadMore() {
    val uiState = _uiState.value
    val mediaType = uiState.mediaType
    val section = uiState.section
    val canFetchMore = uiState.canFetchMore[mediaType] ?: false

    if (canFetchMore) {
      fetchUserData(
        section = section,
        mediaType = mediaType,
      )
    }
  }

  fun onTabSelected(tab: Int) {
    _uiState.update { uiState ->
      uiState.copy(selectedTabIndex = tab)
    }
  }

  fun onRefresh() {
    val mediaType = _uiState.value.mediaType

    _uiState.update { uiState ->
      uiState.copy(forms = uiState.forms + (mediaType to UserDataForm.Loading))
    }
    fetchUserData(
      section = uiState.value.section,
      mediaType = mediaType,
    )
  }

  private fun fetchUserData(
    section: UserDataSection,
    mediaType: MediaType,
  ) {
    viewModelScope.launch {
      fetchUserDataUseCase.invoke(
        UserDataParameters(
          page = uiState.value.pages[mediaType] ?: 1,
          section = section,
          mediaType = mediaType,
        ),
      ).collectLatest { result ->
        result
          .onSuccess { response ->
            updateUiOnSuccess(response)
          }
          .onFailure { throwable ->
            updateUiOnFailure(
              mediaType = mediaType,
              throwable = throwable,
            )
          }
      }
    }
  }

  /**
   * Reset pages to 1 when the user logs out or when swipe to refresh is triggered.
   */
  private fun resetPages() {
    _uiState.update { uiState ->
      uiState.copy(
        pages = uiState.pages.mapValues { (_, _) -> 1 },
      )
    }
  }

  private fun updateUiOnFailure(
    mediaType: MediaType,
    throwable: Throwable,
  ) {
    ErrorHandler.create(throwable) {
      on<UnknownHostException> {
        _uiState.update {
          it.copy(
            forms = it.forms + (mediaType to UserDataForm.Error.Network),
          )
        }
      }
      on<SessionException.Unauthenticated> {
        _uiState.update {
          it.copy(
            forms = it.forms + (mediaType to UserDataForm.Error.Unauthenticated),
          )
        }
      }
      otherwise {
        _uiState.update {
          it.copy(
            forms = it.forms + (mediaType to UserDataForm.Error.Unknown),
          )
        }
      }
    }
  }

  private fun updateUiOnSuccess(response: UserDataResponse) {
    _uiState.update { uiState ->
      val currentData = (uiState.forms[response.type] as? UserDataForm.Data)?.data.orEmpty()
      val currentPage = uiState.pages[response.type] ?: 1

      uiState.copy(
        forms = uiState.forms + (
          response.type to UserDataForm.Data(
            mediaType = response.type,
            data = currentData + response.data,
            totalResults = response.totalResults,
          )
          ),
        pages = uiState.pages + (response.type to currentPage + 1),
        canFetchMore = uiState.canFetchMore + (response.type to response.canFetchMore),
        tabs = uiState.tabs + if (response.type == MediaType.MOVIE) {
          MediaTab.MOVIE to response.totalResults
        } else {
          MediaTab.TV to response.totalResults
        },
      ).run {
        Timber.d("Updating Ui for ${response.type} with data ${response.data}")
        Timber.d("Update page for ${response.type} to ${currentPage + 1}")
        Timber.d("Can fetch more for ${response.type} is ${response.canFetchMore}")
        this
      }
    }
  }
}
