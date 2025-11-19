package com.divinelink.feature.user.data

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.divinelink.core.domain.FetchUserDataUseCase
import com.divinelink.core.domain.session.ObserveAccountUseCase
import com.divinelink.core.model.exception.AppException
import com.divinelink.core.model.exception.SessionException
import com.divinelink.core.model.media.MediaType
import com.divinelink.core.model.tab.MediaTab
import com.divinelink.core.model.user.data.UserDataParameters
import com.divinelink.core.model.user.data.UserDataResponse
import com.divinelink.core.model.user.data.UserDataSection
import com.divinelink.core.navigation.route.Navigation.UserDataRoute
import io.github.aakira.napier.Napier
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class UserDataViewModel(
  private val observeAccountUseCase: ObserveAccountUseCase,
  private val fetchUserDataUseCase: FetchUserDataUseCase,
  savedStateHandle: SavedStateHandle,
) : ViewModel() {

  private val route: UserDataRoute = UserDataRoute(
    section = UserDataSection.from(savedStateHandle.get<String>("section")!!),
  )

  private val _uiState: MutableStateFlow<UserDataUiState> = MutableStateFlow(
    UserDataUiState(
      section = UserDataSection.from(route.section),
      selectedTabIndex = MediaTab.Movie.order,
      tabs = mapOf(
        MediaTab.Movie to null,
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
              section = UserDataSection.from(route.section),
              mediaType = MediaType.TV,
            )
            fetchUserData(
              section = UserDataSection.from(route.section),
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
      ).distinctUntilChanged()
        .collect { result ->
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
   * Reset paginationData to 1 when the user logs out or when swipe to refresh is triggered.
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
    when (throwable) {
      is AppException.Offline -> _uiState.update {
        it.copy(
          forms = it.forms + (mediaType to UserDataForm.Error.Network),
        )
      }
      is SessionException.Unauthenticated -> _uiState.update {
        it.copy(
          forms = it.forms + (mediaType to UserDataForm.Error.Unauthenticated),
        )
      }
      else -> _uiState.update {
        it.copy(
          forms = it.forms + (mediaType to UserDataForm.Error.Unknown),
        )
      }
    }
  }

  private fun updateUiOnSuccess(response: UserDataResponse) {
    _uiState.update { uiState ->
      val data = (uiState.forms[response.type] as? UserDataForm.Data)?.paginationData

      uiState.copy(
        forms = uiState.forms + (
          response.type to UserDataForm.Data(
            mediaType = response.type,
            paginationData = data?.plus((response.page to response.data)) ?: mapOf(
              1 to response.data,
            ),
            totalResults = response.totalResults,
          )
          ),
        pages = uiState.pages + (response.type to response.page + 1),
        canFetchMore = uiState.canFetchMore + (response.type to response.canFetchMore),
        tabs = uiState.tabs + if (response.type == MediaType.MOVIE) {
          MediaTab.Movie to response.totalResults
        } else {
          MediaTab.TV to response.totalResults
        },
      ).run {
        Napier.d("Updating Ui for ${response.type} with data ${response.data}")
        Napier.d("Can fetch more for ${response.type} is ${response.canFetchMore}")
        this
      }
    }
  }
}
